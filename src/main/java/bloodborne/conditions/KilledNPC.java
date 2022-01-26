package bloodborne.conditions;

import bloodborne.entities.Enemy;
import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.world.World;

public class KilledNPC extends Condition{

    private final Enemy ENEMY;

    public KilledNPC(World world, String enemyId) throws MalFormedJsonException {
        super(world);
        ENEMY = WORLD.getEnemyById(enemyId);
        if (ENEMY == null){
            throw new MalFormedJsonException("Enemy not present in json files : " + enemyId);
        }
    }

    @Override
    public boolean checkCondition() {
        return ENEMY.isDead();
    }
}
