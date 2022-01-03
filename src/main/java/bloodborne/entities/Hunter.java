package bloodborne.entities;

import bloodborne.items.*;
import bloodborne.sounds.SoundManager;

import java.util.*;

enum DamageType {FIRE, BOLT, BASE}

public class Hunter extends Entity{

    private int damageBoost;
    private int boostLeft;
    private final Inventory INVENTORY;
    private TrickWeapon trickWeapon;
    private FireArm fireArm;
    private int vialsNumber;
    private int bulletsNumber;
    private DamageType damageType;
    private int bloodEchoes;
    private final List<Rune> RUNE_LIST;
    private boolean lastAttackIsVisceral;

    private static final Map<String, String> CONSTRUCT_MAP = new HashMap<>();
    static{
        CONSTRUCT_MAP.put("health", "30");
        CONSTRUCT_MAP.put("dodgeRate", "0.3");
    }

    public Hunter() {
        super("hunter", "", CONSTRUCT_MAP);
        INVENTORY = new Inventory();
        damageBoost = 0;
        boostLeft = 0;
        vialsNumber = 2;
        bulletsNumber = 0;
        damageType = DamageType.BASE;
        bloodEchoes = 0;
        RUNE_LIST = new ArrayList<>();
        lastAttackIsVisceral = false;
    }

    public void addItem(Item item){
        INVENTORY.addItem(item);
    }

    public void removeItem(Item item){
        INVENTORY.removeItem(item);
    }

    public void addVials(int vialAmount){
        vialsNumber+=vialAmount;
    }

    public void addBullets(int bulletAmount){
        bulletsNumber+=bulletAmount;
    }

    public void gainBloodEchoes(int amount){
        int finalAmount = amount;
        if (hasRune("Moon rune")){
            finalAmount*=0.3;
        }
        bloodEchoes+=finalAmount;
    }

    public void spendBloodEchoes(int amount){
        bloodEchoes-=amount;
    }

    public Item getItemByName(String itemName){
        itemName = itemName.toLowerCase(Locale.ROOT);
        Item returnedItem;
        if (trickWeapon != null && itemName.equals(trickWeapon.getNAME().toLowerCase(Locale.ROOT))){
            returnedItem = trickWeapon;
        } else if (fireArm != null && itemName.equals(fireArm.getNAME().toLowerCase(Locale.ROOT))){
            returnedItem = fireArm;
        } else {
            returnedItem = INVENTORY.getItemByName(itemName);
        }
        return returnedItem;
    }

    public String showInventory(){
        return INVENTORY.showInventory();
    }

    public int getVialsNumber(){
        return vialsNumber;
    }

    public int getBulletsNumber(){
        return bulletsNumber;
    }

    public int getBloodEchoes(){
        return bloodEchoes;
    }

    public int getBoostLeft(){
        return boostLeft;
    }

    public int getDamageBoost() {
        return damageBoost;
    }

    public TrickWeapon getTrickWeapon() {
        if(trickWeapon == null){
            return null;
        }
        return trickWeapon;
    }

    public FireArm getFireArm() {
        if(fireArm == null){
            return null;
        }
        return fireArm;
    }

    public int getNumberOfRunes(){
        return RUNE_LIST.size();
    }

    @Override
    public int getDamage() {
        return (trickWeapon == null) ? 1 : trickWeapon.getCurrentDamage() + this.damageBoost;
    }

    public String getDamageType(){
        return damageType.toString();
    }

    public int getFireArmDamage() {
        return (fireArm == null) ? 0 : fireArm.getCurrentDamage();
    }

    @Override
    public double getDodgeRate(){
        return trickWeapon == null ? Double.parseDouble(ATTRIBUTES.get("dodgeRate")) : trickWeapon.getCurrentDodgeRate();
    }

    public double getHitRate(){
        return fireArm == null ? 0 : fireArm.getHIT_RATE();
    }

