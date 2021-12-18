package bloodborne.conditions;

import bloodborne.entities.Enemy;
import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.zone.Zone;

public class KilledNPC extends Condition{

    private final String ENEMY_ID;

    public KilledNPC(Zone zone, String enemyId) throws MalFormedJsonException {
        super(zone);
        ENEMY_ID = enemyId;
    }

    @Override
    public boolean checkCondition() {
        return ZONE.getEnemyById(ENEMY_ID).isDead();
    }
}
