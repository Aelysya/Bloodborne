package bloodborne.conditions;

import bloodborne.environment.Prop;
import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.world.World;

public class ActivatedProp extends Condition{

    private final Prop PROP;

    public ActivatedProp(World world, String propId) throws MalFormedJsonException {
        super(world);
        PROP = world.getPropById(propId);
        if (PROP == null){
            throw new MalFormedJsonException("Prop not present in json files : " + propId);
        }
    }

    @Override
    public boolean checkCondition() {
        return PROP.isActivated();
    }
}
