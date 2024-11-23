package unicrafter.world;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Scaling;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.Effect;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.io.TypeIO;
import mindustry.type.*;
import mindustry.ui.Bar;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.blocks.heat.HeatBlock;
import mindustry.world.blocks.heat.HeatConsumer;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.blocks.payloads.PayloadBlock;
import mindustry.world.blocks.payloads.UnitPayload;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItemDynamic;
import mindustry.world.consumers.ConsumePayloadDynamic;
import mindustry.world.consumers.ConsumePowerDynamic;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawRegion;
import mindustry.world.meta.Stat;
import unicrafter.ConsumeLiquidsDynamic;
import unicrafter.UniFx;
import unicrafter.recipes.Recipe;

import static mindustry.Vars.*;

/**A multicrafter that should support all in- and output types.
 * Can be considered the base for the recipes to do their work on, does nothing on its own.
 * @author nullevoy */

/* the list
* [X] produce items
* [X] produce payloads
* [X] consume items
* [X] consume liquids
* [X] consume payloads
* [X] consume power
* [X] switch configs
* [X] outDirs
* [X] UI
* [X] stats
* [X] unit cost mul
* [X] drawing
* [X] separator recipe class
* [X] Call/etc. for unit spawns
* [X] separator recipe ui
* [X] add missing bars
*
* [-] attributes (floor + side)
* [-] add heat/attributes to recipe ui
* [ ] schematic compat
* [ ] produce liquids //needs support for multiple
* [ ] produce power
* [L] heat //fat fucking L
*
* //post-release goals
* [ ] generator explosion/death/damage + more
* [ ] container IO
* [ ] liquid container IO
* [ ] logic?
* [ ] aqueduct/etc. ports
*/

public class UniversalCrafter extends PayloadBlock{
    public Seq<Recipe> recipes = new Seq<>();

    //general
    public float warmupSpeed = 0.019f;
    public float heatIncreaseSpeed = 0.15f;

    //io
    /**instantInput will consume a payload as soon as it enters. Best used with UniFx.payInstantDespawn.*/
    public boolean instantInput = true;
    /**Outputs the first payload without an effect, for if you need the payload to look like it was already there.*/
    public boolean instantFirstOutput = false;
    /**The block waits for the spawn effect to finish before outputting again, use Fx.none to get instant output.*/
    //public Effect paySpawnEffect = UniFx.payRespawn;
    /**Enables the above behavior.*/
    public boolean waitForSpawnEffect = true;
    /**Use UniFx.payDespawn instead for unspecial non-instant ones*/
    //public Effect payDespawnEffect = UniFx.payInstantDespawn;

    //visuals
    /**Recipe drawers are drawn between these.*/
    public DrawBlock drawerBottom = new DrawDefault(), drawerTop = new DrawRegion("-top");
    public DrawBlock drawerRecipeDefault;
    /**Whether to draw vanilla payload sprites. TODO convert to drawer or no?*/
    public boolean vanillaIO = false;

    public UniversalCrafter(String name){
        super(name);
        configurable = true;
        solid = true;
    }

    /*--- Set automatically ---*/
    public int[] capacities = {};
    public boolean hasHeat;
    public boolean hasPayloads, hasLiquidIn;
    public Consume consPayload, consItem, consPower, consLiquid;

    @Override
    public void init(){
        capacities = new int[Vars.content.items().size];
        consumesPower = false;

        if(recipes != null && recipes.size == 1) configurable = false;

        config(Integer.class, (UniversalBuild tile, Integer num) -> {
            var val = tile.recipes().get(num);
            if(!configurable || tile.currentRecipe == val) return;

            tile.currentRecipe = val;
            tile.currentRecipe.configTo(tile);

            tile.progress = 0;
        });

        super.init();

        if(recipes != null) initRecipes(recipes);

        if(hasPayloads) consume(consPayload = new ConsumePayloadDynamic((UniversalBuild b) -> b.currentRecipe != null && b.currentRecipe.payReq().size != 0 ? b.currentRecipe.payReq() : Seq.with()));
        if(hasItems) consume(consPower = new ConsumeItemDynamic((UniversalBuild b) -> b.currentRecipe != null && b.currentRecipe.itemReqArray() != null ? b.currentRecipe.itemReqArray() : ItemStack.empty));
        if(consumesPower) consume(consPower = new ConsumePowerDynamic((Building b) -> ((UniversalBuild) b).currentRecipe != null && ((UniversalBuild) b).currentRecipe.powerReq() > -1 ? ((UniversalBuild) b).currentRecipe.powerReq : 0f));
        if(hasLiquidIn) consume(consLiquid = new ConsumeLiquidsDynamic((UniversalBuild b) -> b.currentRecipe != null && b.currentRecipe.liqReqArray() != null ? b.currentRecipe.liqReqArray() : LiquidStack.empty));

        //consumeBuilder.each(c -> c.multiplier = b -> ((UniversalBuild) b).currentRecipe != null && ((UniversalBuild) b).currentRecipe.isUnit ? state.rules.unitCost(b.team) : 1);
    }

