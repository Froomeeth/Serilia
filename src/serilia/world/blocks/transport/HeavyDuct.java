package serilia.world.blocks.transport;

import arc.graphics.g2d.TextureRegion;
import mindustry.world.blocks.distribution.Duct;

public class HeavyDuct extends Duct {
    public TextureRegion[] regions;
    public HeavyDuct(String name){
        super(name);
    }
    @Override
    public void load(){
    }
    public class HeavyDuctBuild extends DuctBuild{

    }
}
