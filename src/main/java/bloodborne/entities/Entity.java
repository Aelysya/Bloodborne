package bloodborne.entities;

import bloodborne.sounds.SoundManager;
import java.util.HashMap;
import java.util.Map;

public abstract class Entity {

    private final String ID;
    private final String DESCRIPTION;
    protected final Map<String, String> ATTRIBUTES;
    protected int healthPoints;

    public Entity(){
        ID = "id";
        DESCRIPTION = "description";
        ATTRIBUTES = new HashMap<>();
        healthPoints = 0;
    }

    public Entity(String id, String description, Map<String, String> att) {
        ID = id;
        DESCRIPTION = description;
        ATTRIBUTES = att;
        healthPoints = Integer.parseInt(att.get("health"));
    }

    public String getID() {
        return ID;
    }

    public int getHealthPoints() {
        return Math.max(healthPoints, 0);
    }

    public double getDodgeRate() {
        return Double.parseDouble(ATTRIBUTES.get("dodgeRate"));
    }

    public boolean isDead(){
        return this.healthPoints <= 0;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void fullRegen(){
        healthPoints = Integer.parseInt(ATTRIBUTES.get("health"));
    }

    public void takeDamage(int damage) {
        if((healthPoints - damage) < 0 ){
            healthPoints = 0;
        } else {
            healthPoints-=damage;
        }
    }

    public abstract int getDamage();

    public abstract String attack(Entity target, SoundManager soundManager);
}
