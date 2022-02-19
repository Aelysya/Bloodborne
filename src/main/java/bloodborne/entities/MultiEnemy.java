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

    public List<Enemy> getEnemies() {
        return ENEMIES;
    }

    @Override
    public boolean isDead() {
        boolean allDead = true;
        for (Enemy e : ENEMIES) {
            if (!e.isDead()) {
                allDead = false;
                break;
            }
        }
        return allDead;
    }

    @Override
    public String takeDamage(String action, int damage, SoundManager soundManager) { //TODO Make the method variation for when the hunter shoots
        StringBuilder explanationText = new StringBuilder();
        int counter = 1;
        for (Enemy e : ENEMIES) {
            if (!e.isDead()) {
                if (action.equals("range")) {
                    explanationText.append(e.takeDamage(action, damage, soundManager));
                    break;
                } else {
                    explanationText.append("You attack the n°").append(counter).append(" enemy in melee range, ");
                    if (Math.random() < e.getDodgeRate()) {
                        explanationText.append("he avoided the attack\n");
                    } else {
                        explanationText.append(e.takeDamage(action, damage, soundManager));
                        if (e.isDead()) {
                            explanationText.append("You killed this enemy\n");
                        }
                    }
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
        int counter = 1;
        for (Enemy e : ENEMIES) {
            if (!e.isDead()) {
                if (e.isStunned()) {
                    explanationText.append("The enemy n°").append(counter).append(" is not able to strike back\n");
                    e.recoverOneStunTurn();
                } else {
                    if (Math.random() < hunterDodgeRate) {
                        explanationText.append("You avoided the n°").append(counter).append(" enemy's attack\n");
                    } else {
                        explanationText.append("The n°").append(counter).append(" enemy strikes back, you took ").append(hunter.takeDamage(getDamage(), soundManager)).append(" damage\n");
                    }
                }
            }
            counter++;
        }
        return explanationText.toString();
    }
}
