package bloodborne.entities;

import bloodborne.sounds.SoundManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiEnemy extends Enemy {

    private final List<Enemy> ENEMIES;

    public MultiEnemy(String id, String description, Map<String, String> att) {
        super(id, description, att);
        ENEMIES = new ArrayList<>();
        for (int i = 1; i <= Integer.parseInt(att.get("enemyAmount")); i++){
            ENEMIES.add(new Enemy("enemy-placeholder", "No description because these enemies are placeholders for HP and eventual stuns differentiation", getATTRIBUTES()));
        }
    }

    @Override
    public String takeDamage(String action, int damage, SoundManager soundManager) {
        StringBuilder explanationText = new StringBuilder();
        int counter = 0;
        for (Enemy e : ENEMIES) {
            if (!e.isDead()) {
                explanationText.append("You attack the n°").append(counter).append("enemy in melee range, ");
                if (Math.random() < e.getDodgeRate()) {
                    explanationText.append("he avoided the attack\n");
                } else {
                    explanationText.append(e.takeDamage(action, damage, soundManager));
                }
            }
            counter++;
        }
        return explanationText.append("\n").toString();
    }

    @Override
    public String attack(Entity target, double hunterDodgeRate, SoundManager soundManager) {
        Hunter hunter = (Hunter) target;
        StringBuilder explanationText = new StringBuilder();
        int counter = 0;
        for (Enemy e : ENEMIES) {
            if (!e.isDead()) {
                if (Math.random() < hunterDodgeRate) {
                    explanationText.append("You avoided the n°").append(counter).append(" enemy's attack");
                } else {
                    explanationText.append("The n°").append(counter).append(" enemy strikes back, you took ").append(hunter.takeDamage(getDamage(), soundManager)).append(" damage");
                }
            }
            counter++;
        }
        return explanationText.toString();
    }
}
