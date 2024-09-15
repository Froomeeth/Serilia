package serilia.content;

import arc.graphics.Color;
import mindustry.content.UnitTypes;
import mindustry.gen.Sounds;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.power.BeamNode;
import mindustry.world.blocks.production.BeamDrill;
import serilia.world.blocks.GeneratorCore;

import static mindustry.type.Category.*;
import static mindustry.type.ItemStack.with;
import static serilia.content.AndarvosUnitType.graft;

public class AndarvosBlocks {
    public static Block
    //turrets
    //production
    //distribution
    heavyDuct,
    //fluids
    //power
    reinforcedNode,
    //defense
    //crafting
    //units
    //effect
    coreSprout, coreBurgeon;
    public static void load() {
        //distribution
        heavyDuct = new Duct("heavy-duct"){{
            details = "straight line";
            requirements(distribution, with());
            health = 100;
            size = 1;

            armored = true;
        }};
        //power
        reinforcedNode = new BeamNode("reinforced-node"){{
            details = "just like factorio";
            requirements(power, with());
            health = 100;
            size = 1;

            consumePowerBuffered(500.0F);
            range = 10;
            outputsPower = true;
            consumesPower = false;
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
            unitType = UnitTypes.evoke;
            thrusterLength = 34/4f;

            unitCapModifier = 5;
            ambientSound = Sounds.electricHum;
            ambientSoundVolume = 0.06f;

            outputsPower = true;
            consumesPower = false;
            powerProduction = 400/60f;
        }};
        coreBurgeon = new GeneratorCore("core-burgeon"){{
            details = "cucumber unit selection";
            requirements(effect, with());
            alwaysUnlocked = true;

            scaledHealth = 250;
            armor = 1f;
            size = 5;
            itemCapacity = 3000;

            isFirstTier = false;
            unitType = graft;
            thrusterLength = 34/4f;

            unitCapModifier = 15;
            ambientSound = Sounds.electricHum;
            ambientSoundVolume = 0.08f;

            outputsPower = true;
            consumesPower = false;
            powerProduction = 1000/60f;
        }};
    }
}
