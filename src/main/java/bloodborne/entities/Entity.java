package bloodborne.entities;

import java.util.Map;

public abstract class Entity {

    private final String ID;
    protected int healthPoints;
    protected final double DODGE_RATE;
    private final String DESCRIPTION;
    protected final Map<String, String> ATTRIBUTES;

    public Entity(String id, String description, Map<String, String> att) {
        ID = id;
        DESCRIPTION = description;
        healthPoints = Integer.parseInt(att.get("health"));
        DODGE_RATE = Double.parseDouble(att.get("dodgeRate"));
        ATTRIBUTES = att;
    }

    public String getID() {
        return ID;
    }

    public int getHealthPoints() {
        return Math.max(healthPoints, 0);
    }

    public double getDodgeRate() {
        return DODGE_RATE;
    }

    public boolean takeDamage(int damage) {
        if((healthPoints - damage) < 0 ){
            healthPoints = 0;
        } else {
            healthPoints-=damage;
        }
        return getHealthPoints() <= 0;
    }

    public abstract int getDamage();

    public abstract boolean attack(Entity target);

    public boolean isDead(){
        return this.healthPoints <= 0;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }
}
