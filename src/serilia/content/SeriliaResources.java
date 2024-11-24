package serilia.content;

import arc.graphics.Color;
import mindustry.type.Item;
import mindustry.type.Liquid;

public class SeriliaResources {
    public static Item
        debris,
    //Andarvos
        iridium;
    //Ahkar
    public static Liquid
        methane, chlorine, plantMatter, biofuel;

    public static void load() {
        //Items---------------------------------------------------------------------------------------------------------
        debris = new Item("debris", Color.valueOf("595365")) {{
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
    }
}
