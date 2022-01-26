package bloodborne.entities;

import bloodborne.sounds.SoundManager;

import java.util.Map;

public abstract class Entity {

    private final String ID;
    private final String DESCRIPTION;
    protected final Map<String, String> ATTRIBUTES;
    protected int healthPoints;

    public Entity(String id, String description, Map<String, String> att) {
        ID = id;
        DESCRIPTION = description;
        ATTRIBUTES = att;
        healthPoints = Integer.parseInt(att.get("maxHealth"));
    }

    public String getID() {
        return ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public int getCurrentHealthPoints() {
        return healthPoints;
    }

    public double getDodgeRate() {
        return Double.parseDouble(ATTRIBUTES.get("dodgeRate"));
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }

    public void fullRegen() {
        healthPoints = Integer.parseInt(ATTRIBUTES.get("maxHealth"));
    }

    public void takeDamage(int damage) {
        if ((healthPoints - damage) < 0) {
            healthPoints = 0;
        } else {
            healthPoints -= damage;
        }
    }

    public abstract int getDamage();

    public abstract String attack(Entity target, SoundManager soundManager);
}
