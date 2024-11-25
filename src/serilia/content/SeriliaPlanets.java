package serilia.content;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.Team;
import mindustry.graphics.Pal;
import mindustry.graphics.g3d.*;
import mindustry.maps.planet.SerpuloPlanetGenerator;
import mindustry.type.Planet;
import mindustry.world.meta.Env;

public class SeriliaPlanets{
    public static Planet serilia, andarvos, ring1, ahkar;

    public static void load(){
        serilia = new Planet("serilia", null, 4f){{
            bloom = true;
            accessible = false;

            meshLoader = () -> new SunMesh(
                    this, 4,
                    5, 0.3, 1.7, 1.2, 1,
                    1.1f,
                    Color.valueOf("ff7a38"),
                    Color.valueOf("ff9638"),
                    Color.valueOf("ffc64c"),
                    Color.valueOf("ffc64c"),
                    Color.valueOf("ffe371"),
                    Color.valueOf("f4ee8e")
            );
        }};


        andarvos = new Planet("andarvos", serilia, 1f, 2){{
            iconColor = Color.valueOf("ff9266");

            //rules
            alwaysUnlocked = true;
            startSector = 0;
            clearSectorOnLose = true;
            defaultCore = AndarvosBlocks.coreSprout;
            unlockedOnLand.add(AndarvosBlocks.coreSprout);
            itemWhitelist.add(SeriliaResources.andarvos);
            ruleSetter = r -> {
                r.waveTeam = Team.crux;
                r.placeRangeCheck = false;
                r.showSpawns = false;
            };

            //env
            //defaultEnv = Env.scorching | Env.terrestrial;
            lightSrcTo = 0.5f;
            lightDstFrom = 0.2f;

            //system view
            orbitSpacing = 2f;
            totalRadius += 2.6f;

            landCloudColor = Color.valueOf("ed6542");
            atmosphereColor = Color.valueOf("f07218");
            atmosphereColor = Color.valueOf("3c1b8f");
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;

            generator = new SerpuloPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 6);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 11, 0.15f, 0.13f, 5, new Color().set(Pal.spore).mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                    new HexSkyMesh(this, 1, 0.6f, 0.16f, 5, Color.white.cpy().lerp(Pal.spore, 0.55f).a(0.75f), 2, 0.45f, 1f, 0.41f)
            );
        }};
    }
}
