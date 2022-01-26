package bloodborne.items;

import java.util.Map;

public abstract class Weapon extends Item {

    public Weapon(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public abstract int getCurrentDamage();

    public StatScaling getStrengthScaling() {
        return StatScaling.valueOf(ATTRIBUTES.get("strengthScaling"));
    }

    public StatScaling getSkillScaling() {
        return StatScaling.valueOf(ATTRIBUTES.get("skillScaling"));
    }

    public StatScaling getBloodScaling() {
        return StatScaling.valueOf(ATTRIBUTES.get("bloodScaling"));
    }

    public StatScaling getArcaneScaling() {
        return StatScaling.valueOf(ATTRIBUTES.get("arcaneScaling"));
    }
}
