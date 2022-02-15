package bloodborne.items;

import bloodborne.conditions.Condition;
import bloodborne.entities.Hunter;
import bloodborne.exceptions.ReflectionException;
import bloodborne.json.Initializable;
import bloodborne.sounds.SoundManager;
import bloodborne.world.World;

import java.lang.reflect.Constructor;
import java.util.Map;

public class ConditionalGroundItem extends GroundItem implements Initializable {

    private Condition condition;

    public ConditionalGroundItem(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
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
        super.initialize(world);
    }

    @Override
    public String take(Hunter hunter, SoundManager soundManager) {
        return condition.checkCondition() ? super.take(hunter, soundManager) : getATTRIBUTES().get("conditionFalseText");
    }
}
