package bloodborne.entities;

import java.util.Map;

public class MultiEnemy extends Enemy{

    //private int enemyAmount;
    //private int oneEnemyHealth;

    public MultiEnemy(String id, String description, Map<String, String> att) {
        super(id, description, att);
        /*enemyAmount = Integer.parseInt(att.get("enemyAmount"));
        Map<Integer, Integer> thresholds = new HashMap<>();
        oneEnemyHealth = Integer.parseInt(att.get("health")) / enemyAmount;
        for (int i = 0; i<=enemyAmount; i++){
            thresholds.put(i, oneEnemyHealth * i);
        }*/
    }

    /*public int getTotalDamage() {
        return Integer.parseInt(ATTRIBUTES.get("damage")) * enemyAmount;
    }

    public int getEnemyAmount(){
        return enemyAmount;
    }

    @Override
    public void takeDamage(int damage) {
        if ((healthPoints - damage) < 0 ){
            healthPoints = 0;
        } else {
            healthPoints-=damage;
        }
    }

    @Override
    public String attack(Entity target, SoundManager soundManager) {
        Hunter hunter = (Hunter) target;
        StringBuilder s = new StringBuilder();
        if (Math.random() < target.getDodgeRate()){
            s.append("You avoided your enemy's attack.");
        } else {
            if (hunter.hasRune("Lake rune")){
                s.append("You took ").append(getDamage() - 1).append(" damage !");
            } else {
                s.append("You took ").append(getDamage()).append(" damage !");
            }
            target.takeDamage(getDamage());
        }
        return s.toString();
    }*/
}
