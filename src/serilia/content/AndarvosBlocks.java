package serilia.content;

import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.BurstDrill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.production.Pump;
import mindustry.world.draw.*;
import serilia.world.blocks.GeneratorCore;
import serilia.world.blocks.transport.DuctNode;
import serilia.world.blocks.transport.HeavyDuct;

import static mindustry.type.Category.*;
import static mindustry.type.ItemStack.with;

public class AndarvosBlocks {
    public static Block
    //turrets
    //production
            combustionDrill,
    //distribution
    heavyDuct, ductNode,
    //fluids
    thermalPump,
    //power
    //defense
    //crafting
    castingBasin,
    //units
    //effect
    coreSprout, coreBurgeon;
    //misc
    public static void load() {
        //production
        combustionDrill = new BurstDrill("combustion-drill"){{
            scaledHealth = 120;
            size = 2;
            buildCostMultiplier = 3;
            requirements(Category.production, ItemStack.with(SeriliaResources.remnants, 20));

            liquidCapacity = 10;
            itemCapacity = 20;

            hasPower = false;
            consumeLiquids(LiquidStack.with(SeriliaResources.methane, 5/60F));

            tier = 1;
            drillTime = 500;

            drillEffect = Fx.blastExplosion;
            shake = 0.5f;
            arrowOffset = 0;
            arrowSpacing = 0;
            arrows = 1;
            }};
        //distribution
        heavyDuct = new HeavyDuct("heavy-duct"){{
            requirements(distribution, with(SeriliaResources.remnants, 1));
            health = 100;
            size = 1;

            armored = true;
        }};
        ductNode = new DuctNode("duct-node"){{
            requirements(distribution, with(SeriliaResources.remnants, 3));
            health = 100;
            size = 1;
        }};
        //fluids
        thermalPump = new Pump("thermal-pump"){{
            requirements(liquid, with(SeriliaResources.remnants, 50));
            health = 360;
            size = 2;
            liquidCapacity = 100;
            pumpAmount = 12.5f/60f;

            squareSprite = false;
        }};
        //power
        //Crafting------------------------------------------------------------------------------------------------------
        castingBasin = new GenericCrafter("casting-basin"){{
            scaledHealth = 120;
            size = 3;

            buildCostMultiplier = 1.3f;
            requirements(crafting, with(SeriliaResources.remnants, 70));

            liquidCapacity = 50;
            itemCapacity = 20;

            hasPower = false;
            consumeLiquids(LiquidStack.with(new Object[]{Liquids.slag, 25/60F}));
            outputItem = new ItemStack(Items.graphite, 10);
            craftTime = 250;

            drawer = new DrawMulti(new DrawBlock[]{new DrawRegion("-bottom"), new DrawLiquidTile(), new DrawDefault()});
        }};
        //effect
        coreSprout = new GeneratorCore("core-sprout"){{
            requirements(effect, with());
            alwaysUnlocked = true;

            scaledHealth = 225;
            armor = 1f;
            size = 4;
            itemCapacity = 1500;

            isFirstTier = true;
            unitType = AndarvosUnitType.scion;
            thrusterLength = 34/4f;

            unitCapModifier = 5;
            ambientSound = Sounds.electricHum;
            ambientSoundVolume = 0.06f;

            outputsPower = true;
            consumesPower = false;
            powerProduction = 500/60f;
        }};
        coreBurgeon = new GeneratorCore("core-burgeon"){{
            details = "shoot where want, shit out unit";
            requirements(effect, with());
            alwaysUnlocked = true;

            scaledHealth = 250;
            armor = 1f;
            size = 5;
            itemCapacity = 3000;

            isFirstTier = false;
            unitType = AndarvosUnitType.graft;
            thrusterLength = 34/4f;

            unitCapModifier = 15;
            ambientSound = Sounds.electricHum;
            ambientSoundVolume = 0.08f;

            outputsPower = true;
            consumesPower = false;
            powerProduction = 1000/60f;
        }};
        //misc
    }
}
