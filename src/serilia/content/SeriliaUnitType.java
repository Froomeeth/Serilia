package serilia.content;

import arc.graphics.Color;
import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Sounds;
import mindustry.type.UnitType;
import mindustry.type.weapons.PointDefenseWeapon;

public class SeriliaUnitType extends UnitType {
    public SeriliaUnitType(String name){
        super(name);
    }

    public void trail(float tx, float ty, float width, int length, Color tcolor, boolean tmirror){
        weapons.add(new PointDefenseWeapon(){{
            mirror = tmirror;
            x=tx;
            y=-ty;
            bullet = new BulletType(){{
                trailWidth = width;
                trailLength = length;
                trailColor = tcolor;

                keepVelocity = false;
                speed = 0;
                collides = false;
                hittable = false;
                absorbable = false;
                shootEffect = Fx.none;
                smokeEffect = Fx.none;
                hitEffect = Fx.none;
                despawnEffect = Fx.none;
            }};
            alwaysShooting = true;
            alwaysContinuous = true;
            alternate = false;
            shootSound = Sounds.none;
            reload = 1;
        }});
    }
}