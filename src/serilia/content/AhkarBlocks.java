package serilia.content;

import mindustry.content.UnitTypes;
import mindustry.gen.Sounds;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.power.BeamNode;
import serilia.world.blocks.GeneratorCore;
import serilia.world.blocks.transport.PayloadBridgeIn;
import serilia.world.blocks.transport.PayloadBridgeOut;

import static mindustry.type.Category.*;
import static mindustry.type.Category.effect;
import static mindustry.type.ItemStack.with;
import static serilia.content.AndarvosUnitType.graft;

public class AhkarBlocks{
    public static Block
    transporter, transporterIn;

    public static void load() {
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
