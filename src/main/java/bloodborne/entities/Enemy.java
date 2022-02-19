package bloodborne.entities;

import bloodborne.sounds.SoundManager;

import java.util.Map;

public class Enemy extends Entity {

    private int takenBloodEchoes;
    private int stunTurnsLeft;

    public Enemy(String id, String description, Map<String, String> att) {
        super(id, description, att);
        takenBloodEchoes = 0;
        stunTurnsLeft = 0;
    }

    public String attack(Entity target, double hunterDodgeRate, SoundManager soundManager) {
        Hunter hunter = (Hunter) target;
        StringBuilder explanationText = new StringBuilder();
        if (isStunned()) {
            explanationText.append("Your enemy is not able to strike back\n");
            recoverOneStunTurn();
        } else {
            if (Math.random() < hunterDodgeRate) {
                explanationText.append("You avoided your enemy's attack\n");
            } else {
                explanationText.append("Your enemy strikes back, you took ").append(hunter.takeDamage(getDamage(), soundManager)).append(" damage\n");
            }
        }
        return explanationText.toString();
    }

    @Override
    public int getDamage() {
        int finalDamage = Integer.parseInt(getATTRIBUTES().get("damage"));
        if (Math.random() < 0.125) { // 1/8 chance to deal 50% more damage (Critical hit)
            finalDamage = (int) (finalDamage * 1.5);
        }
        return finalDamage;
    }

    public String takeDamage(String action, int damage, SoundManager soundManager) {
        StringBuilder explanationText = new StringBuilder();
        explanationText.append("you did ").append(damage).append(" damage");
        if ((healthPoints - damage) < 0) {
            healthPoints = 0;
        } else {
            healthPoints -= damage;
        }
        switch (action) {
            case "heavy-melee" -> {
                if (Math.random() < 0.1) {
                    explanationText.append(" and stunned your enemy");
                    stunForXTurns(1);
                }
            }
            case "charged-melee" -> {
                if (Math.random() < 0.2) {
                    explanationText.append(" and stunned your enemy");
                    stunForXTurns(1);
                }
            }
        }
        return explanationText.append("\n").toString();
    }

    public void takeEchoes(Hunter hunter) {
        takenBloodEchoes = hunter.getBloodEchoes();
    }

    public String loot(Hunter hunter) {
        /*double randomValue = Math.random(); //TODO Make a better looting system (add other sort of items before)
        switch (ATTRIBUTES.get("lootValue")){
            case "basic" -> hunter.addBullets(1);
            case "common" -> hunter.addBullets(2);
            case "uncommon" -> {
                hunter.addBullets(2);
                hunter.addVials(1);
            }
            case "rare" -> {
                hunter.addBullets(4);
                hunter.addVials(2);
            }
            case "very rare" -> {
                hunter.addBullets(5);
                hunter.addVials(3);
            }
        }*/
        hunter.gainBloodEchoes(Integer.parseInt(getATTRIBUTES().get("bloodEchoes")) + takenBloodEchoes);
        return "You loot the corpse and retrieve some useful consumables.";
    }

    public void resetEnemy() {
        if (Boolean.parseBoolean(getATTRIBUTES().get("canRespawn"))) {
            fullRegen();
            takenBloodEchoes = 0;
            System.out.println("Reset : " + getATTRIBUTES().get("id"));
        }
    }

    public String getNAME() {
        return getATTRIBUTES().get("name");
    }

    public Boolean isBeast() {
        return Boolean.parseBoolean(getATTRIBUTES().get("isBeast"));
    }

    public double getAttackSpeed() {
        return AttackSpeed.valueOf(getATTRIBUTES().get("attackSpeed")).getDODGE_MODIFICATION();
    }

    public void stunForXTurns(int amount) {
        stunTurnsLeft = amount;
    }

    public void recoverOneStunTurn() {
        stunTurnsLeft = (stunTurnsLeft == 0) ? 0 : stunTurnsLeft-1;
    }

    public boolean isStunned() {
        return stunTurnsLeft != 0;
    }
}
