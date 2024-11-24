package serilia;

import arc.math.Mathf;
import mindustry.Vars;
import mindustry.mod.*;
import serilia.content.*;
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
        SeriliaResources.load();
        SeriliaEnvBlocks.load();
        AndarvosUnitType.load();
        EnemyUnits.load();
        AndarvosBlocks.load();
        OrbitBlocks.load();

        Vars.mods.getMod("serilia").meta.subtitle = sploosh[Mathf.random(sploosh.length - 1)];
    }
}
