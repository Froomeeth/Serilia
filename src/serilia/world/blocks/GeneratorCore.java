package serilia.world.blocks;

import arc.Core;
import arc.func.Func;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Strings;
import arc.util.Time;
import mindustry.core.UI;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.storage.CoreBlock;

public class GeneratorCore extends CoreBlock{
    public float powerProduction = 0;
    public Color[] glowColors = {Color.valueOf("00000000"), Color.coral, Color.valueOf("ff6161"), Color.pink, Color.acid, Color.sky};
    public TextureRegion glow;
    public float glowMag = 0.5f, glowScl = 10f;

    public GeneratorCore(String name){
        super(name);
        hasPower = true;
        outputsPower = true;
    }

    @Override
    public void load(){
        super.load();
        glow = Core.atlas.find(name + "-glow");
        uiIcon = fullIcon = editorIcon = Core.atlas.find(name + "-full");
    }
    @Override
    public void setBars(){
        super.setBars();
        addBar("poweroutput", (GeneratorCoreBuild entity) -> {
            return new Bar(() -> {
                return Core.bundle.format("bar.poweroutput", Strings.fixed(powerProduction * 60 + 0.0001f, 1));
            }, () -> {
                return Pal.powerBar;
            }, () -> {
                return 1f;
            });
        });
        addBar("power", makePowerBalance());
    }

    public static Func<Building, Bar> makePowerBalance(){
        return entity -> new Bar(() ->
                Core.bundle.format("bar.powerbalance",
                        ((entity.power.graph.getPowerBalance() >= 0 ? "+" : "") + UI.formatAmount((long)(entity.power.graph.getPowerBalance() * 60 + 0.0001f)))),
                () -> Pal.powerBar,
                () -> Mathf.clamp(entity.power.graph.getLastPowerProduced() / entity.power.graph.getLastPowerNeeded())
        );
    }

    public class GeneratorCoreBuild extends CoreBuild{
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