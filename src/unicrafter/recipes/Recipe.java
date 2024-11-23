package unicrafter.recipes;

import arc.Core;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Structs;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Building;
import mindustry.type.*;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.draw.DrawBlock;
import mindustry.world.meta.Stat;
import unicrafter.world.UniversalCrafter;
import unicrafter.world.UniversalCrafter.UniversalBuild;

/**Basic recipe with support for items, liquids and payloads. Can be researched.
 * Use ChanceRecipe for separators/other chances and todo ContainerRecipe for adding container IO for certain stacks.*/
public class Recipe extends UnlockableContent{

    //general
    /** Draws between top and bottom drawer of the block. */
    public DrawBlock drawer;
    public int reqItemCapMul = 2, outItemCapMul = 2;
    public boolean loseProgressOnIdle = false;
    public float progressLoseSpeed = 0.019f;

    /** iconContent copies the content's icon for this recipe. Use the other constructor for a custom one. */
    public Recipe(String name, UnlockableContent iconContent, float time){
        this(name, time);
        this.iconContent = iconContent;
    }

    public Recipe(String name, float time){
        super(name);
        localizedName = Core.bundle.get("recipe." + this.name + ".name", this.name);
        description = Core.bundle.getOrNull("recipe." + this.name + ".description");
        details = Core.bundle.getOrNull("recipe." + this.name + ".details");
        unlocked = Core.settings != null && Core.settings.getBool(this.name + "-unlocked", false);

        this.time = time;
    }

    /**
     * Conveniently sets the requirements at once, instead of needing you to type 5 fields manually.
     *
     * @return the array it performed the checks on, for super chaining.
     */
    public Object[] req(Object... items){
        for(int i = 0; i < items.length; i += 2){
            if(items[i] instanceof Item)
                itemReq.add(new ItemStack((Item) items[i], ((Number) items[i + 1]).intValue()));
            else if(items[i] instanceof Liquid)
                liqReq.add(new LiquidStack((Liquid) items[i], ((Number) items[i + 1]).floatValue()));
            else if(items[i] instanceof UnlockableContent)
                payReq.add(new PayloadStack((UnlockableContent) items[i], ((Number) items[i + 1]).intValue()));
            else if(items[i] instanceof String)
                if(items[i] == "power")
                    powerReq = ((Number) items[i + 1]).floatValue();
                else if(items[i] == "heat")
                    heatReq = ((Number) items[i + 1]).floatValue();
        }
        return items;
    }

    /**
     * Conveniently sets the outputs at once, instead of needing you to type 5 fields manually.
     *
     * @return the array it performed the checks on, for super chaining.
     */
    public Object[] out(Object... items){
        for(int i = 0; i < items.length; i += 2){
            if(items[i] instanceof Item)
                itemOut.add(new ItemStack((Item) items[i], ((Number) items[i + 1]).intValue()));
            else if(items[i] instanceof Liquid)
                liqOut.add(new LiquidStack((Liquid) items[i], ((Number) items[i + 1]).floatValue()));
            else if(items[i] instanceof UnlockableContent)
                payOut.add(new PayloadStack((UnlockableContent) items[i], ((Number) items[i + 1]).intValue()));
            else if(items[i] instanceof String)
                if(items[i] == "power") powerOut = ((Number) items[i + 1]).floatValue();
                else if(items[i] == "heat") heatOut = ((Number) items[i + 1]).floatValue();
        }
        return items;
    }

    /*--- Set automatically ---*/
    public int index;
    public float time;
    private UnlockableContent iconContent;
    public final Seq<ItemStack> itemReq = new Seq<>(ItemStack.class), itemOut = new Seq<>();
    public final Seq<LiquidStack> liqReq = new Seq<>(LiquidStack.class), liqOut = new Seq<>();
    public final Seq<PayloadStack> payReq = new Seq<>(), payOut = new Seq<>();
    public float powerReq = -555f, powerOut = -12f, heatReq = -42f, heatOut = -9999999999999999f;
    private ItemStack[] itemReqArray;
    private LiquidStack[] liqReqArray;

    @Override
    public ContentType getContentType(){
        return ContentType.loadout_UNUSED;
    }

    @Override
    public void loadIcon(){
        fullIcon = uiIcon = (iconContent != null ? iconContent.fullIcon : Core.atlas.find("recipe-" + name));
    }

    @Override
    public void setStats(){
        stats.add(Stat.output, table -> {
            table.row();
            //table.add(recipeTable(false, this));
        });
    }


