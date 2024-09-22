package serilia.content;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.gen.Sounds;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.power.BeamNode;
import mindustry.world.blocks.production.GenericCrafter;
import serilia.world.blocks.GeneratorCore;
import serilia.world.blocks.transport.DuctNode;
import serilia.world.blocks.transport.HeavyDuct;

import static mindustry.type.Category.*;
import static mindustry.type.ItemStack.with;

public class AndarvosBlocks {
    public static Block
    //turrets
    //production
    //distribution
    heavyDuct, ductNode,
    //fluids
    //power
    reinforcedNode,
    //defense
    //crafting
    //units
    //effect
    //misc
    fireflyNest,
    coreSprout, coreBurgeon;
    public static void load() {
        //distribution
        heavyDuct = new HeavyDuct("heavy-duct"){{
            requirements(distribution, with());
            health = 100;
            size = 1;

            armored = true;
        }};
        ductNode = new DuctNode("duct-node"){{
            requirements(distribution, with());
            health = 100;
            size = 1;
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
        fireflyNest = new GenericCrafter("firefly-nest"){{
            alwaysUnlocked = true;
            requirements(effect, with());
            craftTime = 1;
            craftEffect = new MultiEffect(
                    new ParticleEffect(){{
                        sizeFrom = 3;
                        sizeTo = 0;
                        length = 60;
                        colorFrom = Color.valueOf("75eb4d");
                        colorTo = Color.valueOf("75eb4d");
                        cone = 360f;
                        particles = 1;
                    }});
        }};
    }
}
