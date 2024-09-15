package serilia.content;

import arc.graphics.Color;
import mindustry.entities.bullet.MissileBulletType;
import mindustry.gen.Sounds;
import mindustry.gen.UnitEntity;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class AndarvosUnitType {
    public static UnitType
        //Core units
            scion, graft
            ;
    public static void load() {
        scion = new UnitType("scion") {
            {
                isEnemy = false;
                constructor = UnitEntity::create;
                groundLayer = Layer.flyingUnit;

                health = 270f;
                armor = 3f;
                hitSize = 13f;

                flying = true;
                speed = 4.3f;
                rotateSpeed = 10f;
                drag = 0.09f;
                accel = 0.14f;

                itemCapacity = 30;
                buildSpeed = 3;
                mineTier = 1;
                mineSpeed = 8.5f;

                engineSize = 3;
                trailLength = 30;
                weapons.add(new Weapon() {{
                    x = 3f;
                    y = 0f;
                    mirror = true;

                    rotate = false;
                    shootCone = 5;
                    inaccuracy = 6;
                    reload = 15f;

                    shootSound = Sounds.missile;

                    bullet = new MissileBulletType() {{
                        sprite = "missile";
                        width = 6f;
                        height = 10f;

                        speed = 2.6f;
                        lifetime = 50f;
                        damage = 23;

                        trailWidth = 2;
                        trailLength = 16;

                        homingDelay = 20;
                        homingRange = 60;
                        buildingDamageMultiplier = 0;
                    }};
                }});
            }};
        graft = new UnitType("graft") {
            {
                isEnemy = false;
                constructor = UnitEntity::create;
                groundLayer = Layer.flyingUnit;

                health = 270f;
                armor = 3f;
                hitSize = 17f;

                flying = true;
                speed = 10f;
                rotateSpeed = 5f;
                drag = 0.03f;
                accel = 0.05f;
                omniMovement = false;
                faceTarget = false;

                itemCapacity = 30;
                buildSpeed = 3;
                mineTier = 1;
                mineSpeed = 8.5f;

                engineSize = 3.4f;
                engineOffset = 14;
                setEnginesMirror(new UnitType.UnitEngine[]{new UnitType.UnitEngine(12.25F, -9.25F, 3F, 315.0F)});
                trailLength = 30;
                outlineColor = Color.valueOf("313236");
                weapons.add(new Weapon() {{
                    x = 3f;
                    y = 0f;
                    mirror = true;

                    rotate = true;
                    shootCone = 5;
                    inaccuracy = 6;
                    reload = 15f;

                    shootSound = Sounds.missile;

                    bullet = new MissileBulletType() {{
                        sprite = "missile";
                        width = 6f;
                        height = 10f;

                        speed = 2.6f;
                        lifetime = 50f;
                        damage = 23;

                        trailWidth = 2;
                        trailLength = 16;

                        homingDelay = 20;
                        homingRange = 60;
                        buildingDamageMultiplier = 0;
                    }};
                }});
            }};
        }
    }
