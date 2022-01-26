package bloodborne.conditions;

import bloodborne.environment.Prop;
import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.world.World;

public class LookedProp extends Condition{

    private final Prop PROP;

    public LookedProp(World world, String propId) throws MalFormedJsonException {
        super(world);
        PROP = world.getPropById(propId);
        if (PROP == null){
            throw new MalFormedJsonException("Prop not present in json files : " + propId);
        }
    }

    @Override
    public boolean checkCondition() {
        return PROP.hasBeenLooked();
    }
}
