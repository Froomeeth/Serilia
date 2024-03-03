package serilia;

import mindustry.mod.*;
import serilia.content.AndarvosBlocks;
import serilia.gen.*;

public class Serilia extends Mod{
    @Override
    public void loadContent(){
        EntityRegistry.register();
        AndarvosBlocks.load();
    }
}