package bloodborne.items;

import java.util.Map;

public abstract class Weapon extends ItemWithDamageScaling {

    public Weapon(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public String getAttackSound() {
        return getID() + ".wav";
    }

    public abstract int getCurrentDamage();
}
