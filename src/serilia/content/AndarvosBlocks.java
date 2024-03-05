package serilia.content;

import mindustry.content.UnitTypes;
import mindustry.world.Block;
import serilia.world.blocks.DrawCore;

import static mindustry.type.Category.*;
import static mindustry.type.ItemStack.with;

public class AndarvosBlocks {
    public static Block
    //effect
    coreSprout, coreBurgeon, coreGreenhouse;
    public static void load() {
        //effect
        coreSprout = new DrawCore("core-sprout"){{
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
        }};
        coreBurgeon = new DrawCore("core-burgeon"){{
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
        }};
    }
}
