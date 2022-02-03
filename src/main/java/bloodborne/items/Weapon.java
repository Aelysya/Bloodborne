package bloodborne.items;

import java.util.Map;

public abstract class Weapon extends Item {

    public Weapon(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public abstract int getCurrentDamage();

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
