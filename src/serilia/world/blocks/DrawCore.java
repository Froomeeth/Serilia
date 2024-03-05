package serilia.world.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.world.blocks.storage.CoreBlock;

public class DrawCore extends CoreBlock{
    public float powerProduction = 0;
    public Color[] glowColors = {Color.valueOf("00000000"), Color.coral, Color.valueOf("ff6161"), Color.pink, Color.acid, Color.sky};
    public TextureRegion glow;
    public float glowMag = 0.5f, glowScl = 10f;

    public DrawCore(String name){
        super(name);
    }

    @Override
    public void load(){
        super.load();
        glow = Core.atlas.find(name + "-glow");
    }

    public class DrawCoreBuild extends CoreBuild{
        @Override
        public float getPowerProduction(){
            return powerProduction;
        }
        @Override
        public void draw(){
            super.draw();
            Drawf.additive(glow, team.id < 6 ? glowColors[team.id] : glowColors[1], 1f - glowMag + Mathf.absin(Time.time, glowScl, glowMag), x, y, 0f, Layer.blockAdditive);
        }
    }
}