    public double getVisceralRate(){
        return fireArm == null ? 0 : fireArm.getVISCERAL_RATE();
    }

    public String getTrickWeaponIcon(){
        String path;
        if(trickWeapon == null){
            path = "images/empty.png";
        } else {
            path = "images/items/" + trickWeapon.getIcon();
        }
        return path;
    }

    public String getFireArmIcon(){
        String path;
        if(fireArm == null){
            path = "images/empty.png";
        } else {
            path = "images/items/" + fireArm.getIcon();
        }
        return path;
    }

    public List<Rune> getRUNE_LIST(){
        return RUNE_LIST;
    }

    public boolean isLastAttackVisceral(){
        return lastAttackIsVisceral;
    }

    public boolean hasItem(String itemId){
        return INVENTORY.hasItem(itemId);
    }

    public String heal(SoundManager soundManager){
        StringBuilder s = new StringBuilder();

        if(vialsNumber == 0){
            s.append("You have no blood vials left.");
        } else {
            if(healthPoints == 30){
                s.append("You don't need to use a blood vial right now, control your thirst or you'll get addicted.");
            } else {
                healthPoints+=10;
                if(healthPoints>30){
                    healthPoints=30;
                }
                s.append("You use a blood vial, your wounds heal and a dark part of you wants more of it.");
                vialsNumber--;
                soundManager.playSoundEffect("used-bloodvial.wav");
            }
        }

        return s.toString();
    }

    public String equipTrickWeapon(TrickWeapon weapon) {
        if (trickWeapon != null) {
            this.INVENTORY.addItem(trickWeapon);
        }
        INVENTORY.removeItem(weapon);
        trickWeapon = weapon;

        return "You equipped the " + weapon.getNAME() + " as your trick weapon.";
    }

    public String equipFireArm(FireArm weapon) {
        if (fireArm != null) {
            this.INVENTORY.addItem(fireArm);
        }
        INVENTORY.removeItem(weapon);
        fireArm = weapon;

        return "You equipped the " + weapon.getNAME() + " as your gun.";
    }

    public String equipRune(Rune rune, int position){
        if (position != -1){ //-1 is when we have less than 3 runes equipped and just want to add one more
            RUNE_LIST.add(position, rune);
            INVENTORY.addItem(RUNE_LIST.remove(position+1));
        }
        RUNE_LIST.add(rune);
        INVENTORY.removeItem(rune);
        return rune.getEquipText();
    }

    public boolean hasRune(String runeName){
        boolean hasRune = false;
        for(Rune r : RUNE_LIST){
            if(RUNE_LIST.isEmpty()){
                break;
            }
            if(r.getNAME().equals(runeName)){
                hasRune = true;
                break;
            }
        }
        return hasRune;
    }

    public String switchTrickWeaponState(){
        String txt;
        if(trickWeapon == null){
            txt = "You don't have any trick weapon equipped";
        } else {
            txt = "You switched your trick weapon's state, check your status to see the changes";
            trickWeapon.switchMode();
        }
        return txt;
    }

    public void boostDamage(int boost, Paper p, SoundManager soundManager) {
        damageBoost = boost;
        boostLeft = 5;
        removeItem(p);
        if(p.getTYPE().equals("fire")){
            soundManager.playSoundEffect("fire-applied.wav");
            damageType = DamageType.FIRE;
        } else {
            soundManager.playSoundEffect("bolt_applied.wav");
            damageType = DamageType.BOLT;
        }
    }

