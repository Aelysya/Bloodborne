package bloodborne.entities;

import java.util.Map;

public class Enemy extends Entity{

    public Enemy(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public String getNAME() {
        return ATTRIBUTES.get("name");
    }

    @Override
    public int getDamage() {
        return Integer.parseInt(ATTRIBUTES.get("damage"));
    } //TODO Make damage random or multiple possible attack types

    @Override
    public boolean attack(Entity target) {
        return target.takeDamage(getDamage());
    }

    public void takeEchoes(Hunter hunter){
        int totalValue = Integer.parseInt(ATTRIBUTES.get("bloodEchoes")) + hunter.getBloodEchoes();
        ATTRIBUTES.replace("bloodEchoes", Integer.toString(totalValue));
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
        hunter.gainBloodEchoes(Integer.parseInt(ATTRIBUTES.get("bloodEchoes")));
        return "You looted the corpse of your enemy and retrieved some useful consumables.";
    }

}
