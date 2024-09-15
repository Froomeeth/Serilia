package serilia.content;

import arc.graphics.Color;
import mindustry.content.UnitTypes;
import mindustry.gen.Sounds;
import mindustry.world.Block;
import mindustry.world.blocks.power.BeamNode;
import mindustry.world.blocks.production.BeamDrill;
import serilia.world.blocks.GeneratorCore;

import static mindustry.type.Category.*;
import static mindustry.type.ItemStack.with;

public class AndarvosBlocks {
    public static Block
    //turrets
    //production
    //transport
    //fluids
    //power
    reinforcedNode,
    //defense
    //crafting
    //units
    //effect
    coreSprout, coreBurgeon;
    public static void load() {
        //power
        reinforcedNode = new BeamNode("reinforced-node"){{
            requirements(power, with());
            health = 100;
            size = 1;

            consumePowerBuffered(500.0F);
            range = 10;
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
            details = "cucumber unit seelction";
            requirements(effect, with());
            alwaysUnlocked = true;

            scaledHealth = 250;
            armor = 1f;
            size = 5;
            itemCapacity = 3000;

            isFirstTier = false;
            unitType = UnitTypes.incite;
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
