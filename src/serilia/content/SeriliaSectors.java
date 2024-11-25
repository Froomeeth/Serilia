package serilia.content;

import mindustry.type.SectorPreset;

import static serilia.content.SeriliaPlanets.*;

public class SeriliaSectors{
    public static SectorPreset reprise;

    public static void load(){
        reprise = new SectorPreset("reprise", andarvos, 0){{
            alwaysUnlocked = true;
            captureWave = 5;
            difficulty = 1;
            overrideLaunchDefaults = true;
            noLighting = true;
            startWaveTimeMultiplier = 3f;
        }};
    }
}
