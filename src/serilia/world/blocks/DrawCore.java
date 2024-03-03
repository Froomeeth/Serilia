package serilia.world.blocks;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.world.blocks.storage.CoreBlock;

public class DrawCore extends CoreBlock{
    public float powerProduction = 0;
    public Color glowColor = Color.red;
    public TextureRegion glow;

    public DrawCore(String name){
        super(name);
    }

    @Override
    public void load(){
        super.load();

        glow = Core.atlas.find(name + "-glow");
    }

    public class NullCoreBuild extends CoreBuild{
        @Override
        public float getPowerProduction(){
            return powerProduction;
        }
        @Override
        public void draw(){
            super.draw();
            if(!glow.found()) return;

            //set color and blend mode
            Draw.blend(Blending.additive);
            Draw.color(glowColor);

            //draw sprite
            Draw.rect(glow, x, y);

            //reset both (((very important)))
            Draw.blend();
            Draw.color();
        }
    }
}