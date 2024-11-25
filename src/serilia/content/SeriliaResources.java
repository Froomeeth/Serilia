package serilia.content;

import arc.graphics.Color;
import mindustry.type.CellLiquid;
import mindustry.type.Item;
import mindustry.type.Liquid;

public class SeriliaResources {
    public static Item
        remnants,
    //Andarvos
        iridium;
    //Ahkar


    public static Item[] andarvos = {remnants, iridium};
    public static Item[] orbit = {};

    public static Liquid methane, chlorine, plantMatter, biofuel;

    public static void load() {
        //Items---------------------------------------------------------------------------------------------------------
        remnants = new Item("remnants", Color.valueOf("595365")) {{
            hardness = 1;
            cost = 1f;
            alwaysUnlocked = true;
        }};
        iridium = new Item("iridium", Color.valueOf("656e83")) {{
            hardness = 3;
            cost = 1.5f;
            alwaysUnlocked = true;
        }};
        //Liquids-------------------------------------------------------------------------------------------------------
        methane = new Liquid("methane", Color.valueOf("ea8878")) {{
                gas = true;
                explosiveness = 1;
                flammability = 3;
            }};
        plantMatter = new CellLiquid("plant-matter", Color.valueOf("3b4935")) {{
                heatCapacity = 0;
                temperature = 0.5f;
                viscosity = 0.9f;
                flammability = 0;
                capPuddles = false;
                spreadTarget = SeriliaResources.plantMatter;
                moveThroughBlocks = true;
                incinerable = true;
                blockReactive = false;
                canStayOn.addAll(new Liquid[]{SeriliaResources.plantMatter});
                colorFrom = Color.valueOf("3b4935");
                colorTo = Color.valueOf("55693c");
            }};
    }
}
