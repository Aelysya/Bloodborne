package bloodborne.conditions;

import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.world.World;

public class KilledNPC extends Condition{

    private final String ENEMY_ID;

    public KilledNPC(World world, String enemyId){
        super(world);
        ENEMY_ID = enemyId;
    }

    @Override
    public boolean checkCondition() {
        return WORLD.getEnemyById(ENEMY_ID).isDead();
    }
}
