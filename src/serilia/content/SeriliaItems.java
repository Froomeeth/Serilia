package serilia.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class SeriliaItems {
    public static Item
        debris,
    //Andarvos
        iridium;
    //Ahkar

    public static void load() {
        debris = new Item("debris", Color.valueOf("595365")) {{
            hardness = 3;
            cost = 1f;
            alwaysUnlocked = true;
        }};
        iridium = new Item("iridium", Color.valueOf("656e83")) {{
            hardness = 3;
            cost = 1.2f;
            alwaysUnlocked = true;
        }};
    }
}
