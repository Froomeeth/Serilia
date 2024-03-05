package serilia;

import arc.math.Mathf;
import mindustry.Vars;
import mindustry.mod.*;
import serilia.content.AndarvosBlocks;
import serilia.gen.*;

public class SeriliaMain extends Mod{
    public static String[] sploosh = {
            "Do 15% of a backflip!",
            "why is it always thinking",
            "been"
    };

    @Override
    public void loadContent(){
        EntityRegistry.register();
        AndarvosBlocks.load();

        Vars.mods.getMod("serilia").meta.subtitle = sploosh[Mathf.random(sploosh.length - 1)];
    }
}