    //logic
    public void update(UniversalBuild build){
        if(build.crafter().hasPayloads){

            if(acceptPayload(build) && build.moveInPayload() || build.crafter().instantInput){

                build.mmmDelish.add(build.payload.content());
                build.payload = null;
            }else{
                if(build.payload == null){
                    build.outputPayQueue();
                }else{
                    build.moveOutPayload();
                }
            }
        }

        if(build.efficiency > 0 && shouldConsume(build)){
            build.progress += build.getProgressIncrease(time);
            build.warmup = Mathf.approachDelta(build.warmup, build.warmupTarget(), build.crafter().warmupSpeed);
        }else{
            build.warmup = Mathf.approachDelta(build.warmup, 0f, build.crafter().warmupSpeed);
            if(loseProgressOnIdle) build.progress = Mathf.approachDelta(build.progress, 0f, progressLoseSpeed);
        }

        dumpOutputs(build);

        if(build.progress >= 1f){
            craft(build);
        }
    }

    public void craft(UniversalBuild build){
        build.consume();

        if(build.items != null && itemOut().size != 0) itemOut.each(output -> {
            for(int i = 0; i < output.amount; i++){
                build.offload(output.item);
            }
        });

        if(build.liquids != null && liqOut().size != 0) liqOut.each(output -> {
            for(int i = 0; i < output.amount; i++){
                build.handleLiquid(build, output.liquid, Math.min(output.amount, build.block.liquidCapacity - build.liquids.get(output.liquid)));
            }
        });

        if(payOut().size != 0){
            payOut.each(output -> {
                for(int i = 0; i < output.amount; i++){
                    build.payQueue.add(build.createPayload(output.item));
                }
            });

            if(build.crafter().instantFirstOutput){
                build.payVector.setZero();
                build.payload = build.payQueue.pop();
            }
        }

        //if(build.wasVisible) craftEffect.at(build);

        build.progress %= 1f;
    }

    public boolean shouldConsume(UniversalBuild build){
        if(itemOut().size != 0){
            for(int i = 0; i < itemOut().size; i++){
                if(build.items != null && build.items.get(itemOut().get(i).item) + itemOut().get(i).amount > build.block.itemCapacity){
                    return false;
                }
            }
        }

        if(liqOut().size != 0){
            boolean allFull = true;
            for(int i = 0; i < liqOut().size; i++){
                if(build.liquids != null && build.liquids.get(liqOut().get(i).liquid) + liqOut().get(i).amount > build.block.liquidCapacity){
                    return false;
                }
            }
        }
        return true;
    }

    public void dumpOutputs(UniversalBuild build){
        if(itemOut().size != 0 && build.timer(build.timerDump(), build.dumpTime() / build.timeScale())){
            itemOut().each(output -> build.dump(output.item));
        }

        if(liqOut().size != 0 && build.timer(build.timerDump(), build.dumpTime() / build.timeScale())){
            for(int i = 0; i < liqOut().size; i++){
                build.dumpLiquid(liqOut().get(i).liquid, 2f);
            }
        }
    }

    public boolean acceptPayload(UniversalBuild build){
        return build.payload != null && payReq().size != 0 && payReq().contains(b -> b.item == build.payload.content());
    }

    public boolean acceptPayload(UniversalBuild build, Payload pay){
        return build.payload == null && pay != null && payReq().size != 0 && payReq().contains(b ->
                b.item == pay.content() && build.mmmDelish.get(pay.content()) < Mathf.round(b.amount * build.team.rules().unitCostMultiplier));
    }

    public boolean acceptItem(UniversalBuild build, Building source, Item item){
        for(int i = 0; i < itemReqArray().length; i++){
            if(build.items.get(itemReqArray()[i].item) >= build.getMaximumAccepted(item) || !Structs.contains(itemReqArray(), stack -> stack.item == item)){
                return false;
            }
        }
        return true;
    }

    public boolean acceptLiquid(UniversalBuild build, Building source, Liquid liquid){
        for(int i = 0; i < liqReqArray().length; i++){
            if(!Structs.contains(liqReqArray(), stack -> stack.liquid == liquid)){
                return false;
            }
        }
        return true;
    }

