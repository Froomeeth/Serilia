package serilia.content;

import mindustry.gen.LegsUnit;
import mindustry.type.UnitType;

public class EnemyUnits {
    public static UnitType
        firestarter;
    public static void load() {
        firestarter = new UnitType("firestarter"){{
                constructor = LegsUnit::create;

                health = 2000;
                armor = 3;
                hitSize = 20f;

                speed = 0.5f;
                drag = 0.3f;
                rotateSpeed = 5f;

                legCount = 6;
                legLength = 35f;
                legForwardScl = 0.8f;
                legBaseOffset = 0f;
                lockLegBase = true;
                legContinuousMove = true;
                hovering = true;
            }};
    }
}
