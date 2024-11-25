package serilia.content;

import mindustry.content.Blocks;
import mindustry.content.StatusEffects;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;

public class SeriliaEnvBlocks {
    public static Block
    //ores
    oreDebris, oreIridium,
    //andarvos env tiles
    moss, mossWall,
    smoothBasalt;
    public static void load() {
        oreIridium = new OreBlock(SeriliaResources.iridium) {{
            variants = 4;
        }};
        oreDebris = new OreBlock(SeriliaResources.remnants) {{
            variants = 3;
        }};
        moss = new Floor("moss") {{
                variants = 6;
                status = StatusEffects.shocked;
                statusDuration = 5;
            }};
        mossWall = new StaticWall("moss-wall") {{
                variants = 4;
                SeriliaEnvBlocks.moss.asFloor().wall = this;
            }};
        smoothBasalt = new Floor("smooth-basalt") {{
            variants = 6;
            blendGroup = Blocks.basalt;
        }};
    }
}