    //everything else
    public void init(UniversalCrafter b){
        b.hasLiquids |= (b.hasLiquidIn |= liqReq.size != 0) || (b.outputsLiquid |= liqOut.size != 0);
        b.hasPayloads |= (b.acceptsPayload |= payReq.size != 0) || (b.outputsPayload |= payOut.size != 0);
        b.rotate |= b.outputsPayload; //doesn't set?
        b.consumesPower |= powerReq > -1f;
        b.outputsPower |= powerOut > -1f;

        if(itemReq.size != 0){
            itemReqArray = itemReq.toArray(ItemStack.class);
            for(ItemStack stack : itemReqArray){
                b.capacities[stack.item.id] = Math.max(b.capacities[stack.item.id], stack.amount * reqItemCapMul);
                b.itemCapacity = Math.max(b.itemCapacity, stack.amount * outItemCapMul);
            }
        }

        if(liqReq.size != 0){
            liqReqArray = liqReq.toArray(LiquidStack.class);
        }

        if(liqReq.size != 0){
            liqReq.each(req -> {
                b.liquidFilter[req.liquid.id] = true;
                b.liquidCapacity = Math.max(b.liquidCapacity, req.amount * 2);
            });
            liqOut.each(out -> {
                b.liquidFilter[out.liquid.id] = true;
                b.liquidCapacity = Math.max(b.liquidCapacity, out.amount * 2);
            });
        }
    }

    public void load(UniversalCrafter block){
        if(drawer != null) drawer.load(block);
    }

    public void configTo(UniversalBuild build){
        liqReq.each(bar -> build.block.addLiquidBar(bar.liquid)); //this is really stupid. I don't want to make dynamic bars.
        liqOut.each(bar -> build.block.addLiquidBar(bar.liquid));
    }

    public Seq<ItemStack> itemReq(){
        return itemReq;
    }

    public Seq<LiquidStack> liqReq(){
        return liqReq;
    }

    public Seq<PayloadStack> payReq(){
        return payReq;
    }

    public float powerReq(){
        return powerReq;
    }

    public Seq<ItemStack> itemOut(){
        return itemOut;
    }

    public Seq<LiquidStack> liqOut(){
        return liqOut;
    }

    public Seq<PayloadStack> payOut(){
        return payOut;
    }

    public float powerOut(){
        return powerOut;
    }

    //dynamic consumers need these. really wish they didn't.
    public ItemStack[] itemReqArray(){
        return itemReqArray;
    }

    public LiquidStack[] liqReqArray(){
        return liqReqArray;
    }
}
    //ui
