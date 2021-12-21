package bloodborne.zone;

import bloodborne.conditions.Condition;
import bloodborne.exceptions.ReflectionException;

import java.lang.reflect.Constructor;
import java.util.Map;

public class ConditionalExit extends Exit{

    private Condition condition;

    public ConditionalExit(Place out, Map<String, String> att) {
        super(out, att);
        condition = null;
    }

    @Override
    public void initialize(Zone zone) throws ReflectionException {
        try {
            Class<?> conditionClass = Class.forName("bloodborne.conditions." + ATTRIBUTES.get("conditionType"));
            Constructor<?> conditionConstructor = conditionClass.getConstructor(Zone.class, String.class);
            condition = (Condition) conditionConstructor.newInstance(zone, ATTRIBUTES.get("objectName"));
        } catch (ClassNotFoundException e) {
            throw new ReflectionException("Class not found for : " + ATTRIBUTES.get("conditionType"));
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Constructor (Zone zone, String objectName) not found in : " + ATTRIBUTES.get("type"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isTraversable(){
        return condition.checkCondition();
    }

    @Override
    public String getDescription(){
        return condition.checkCondition() ? ATTRIBUTES.get("altDescription") : ATTRIBUTES.get("description");
    }
}
