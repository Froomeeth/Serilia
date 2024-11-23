package indev;

import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import unicrafter.recipes.Recipe;
import unicrafter.world.UniversalCrafter;

/**Where the mentally unstable (buggy) content goes, so it's easy to turn it off using settings*/
public class RnDContent{
    public static Block unicraft;

    public static void load(){
        unicraft = new UniversalCrafter("unidebug"){{
            requirements(Category.distribution, ItemStack.with());
            size = 5;

            recipes.add(
                new Recipe("FUCK", 50f){{
                    req(Items.sand, 10);
                    out(Items.silicon, 8);
                }}
            );
        }};
    }
}
