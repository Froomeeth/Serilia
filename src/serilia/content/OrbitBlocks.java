package serilia.content;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawFlame;
import mindustry.world.draw.DrawMulti;
import mindustry.world.meta.Env;
import serilia.world.blocks.transport.*;

import static mindustry.content.Items.*;
import static mindustry.type.Category.*;
import static mindustry.type.ItemStack.with;

public class OrbitBlocks{
    public static Block
            //early
            electricBore, electricFurnace,

            //in testing
            transporter, transporterIn;

    public static void load(){
        //region early
        Blocks.stone.itemDrop = sand; //TODO HORRID.

        electricBore = new Drill("electric-bore"){{
            requirements(production, with(scrap, 16, copper, 8));
            //envEnabled = Env.space;
            size = 2;

            hasPower = true;
            tier = 2;
            drillTime = 600;
            consumeLiquid(Liquids.water, 0.05f).boost();
            consumePower(0.50f);

            researchCost = with(scrap, 10);
        }};

        electricFurnace = new GenericCrafter("electric-furnace-2"){{
            requirements(crafting, with(scrap, 16, copper, 8));
            //envEnabled = Env.space;
            size = 2;

            hasPower = hasItems = true;
            outputItem = new ItemStack(metaglass, 1);
            craftTime = 30f;
            consumeItems(with(sand, 3, scrap, 1));
            consumePower(0.75f);


            craftEffect = Fx.smeltsmoke;
            drawer = new DrawMulti(new DrawDefault());
            ambientSound = Sounds.smelter;
            ambientSoundVolume = 0.07f;
        }};

        //endregion


        transporter = new PayloadBridgeOut("transporter"){{
            requirements(distribution, with());
            size = 5;
        }};

        transporterIn = new PayloadBridgeIn("transporter-in"){{
            requirements(distribution, with());
            size = 5;
        }};
    }
}
