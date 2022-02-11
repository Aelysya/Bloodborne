package bloodborne.items;

import java.util.Map;

public class ItemWithDamageScaling extends Item {

    public ItemWithDamageScaling(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
    }

    public double getStrengthScaling() {
        return StatScaling.valueOf(getATTRIBUTES().get("strengthScaling")).getRATIO();
    }

    public double getSkillScaling() {
        return StatScaling.valueOf(getATTRIBUTES().get("skillScaling")).getRATIO();
    }

    public double getBloodScaling() {
        return StatScaling.valueOf(getATTRIBUTES().get("bloodScaling")).getRATIO();
    }

    public double getArcaneScaling() {
        return StatScaling.valueOf(getATTRIBUTES().get("arcaneScaling")).getRATIO();
    }
}
