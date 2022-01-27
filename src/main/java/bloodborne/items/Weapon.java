package bloodborne.items;

import java.util.Map;

public abstract class Weapon extends Item {

    public Weapon(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public abstract int getCurrentDamage();

    public StatScaling getStrengthScaling() {
        return StatScaling.valueOf(getATTRIBUTES().get("strengthScaling"));
    }

    public StatScaling getSkillScaling() {
        return StatScaling.valueOf(getATTRIBUTES().get("skillScaling"));
    }

    public StatScaling getBloodScaling() {
        return StatScaling.valueOf(getATTRIBUTES().get("bloodScaling"));
    }

    public StatScaling getArcaneScaling() {
        return StatScaling.valueOf(getATTRIBUTES().get("arcaneScaling"));
    }
}
