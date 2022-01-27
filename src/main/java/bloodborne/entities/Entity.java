package bloodborne.entities;

import bloodborne.json.ElementTemplate;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public abstract class Entity extends ElementTemplate {

    protected int healthPoints;

    public Entity(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
        healthPoints = Integer.parseInt(attributes.get("maxHealth"));
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }

    public void fullRegen() {
        healthPoints = Integer.parseInt(getATTRIBUTES().get("maxHealth"));
    }

    public void takeDamage(int damage) {
        if ((healthPoints - damage) < 0) {
            healthPoints = 0;
        } else {
            healthPoints -= damage;
        }
    }

    public int getCurrentHealthPoints() {
        return healthPoints;
    }

    public double getDodgeRate() {
        return Double.parseDouble(getATTRIBUTES().get("dodgeRate"));
    }

    public abstract int getDamage();

    public abstract String attack(Entity target, SoundManager soundManager);
}