    public void initRecipes(Seq<Recipe> recipes){
        for(int i = 0; i < recipes.size; i++){
            recipes.get(i).index = i;
            recipes.get(i).init(this);
        }
    }

    @Override
    public void load(){
        super.load();

        drawerTop.load(this);
        drawerBottom.load(this);
        if(drawerRecipeDefault != null) drawerRecipeDefault.load(this);
        if(recipes != null) recipes.each(recipe -> recipe.load(this));
    }

    @Override
    public void setBars(){
        super.setBars();
        removeBar("liquid");

        addBar("progress", (UniversalBuild entity) -> new Bar("bar.progress", Pal.ammo, () -> entity.progress));
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.output, table -> {
            table.row();
            recipes.each(recipe -> {
                if(recipe.index % 2 == 0) table.row();
                //table.add(recipe.recipeTable(true, recipe)).top().growX();
            });
        });
    }

    public class UniversalBuild extends PayloadBlockBuild<Payload>{
        public Recipe currentRecipe = recipes.get(0);
        public int slot = 0;

        public PayloadSeq mmmDelish = new PayloadSeq();
        public Seq<Payload> payQueue = new Seq<>(4);

        public float progress, warmup, totalProgress, spawnTime;
        public boolean outputting;
        public @Nullable Vec2 commandPos;

        public void outputPayQueue(){
            if(payQueue.isEmpty()) return;

            if(!outputting){
                outputting = true;
            }

            payVector.setZero();
            payload = payQueue.pop();
        }

        public Payload createPayload(UnlockableContent content){
            if(content instanceof UnitType e){
                Unit diarrhea = e.create(team);
                if(commandPos != null && diarrhea.isCommandable()){
                    diarrhea.command().commandPosition(commandPos);
                }
                diarrhea.rotation = rotdeg();
                var unit = new UnitPayload(diarrhea);
                Events.fire(new EventType.UnitCreateEvent(unit.unit, this));

                return unit;
            }else{
                return new BuildPayload((Block) content, team);
            }
        }

        @Override
        public void moveOutPayload(){
            if(payload == null) return;

            if(payload instanceof UnitPayload && currentRecipe != null && payload.dump())
                payload = null;

            updatePayload();

            Vec2 dest = Tmp.v1.trns(rotdeg(), size * tilesize / 2f);

            payRotation = Angles.moveToward(payRotation, rotdeg(), payloadRotateSpeed * delta());
            payVector.approach(dest, payloadSpeed * delta());

            Building front = front();
            boolean canDump = front == null || !front.tile().solid();
            boolean canMove = front != null && (front.block.outputsPayload || front.block.acceptsPayload);

            if(canDump && !canMove){
                pushOutput(payload, 1f - (payVector.dst(dest) / (size * tilesize / 2f)));
            }

            if(payVector.within(dest, 0.001f)){
                payVector.clamp(-size * tilesize / 2f, -size * tilesize / 2f, size * tilesize / 2f, size * tilesize / 2f);

                if(canMove){
                    if(movePayload(payload)){
                        payload = null;
                        outputting = false; //the only changes
                    }
                }else if(canDump){
                    dumpPayload();
                    outputting = false;
                }
            }
        }

        @Override
        public void dumpPayload(){
            //translate payload forward slightly
            float tx = Angles.trnsx(payload.rotation(), 0.1f), ty = Angles.trnsy(payload.rotation(), 0.1f);
            payload.set(payload.x() + tx, payload.y() + ty, payload.rotation());

            if(payload.dump() && payload instanceof UnitPayload){
                payload = null;
                Call.unitBlockSpawn(tile);
            }else{
                payload.set(payload.x() - tx, payload.y() - ty, payload.rotation());
            }
        }

        @Override
        public void updateTile(){
            if(currentRecipe != null) currentRecipe.update(this);

            totalProgress += warmup * Time.delta;

            Unit unit = UnitEntity.create();
            UnitType spawnType = UnitTypes.renale;

            int spawnAmount = 0;
            int countSelf = 0, countSpawned = 0;

            for(Unit u : Groups.unit){
                if(u.type == unit.type){
                    countSelf++;
                }else if(u.type == spawnType){
                    countSpawned++;
                }
            }

            for(Unit u : Groups.unit){
                if(u.type == spawnType && countSpawned > spawnAmount){
                    u.kill();
                }
            }
        }

        @Override
        public void buildConfiguration(Table table){
            Seq<Recipe> bRecipes = Seq.with(recipes()).filter(r -> {
                if(r.payOut().size != 0){
                    for(int i = 0; i < r.payOut().size; i++){
                        if(r.payOut().get(i).item instanceof Block b) return !state.rules.isBanned(b);
                        if(r.payOut().get(i).item instanceof UnitType u) return !u.isBanned();
                    }
                }
                return true;
            });

            if(bRecipes.any()){
                ItemSelection.buildTable(UniversalCrafter.this, table, bRecipes, () -> currentRecipe, recipe -> configure(recipes().indexOf(rec -> rec == recipe)), selectionRows, selectionColumns);
            }else{
                table.table(Styles.black3, t -> t.add("@none").color(Color.lightGray));
            }
        }


        @Override
        public void pickedUp(){
            warmup = 0f;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(progress);
            write.i(slot);
            write.i(currentRecipe.index);
            mmmDelish.write(write);
            TypeIO.writeVecNullable(write, commandPos);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            progress = read.f();
            slot = read.i();
            currentRecipe = recipes().get(read.i());
            mmmDelish.read(read);
            commandPos = TypeIO.readVecNullable(read);
        }



//visuals
        @Override
        public void draw(){
            drawerBottom.draw(this);

            if(vanillaIO){
                for(int i = 0; i < 4; i++){
                    if(blends(i) && i != rotation){
                        Draw.rect(inRegion, x, y, (i * 90) - 180);
                    }
                }
            }

            Draw.z(Layer.blockOver);
            if(payload != null){
                updatePayload();
                payload.draw();
            }

            if(currentRecipe != null && currentRecipe.drawer != null) currentRecipe.drawer.draw(this);
            else if(drawerRecipeDefault != null) drawerRecipeDefault.draw(this);
            Draw.z(37f);
            drawerTop.draw(this);
        }

        @Override
        public void display(Table table){
            super.display(table);
            if(team != player.team()) return;

            table.row();
            table.table(t -> {
                t.image(currentRecipe.fullIcon).size(iconMed).scaling(Scaling.fit).padBottom(-4f).padRight(2f).padTop(2f);
                t.label(() -> currentRecipe.name).color(Color.lightGray);
            });
        }



//everything that does nothing except return a value in 1ish line
        public Seq<Recipe> recipes(){
            return recipes;
        }

        public UniversalCrafter crafter(){
            return (UniversalCrafter)block;
        }

        public int timerDump(){
            return timerDump;
        }

        public float dumpTime(){
            return dumpTime;
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
            return currentRecipe != null && payQueue.isEmpty() && !outputting && currentRecipe.acceptPayload(this, payload);
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            return currentRecipe != null && currentRecipe.acceptItem(this, source, item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return currentRecipe != null && currentRecipe.acceptLiquid(this, source, liquid);
        }

        @Override
        public boolean shouldConsume(){
            return enabled && !outputting && currentRecipe.shouldConsume(this);
        }

        @Override
        public float getPowerProduction(){
            return currentRecipe != null ? currentRecipe.powerOut() * efficiency : 0f;
        }

        @Override
        public int getMaximumAccepted(Item item){
            return capacities[item.id];
        }

        @Override
        public PayloadSeq getPayloads(){
            return mmmDelish;
        }

        @Override
        public float warmup(){
            return warmup;
        }

        public float warmupTarget(){
            return 1f;
        }

        @Override
        public float totalProgress(){
            return totalProgress;
        }

        @Override
        public float progress(){
            return Mathf.clamp(progress);
        }

        @Override
        public Vec2 getCommandPosition(){
            return commandPos;
        }

        @Override
        public void onCommand(Vec2 target){
            commandPos = target;
        }
    }
}