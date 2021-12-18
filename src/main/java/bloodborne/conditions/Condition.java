package bloodborne.conditions;

import bloodborne.zone.Zone;

public abstract class Condition {

    protected final Zone ZONE;

    public Condition(Zone ZONE){
        this.ZONE = ZONE;
    }

    public abstract boolean checkCondition();

}
