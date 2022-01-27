package bloodborne.environment;

import bloodborne.conditions.Condition;
import bloodborne.entities.Hunter;
import bloodborne.exceptions.ReflectionException;
import bloodborne.json.Initializable;
import bloodborne.world.World;

import java.lang.reflect.Constructor;
import java.util.Map;

public class ConditionalProp extends Prop implements Initializable {

    private Condition condition;

    public ConditionalProp(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public void initialize(World world) throws ReflectionException {
        try {
            Class<?> conditionClass = Class.forName("bloodborne.conditions." + getATTRIBUTES().get("conditionType"));
            Constructor<?> conditionConstructor = conditionClass.getConstructor(World.class, String.class);
            condition = (Condition) conditionConstructor.newInstance(world, getATTRIBUTES().get("objectName"));
        } catch (ClassNotFoundException e) {
            throw new ReflectionException("Class not found for : " + getATTRIBUTES().get("conditionType"));
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Constructor (World world, String objectId) not found in : " + getATTRIBUTES().get("type"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String activate(Hunter hunter) {
        return condition.checkCondition() ? super.activate(hunter) : getATTRIBUTES().get("conditionFalseText");
    }

    @Override
    public String look(Hunter hunter) {
        return condition.checkCondition() ? super.look(hunter) : getATTRIBUTES().get("conditionFalseText");
    }
}
