package bloodborne.entities;

import bloodborne.sounds.SoundManager;

import java.util.Map;

public class Enemy extends Entity{

    private int takenBloodEchoes;

    public Enemy(String id, String description, Map<String, String> att) {
        super(id, description, att);
        takenBloodEchoes = 0;
    }

    public String getNAME() {
        return ATTRIBUTES.get("name");
    }

    @Override
    public int getDamage() {
        return Integer.parseInt(ATTRIBUTES.get("damage"));
    } //TODO Make damage random or multiple possible attack types

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
    }

    public void takeEchoes(Hunter hunter){
        takenBloodEchoes = hunter.getBloodEchoes();
    }

    public String loot(Hunter hunter){
        double randomValue = Math.random(); //TODO Make a better looting system (add other sort of items before)
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
        }
        hunter.gainBloodEchoes(Integer.parseInt(ATTRIBUTES.get("bloodEchoes")) + takenBloodEchoes);
        return "You loot the corpse and retrieve some useful consumables.";
    }

    public void resetEnemy(){
        if (Boolean.parseBoolean(ATTRIBUTES.get("canRespawn"))){
            fullRegen();
            takenBloodEchoes = 0;
            System.out.println("Reset : " + ATTRIBUTES.get("id"));
        } else {
            System.out.println("Enemy not reset : " + ATTRIBUTES.get("id"));
        }
    }

}
