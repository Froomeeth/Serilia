package serilia.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;

public class SeriliaEnvBlocks {
    public static Block
    //ores
    oreIridium;
    public static void load() {
        oreIridium = new OreBlock(SeriliaItems.iridium) {{
            oreThreshold = 0.81f;
            oreScale = 23.47619f;
            variants = 4;
        }};
    }
}