//    public Table recipeTable(boolean icon, Recipe recipe){
//        Table t = new Table();
//
//        t.row(); t.table(Styles.grayPanel, display -> {
//            if(icon){
//                display.add(recipeTopTable(recipe)).pad(10f).padBottom(0f); display.row();
//            }
//
//            display.table(io -> { //encompasses input/output
//                io.table(Styles.black5, input -> input.add(contentListVertical(recipe.payReq, recipe.itemReq, recipe.liqReq, -20f, -20f, recipe.time, true, 1f)).pad(5f).grow()).pad(5f).grow(); //not adding directly to get an easy outline
//
//                io.add(recipeArrowTable(recipe)).padBottom(5f).padTop(5f);
//
//                io.table(out -> {
//                    if(recipe instanceof ChanceRecipe){
//                        out.add("100%").color(Color.white); out.row();
//                    }
//
//                    out.table(Styles.black5, output -> output.add(contentListVertical(recipe.payOut, recipe.itemOut, recipe.liqOut, recipe.powerOut, recipe.heatOut, recipe.time, true, 1f)).pad(5f).grow()).pad(5f).grow();
//
//                    if(recipe instanceof ChanceRecipe ch){
//                        ch.outs.each(chanceOut -> {
//                            out.row(); out.add(Mathf.round((chanceOut.rangeMax - chanceOut.rangeMin) * 100f) + "%").color(Pal.accent);
//                            out.row(); out.table(Styles.black5, output -> output.add(contentListVertical(chanceOut.payOutChance, chanceOut.itemOutChance, chanceOut.liqOutChance, -20f, -20f, recipe.time, true, chanceOut.rangeMax - chanceOut.rangeMin)).pad(5f).grow()).pad(5f).grow();
//                        });
//                    }
//                });
//            });
//        }).pad(5f).top();
//
//        return t;
//    }
//
//    public Table recipeTopTable(Recipe recipe){
//        Table t = new Table();
//
//        t.table(name -> {
//            t.add(recipe.localizedName);
//            t.button("?", Styles.flatBordert, () -> ui.content.show(recipe)).size(iconMed).padLeft(5f).visible(recipe::unlockedNow);
//        });
//        t.row(); t.image(recipe.uiIcon).size(48f).scaling(Scaling.fit);
//
//        return t;
//    }
//
//    public Table recipeArrowTable(Recipe recipe){
//        Table t = new Table();
//
//        if(recipe.powerReq > 0){
//            t.table(power -> {
//                power.add("[accent]" + Iconc.power + "[]");
//                power.row();
//                power.add(Strings.autoFixed(recipe.powerReq * 60f, 1));
//            }).pad(5f).bottom();
//            t.row();
//        }
//
//        t.image(Icon.play).pad(5f).padTop(10f).padBottom(10f).center();
//
//        return t;
//    }
//
//    public Table contentListVertical(Seq<PayloadStack> pay, Seq<ItemStack> item, Seq<LiquidStack> liq, float power, float heat, float time, boolean flip, float timeDiv){
//        Table t = new Table();
//
//        if(pay != null) pay.each(requirement -> {
//            if(flip) t.add(new ItemImage(requirement.item.uiIcon, requirement.amount)).left();
//
//            t.table(req -> {
//                req.add(Strings.autoFixed(requirement.amount / ((time / timeDiv) / 60f), 2) + "/s").pad(4f).color(Color.lightGray);
//            });
//
//            if(!flip) t.add(new ItemImage(requirement.item.uiIcon, requirement.amount)).right();
//            t.row();
//        });
//        if(item != null) item.each(requirement -> {
//            if(flip) t.add(new ItemImage(requirement.item.uiIcon, requirement.amount)).left();
//
//            t.table(req -> {
//                req.add(Strings.autoFixed(requirement.amount / ((time * timeDiv) / 60f), 2) + "/s").pad(4f).color(Color.lightGray);
//            });
//            if(!flip) t.add(new ItemImage(requirement.item.uiIcon, requirement.amount)).right();
//            t.row();
//        });
//        if(liq != null) liq.each(requirement -> {
//            if(flip) t.image(requirement.liquid.fullIcon).size(iconMed).left().scaling(Scaling.fit);
//
//            t.table(req -> {
//                req.add(Strings.autoFixed(requirement.amount * 60f, 2) + "/s").color(Color.lightGray).pad(4f);
//            });
//            if(!flip) t.image(requirement.liquid.fullIcon).size(iconMed).right().scaling(Scaling.fit);
//            t.row();
//        });
//        t.row();
//        if(power > 0f) t.add("[accent]" + Iconc.power + "[] " + Strings.autoFixed(power * 60f, 2)).pad(4f);
//        if(heat > 0f) t.add("[red]" + Iconc.waves + "[] " + Strings.autoFixed(heat, 2)).pad(4f);
//
//        return t;
//    }
//
//    public Table contentListHorizontal(Seq<PayloadStack> pay, Seq<ItemStack> item, Seq<LiquidStack> liq, float power, float heat, float time, float timeDiv){
//        Table t = new Table();
//
//        if(pay != null) pay.each(requirement -> t.table(req -> {
//            req.add(new ItemImage(requirement.item.uiIcon, requirement.amount));
//            req.row();
//            req.add(Strings.autoFixed(requirement.amount / ((time / timeDiv) / 60f), 2) + "/s").pad(4f).color(Color.lightGray);
//        }));
//        if(item != null) item.each(requirement -> t.table(req -> {
//            req.add(new ItemImage(requirement.item.uiIcon, requirement.amount));
//            req.row();
//            req.add(Strings.autoFixed(requirement.amount / ((time / timeDiv) / 60f), 2) + "/s").pad(4f).color(Color.lightGray);
//        }));
//        if(liq != null) liq.each(requirement -> t.table(req -> {
//            req.image(requirement.liquid.fullIcon).size(iconMed).scaling(Scaling.fit);
//            req.row();
//            req.add(Strings.autoFixed(requirement.amount * 60f, 2) + "/s").pad(4f).color(Color.lightGray);
//        }));
//        if(power > 0f || heat > 0f) t.table(req -> {
//            if(power > 0f) req.add("[accent]" + Iconc.power + "[] " + Strings.autoFixed(power * 60f, 2)).pad(4f);
//            req.row();
//            if(heat > 0f) req.add("[red]" + Iconc.waves + "[] " + Strings.autoFixed(heat, 2)).pad(4f);
//        });
//
//        return t;
//    }
//}
