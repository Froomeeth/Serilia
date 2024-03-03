package serilia.content;

import mindustry.content.UnitTypes;
import mindustry.entities.Units;
import mindustry.world.Block;
import serilia.world.blocks.DrawCore;

import static mindustry.type.Category.*;
import static mindustry.type.ItemStack.with;
import static mindustry.world.meta.BuildVisibility.*;

public class AndarvosBlocks {
    public static Block
    coreSprout;
    public static void load() {
        //effect
        coreSprout = new DrawCore("core-sprout"){{
            requirements(logic, sandboxOnly, with());
            alwaysUnlocked = true;

            scaledHealth = 220;
            armor = 1f;
            size = 4;
            itemCapacity = 1500;

            isFirstTier = true;
            unitType = UnitTypes.evoke;
            thrusterLength = 34/4f;

            unitCapModifier = 5;
        }};

    }
}