    @Override
    public String attack(Entity target, SoundManager soundManager) {
        StringBuilder s = new StringBuilder();
        s.append("You attack your enemy, ");
        if (Math.random() < target.getDodgeRate()){
            s.append("he avoided the attack.");
        } else {
            int finalDamage = getDamage(); //To avoid losing one instance of boosted damage from the code below
            if(boostLeft != 0){
                boostLeft--;
                if(boostLeft == 0){
                    damageBoost = 0;
                    damageType = DamageType.BASE;
                }
            }
            target.takeDamage(finalDamage);
            s.append("you did ").append(getDamage()).append(" damage !");
            switch (damageType){
                case FIRE -> soundManager.playSoundEffect("enemy-hit-fire.wav");
                case BOLT -> soundManager.playSoundEffect("enemy-hit-bolt.wav");
                default -> soundManager.playSoundEffect("enemy-hit.wav");
            }
        }
        lastAttackIsVisceral = false;
        return s.toString();
    }

    @Override
    public void takeDamage(int damage){
        int finalDamage = damage;
        if (hasRune("Lake rune")){
            finalDamage--;
        }
        if((healthPoints - finalDamage) < 0 ){
            healthPoints = 0;
        } else {
            healthPoints-=finalDamage;
        }
    }

    public void regenAfterVisceral(int amount){
        if (hasRune("Heir rune")){
            gainBloodEchoes(500);
        }
        if(healthPoints != 30){
            healthPoints+=amount;
            if(healthPoints>30){
                healthPoints=30;
            }
        }
    }

    public String shoot(Entity target, SoundManager soundManager) {
        StringBuilder s = new StringBuilder();
        if (cantShoot()){
            s.append("You don't have enough quicksilver bullets left to shoot.");
        } else if (fireArm == null){
            s.append("You have no gun equipped.");
        } else {
            s.append("You shoot at your enemy, ");
            soundManager.playSoundEffect("gunshot.wav");
            if (Math.random() > fireArm.getHIT_RATE()){ //Shot is missed
                bulletsNumber-=fireArm.getBULLET_USE();
                s.append("you missed.");
            } else {
                if (Math.random() < fireArm.getVISCERAL_RATE()){ //Player performs a visceral attack, regenerating health and cancelling the enemy's attack
                    s.append("you shot at the right timing and perform a visceral attack on him. It regenerates a bit of your life.\n");
                    soundManager.playSoundEffect("visceral-attack.wav");
                    regenAfterVisceral(target.getDamage());
                    target.takeDamage(fireArm.getCurrentDamage()+10);
                    lastAttackIsVisceral = true;
                    s.append("You did ").append(fireArm.getCurrentDamage() + 10).append(" damage !");
                } else { //Classic ranged attack
                    target.takeDamage(fireArm.getCurrentDamage());
                    lastAttackIsVisceral = false;
                    s.append("You did ").append(fireArm.getCurrentDamage() + 10).append(" damage !");
                }
            }
        }
        return s.toString();
    }

    public boolean cantShoot(){
        boolean cantShoot = false;
        if(fireArm == null){
            cantShoot = true;
        } else {
            int usage = fireArm.getBULLET_USE();
            if((bulletsNumber - usage) < 0){
                cantShoot = true;
            }
        }
        return cantShoot;
    }


    //Deprecated methods

    public String getTrickWeaponToString(){
        return getTrickWeapon() != null ? getTrickWeapon().getNAME() + ", deals " + getTrickWeapon().getCurrentDamage() + " (+ " + getDamageBoost() + ") damage\n" :
                "No trick weapon equipped. You deal 1 damage with your fists.\n";
    }

    public String getFireArmToString(){
        return getFireArm() != null ? getFireArm().getNAME() + ", deals " + getFireArm().getCurrentDamage() + " damage. Uses " + fireArm.getBULLET_USE() + " bullet(s) per shot\n":
                "No gun equipped.\n";
    }

    public String getRates(){
        String rates = "Dodge rate : ";
        if(trickWeapon == null){
            rates += Double.parseDouble(ATTRIBUTES.get("dodgeRate"));
        } else {
            rates += trickWeapon.getCurrentDodgeRate();
        }
        if(fireArm != null){
            rates += ", hit rate : " + fireArm.getHIT_RATE() + ", visceral rate : " + fireArm.getVISCERAL_RATE();
        }
        return rates + "\n";
    }
}
