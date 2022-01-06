package bloodborne.environment;

import bloodborne.conditions.Condition;
import bloodborne.entities.Hunter;
import bloodborne.exceptions.ReflectionException;
import bloodborne.world.World;

import java.lang.reflect.Constructor;
import java.util.Map;

public class ConditionalProp extends Prop{

    private Condition condition;

    public ConditionalProp(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public String activate(Hunter hunter) {
        return "You can't activate this";
    }

    @Override
    public String lookReaction(Hunter hunter){
        String txt;
        if(condition.checkCondition()){
            if(ATTRIBUTES.get("isContainer") == null){
                addConsumables(hunter);
            }
            txt = getDESCRIPTION();
        } else {
            txt = ATTRIBUTES.get("conditionFalseText");
        }
        return txt;
    }

    @Override
    public void initialize(World world) throws ReflectionException {
        try {
            Class<?> conditionClass = Class.forName("bloodborne.conditions." + ATTRIBUTES.get("conditionType"));
            Constructor<?> conditionConstructor = conditionClass.getConstructor(World.class, String.class);
            condition = (Condition) conditionConstructor.newInstance(world, ATTRIBUTES.get("objectName"));
        } catch (ClassNotFoundException e) {
            throw new ReflectionException("Class not found for : " + ATTRIBUTES.get("conditionType"));
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Constructor (World world, String objectName) not found in : " + ATTRIBUTES.get("type"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
