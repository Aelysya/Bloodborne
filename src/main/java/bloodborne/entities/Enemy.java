package bloodborne.entities;

import java.util.Map;

public class Enemy extends Entity{

    private final String NAME;
    private final int DAMAGE;

    public Enemy(String id, String description, Map<String, String> att) {
        super(id, description, att);
        NAME = att.get("name");
        DAMAGE = Integer.parseInt(att.get("damage"));
    }

    public String getNAME() {
        return NAME;
    }

    @Override
    public int getDamage() {
        return DAMAGE;
    } //TODO Make damage random or multiple possible attack types

    @Override
    public boolean attack(Entity target) {
        return target.takeDamage(getDamage());
    }

}
