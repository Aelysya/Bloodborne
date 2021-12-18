package bloodborne.entities;

import bloodborne.items.*;
import bloodborne.sounds.SoundManager;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    }

    public void addItem(Item item){
        INVENTORY.addItem(item);
    }

    public void removeItem(Item item){
        INVENTORY.removeItem(item);
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

    public boolean hasItem(String itemId){
        return INVENTORY.hasItem(itemId);
    }

    public String showInventory(){
        return INVENTORY.showInventory();
    }

    public void addVials(int vialAmount){
        vialsNumber+=vialAmount;
    }

    public void addBullets(int bulletAmount){
        bulletsNumber+=bulletAmount;
    }

    public void wasteBullet(){
        bulletsNumber--;
    }

    public int getVialsNumber(){
        return vialsNumber;
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
                soundManager.playSoundEffect("used_bloodvial.wav");
            }
        }

        return s.toString();
    }

    public void regenAfterVisceral(int amount){ //TODO verify it works correctly
        if(healthPoints != 30){
            healthPoints+=amount;
            if(healthPoints>30){
                healthPoints=30;
            }
        }
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
            soundManager.playSoundEffect("fire_applied.wav");
            damageType = DamageType.FIRE;
        } else {
            soundManager.playSoundEffect("bolt_applied.wav");
            damageType = DamageType.BOLT;
        }
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
            rates += DODGE_RATE;
        } else {
            rates += trickWeapon.getCurrentDodgeRate();
        }
        if(fireArm != null){
            rates += ", hit rate : " + fireArm.getHIT_RATE() + ", visceral rate : " + fireArm.getVISCERAL_RATE();
        }
        return rates + "\n";
    }

    @Override
    public int getDamage() {
        return (trickWeapon == null) ? 1 : trickWeapon.getCurrentDamage() + this.damageBoost;
    }

    public int getFireArmDamage() {
        return (fireArm == null) ? 0 : fireArm.getCurrentDamage();
    }

    @Override
    public double getDodgeRate(){
        return trickWeapon == null ? DODGE_RATE : trickWeapon.getCurrentDodgeRate();
    }

    @Override
    public boolean attack(Entity target) {
        int finalDamage = getDamage(); //To avoid losing one instance of boosted damage from the code below
        if(boostLeft != 0){
            boostLeft--;
            if(boostLeft == 0){
                damageBoost = 0;
                damageType = DamageType.BASE;
            }
        }
        return target.takeDamage(finalDamage);
    }

    public String getDamageType(){
        return damageType.toString();
    }

    public boolean shoot(Entity target) {
        bulletsNumber-=fireArm.getBULLET_USE();
        return target.takeDamage(getFireArmDamage());
    }

    public boolean canShoot(){
        boolean canShoot = true;
        if(fireArm == null){
            canShoot = false;
        } else {
            int usage = fireArm.getBULLET_USE();
            if((bulletsNumber - usage) < 0){
                canShoot = false;
            }
        }
        return canShoot;
    }

    public int getBulletsNumber(){
        return bulletsNumber;
    }

    public String getTrickWeaponIcon(){
        String path;
        if(trickWeapon == null){
            path = "empty_hand.png";
        } else {
            path = trickWeapon.getIcon();
        }
        return path;
    }

    public String getFireArmIcon(){
        String path;
        if(fireArm == null){
            path = "empty_hand.png";
        } else {
            path = fireArm.getIcon();
        }
        return path;
    }
}
