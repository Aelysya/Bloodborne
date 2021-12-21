package bloodborne.conditions;

import bloodborne.environment.Prop;
import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.zone.Zone;

public class LookedProp extends Condition{

    private final Prop PROP;

    public LookedProp(Zone zone, String propId) throws MalFormedJsonException {
        super(zone);
        PROP = zone.getPropById(propId);
        if(PROP == null){
            throw new MalFormedJsonException("Prop not present on the map : " + propId);
        }
    }

    @Override
    public boolean checkCondition() {
        return PROP.hasBeenLooked();
    }
}
