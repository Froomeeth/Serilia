package serilia;

import arc.math.Mathf;
import mindustry.Vars;
import mindustry.mod.*;
import serilia.content.AndarvosBlocks;
import serilia.content.AndarvosUnitType;
import serilia.gen.*;

public class SeriliaMain extends Mod{
    public static String[] sploosh = {
            "Do 15% of a backflip!",
            "why is it always thinking",
            "been",
            ":)",
            ":D",
            "git gud",
            "may be gluten free",
            "pickle part",
            "cordoba bossfight",
            "beeblebum",
            "THE GODS ARE AFRAID"
    };

    @Override
    public void loadContent(){
        EntityRegistry.register();
        AndarvosBlocks.load();
        AndarvosUnitType.load();

        Vars.mods.getMod("serilia").meta.subtitle = sploosh[Mathf.random(sploosh.length - 1)];
    }
}
