package bloodborne.conditions;

import bloodborne.world.World;

public abstract class Condition {

    protected final World WORLD;

    public Condition(World WORLD) {
        this.WORLD = WORLD;
    }

    public abstract boolean checkCondition();
}
