package bloodborne.entities;

import bloodborne.items.*;
import bloodborne.sounds.SoundManager;

import java.util.*;

public class Hunter extends Entity {

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
    private Rune oathRune;
    private boolean lastAttackIsVisceral;
    private boolean firstDeathHappened;
    private int maxHP;
    private final Map<String, Integer> STATS;

    private static final Map<String, String> CONSTRUCT_MAP = new HashMap<>();

    static {
        CONSTRUCT_MAP.put("maxHealth", "100");
        CONSTRUCT_MAP.put("dodgeRate", "0.4");
    }

    public Hunter() {
        super("hunter", "", CONSTRUCT_MAP);
        INVENTORY = new Inventory();
        damageBoost = 0;
        boostLeft = 0;
        vialsNumber = 0;
        bulletsNumber = 0;
        damageType = DamageType.PHYS;
        bloodEchoes = 0;
        RUNE_LIST = new ArrayList<>();
        lastAttackIsVisceral = false;
        firstDeathHappened = false;
        STATS = new HashMap<>();
        STATS.put("Vitality", 0);
        STATS.put("Endurance", 0);
        STATS.put("Strength", 0);
        STATS.put("Skill", 0);
        STATS.put("Bloodtinge", 0);
        STATS.put("Arcane", 0);
        maxHP = 100;
    }

    public void updateHP() {
        int baseValue = 100;
        int bonusValue;
        int vitalityPoints = STATS.get("Vitality");
        if (vitalityPoints <= 30) {
            bonusValue = 5 * vitalityPoints;
        } else if (vitalityPoints <= 50) {
            bonusValue = (5 * 30) + (2 * (vitalityPoints - 30));
        } else {
            bonusValue = (5 * 30) + (2 * 20) + vitalityPoints - 50;
        }
        maxHP = baseValue + bonusValue;
    }

    public void addItem(Item item) {
        INVENTORY.addItem(item);
    }

    public void removeItem(Item item) {
        INVENTORY.removeItem(item);
    }

    public void addVials(int vialAmount) {
        vialsNumber += vialAmount;
    }

    public void addBullets(int bulletAmount) {
        bulletsNumber += bulletAmount;
    }

    public void gainBloodEchoes(int amount) {
        int finalAmount = amount;
        if (hasRune("Moon rune")) {
            finalAmount *= 0.3;
        }
        bloodEchoes += finalAmount;
    }

    public void spendBloodEchoes(int amount) {
        bloodEchoes -= amount;
    }

    public Item getItemByName(String itemName) {
        itemName = itemName.toLowerCase(Locale.ROOT);
        Item returnedItem;
        if (trickWeapon != null && itemName.equals(trickWeapon.getNAME().toLowerCase(Locale.ROOT))) {
            returnedItem = trickWeapon;
        } else if (fireArm != null && itemName.equals(fireArm.getNAME().toLowerCase(Locale.ROOT))) {
            returnedItem = fireArm;
        } else {
            returnedItem = INVENTORY.getItemByName(itemName);
        }
        return returnedItem;
    }

    public Inventory getINVENTORY() {
        return INVENTORY;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getVialsNumber() {
        return vialsNumber;
    }

    public int getBulletsNumber() {
        return bulletsNumber;
    }

    public int getBloodEchoes() {
        return bloodEchoes;
    }

    public int getBoostLeft() {
        return boostLeft;
    }

    public int getDamageBoost() {
        return damageBoost;
    }

    public TrickWeapon getTrickWeapon() {
        return trickWeapon == null ? null : trickWeapon;
    }

    public FireArm getFireArm() {
        return fireArm == null ? null : fireArm;
    }

    public int getNumberOfRunes() {
        return RUNE_LIST.size();
    }

    @Override
    public int getDamage() {
        return (trickWeapon == null) ? 3 : trickWeapon.getCurrentDamage() + this.damageBoost;
    }

    @Override
    public double getDodgeRate() {
        double baseRate = trickWeapon == null ? Double.parseDouble(getATTRIBUTES().get("dodgeRate")) : trickWeapon.getCurrentDodgeRate();
        double bonusRate;
        int endurancePoints = STATS.get("Endurance");
        if (endurancePoints <= 15) {
            bonusRate = 0.01 * endurancePoints;
        } else if (endurancePoints <= 40) {
            bonusRate = (0.01 * 15) + (0.005 * (endurancePoints - 15));
        } else {
            bonusRate = (0.01 * 15) + (0.005 * 25) + (0.002 * (endurancePoints - 40));
        }
        return baseRate + bonusRate;
    }

    public double getHitRate() {
        return fireArm == null ? 0 : fireArm.getHIT_RATE();
    }

    public double getVisceralRate() {
        return fireArm == null ? 0 : fireArm.getVISCERAL_RATE();
    }

    public String getTrickWeaponIcon() {
        String path;
        if (trickWeapon == null) {
            path = "images/empty.png";
        } else {
            path = "images/items/" + trickWeapon.getImage();
        }
        return path;
    }

    public String getFireArmIcon() {
        String path;
        if (fireArm == null) {
            path = "images/empty.png";
        } else {
            path = "images/items/" + fireArm.getImage();
        }
        return path;
    }

    public List<Rune> getRUNE_LIST() {
        return RUNE_LIST;
    }

    public boolean isLastAttackVisceral() {
        return lastAttackIsVisceral;
    }

    public boolean hasFirstDeathHappened() {
        return firstDeathHappened;
    }

    public void firstDeath() {
        firstDeathHappened = true;
        fullRegen();
    }

    public boolean hasItem(Item item) {
        return INVENTORY.hasItem(item);
    }

    public String heal(SoundManager soundManager) {
        StringBuilder s = new StringBuilder();

        if (vialsNumber == 0) {
            s.append("You have no blood vials left.");
        } else {
            if (healthPoints == maxHP) {
                s.append("You don't need to use a blood vial right now.");
            } else {
                healthPoints += (maxHP * 0.4);
                if (healthPoints > maxHP) {
                    healthPoints = maxHP;
                }
                s.append("You use a blood vial, your wounds heal and a dark part of you wants more of it.");
                vialsNumber--;
                soundManager.playSoundEffect("used-bloodvial.wav");
            }
        }
        return s.toString();
    }

    public String healFromItem(double amount, HealingItem item) {
        StringBuilder s = new StringBuilder();
        if (healthPoints == maxHP) {
            s.append("You don't need to use a this right now.");
        } else {
            healthPoints += (maxHP * amount);
            if (healthPoints > maxHP) {
                healthPoints = maxHP;
            }
            s.append("You use the").append(item.getNAME()).append(", your wounds heal and a dark part of you wants more of it.");
        }
        INVENTORY.removeItem(item);
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

    public String equipRune(Rune rune, int position) {
        if (position != -1) { //-1 is when we have less than 3 runes equipped and just want to add one more
            RUNE_LIST.add(position, rune);
            INVENTORY.addItem(RUNE_LIST.remove(position + 1));
        }
        RUNE_LIST.add(rune);
        INVENTORY.removeItem(rune);
        return rune.getEquipText();
    }

    public boolean hasRune(String runeName) {
        boolean hasRune = false;
        for (Rune r : RUNE_LIST) {
            if (RUNE_LIST.isEmpty()) {
                break;
            }
            if (r.getNAME().equals(runeName)) {
                hasRune = true;
                break;
            }
        }
        return hasRune;
    }

    public String switchTrickWeaponState() {
        String txt;
        if (trickWeapon == null) {
            txt = "You don't have any trick weapon equipped";
        } else {
            txt = "You switched your trick weapon's state, check your stats to see the changes";
            trickWeapon.switchMode();
        }
        return txt;
    }

    public void boostDamage(int boost, BoostItem p, SoundManager soundManager) {
        damageBoost = boost;
        boostLeft = 5;
        removeItem(p);
        if (p.getTYPE().equals("fire")) {
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
        if (Math.random() < target.getDodgeRate()) {
            s.append("he avoided the attack.");
        } else {
            int finalDamage = getDamage(); //To avoid losing one instance of boosted damage from the code below
            if (boostLeft != 0) {
                boostLeft--;
                if (boostLeft == 0) {
                    damageBoost = 0;
                    damageType = DamageType.PHYS;
                }
            }
            target.takeDamage(finalDamage);
            s.append("you did ").append(getDamage()).append(" damage !");
            switch (damageType) {
                case FIRE -> soundManager.playSoundEffect("enemy-hit-fire.wav");
                case BOLT -> soundManager.playSoundEffect("enemy-hit-bolt.wav");
                default -> soundManager.playSoundEffect("enemy-hit.wav");
            }
        }
        lastAttackIsVisceral = false;
        return s.toString();
    }

    @Override
    public void takeDamage(int damage) {
        int finalDamage = damage;
        if (hasRune("Lake rune")) {
            finalDamage--;
        }
        if ((healthPoints - finalDamage) < 0) {
            healthPoints = 0;
        } else {
            healthPoints -= finalDamage;
        }
    }

    public void regenAfterVisceral(int amount) {
        if (hasRune("Heir rune")) {
            gainBloodEchoes(500);
        }
        if (healthPoints != 30) {
            healthPoints += amount;
            if (healthPoints > 30) {
                healthPoints = 30;
            }
        }
    }

    public String shoot(Entity target, SoundManager soundManager) {
        StringBuilder s = new StringBuilder();
        if (cantShoot()) {
            s.append("You don't have enough quicksilver bullets left to shoot.");
        } else if (fireArm == null) {
            s.append("You have no gun equipped.");
        } else {
            s.append("You shoot at your enemy, ");
            soundManager.playSoundEffect("gunshot.wav");
            if (Math.random() > fireArm.getHIT_RATE()) { //Shot is missed
                bulletsNumber -= fireArm.getBULLET_USE();
                s.append("you missed.");
            } else {
                if (Math.random() < fireArm.getVISCERAL_RATE()) { //Player performs a visceral attack, regenerating health and cancelling the enemy's attack
                    s.append("you shot at the right timing and perform a visceral attack on him. It regenerates a bit of your life.\n");
                    soundManager.playSoundEffect("visceral-attack.wav");
                    regenAfterVisceral(target.getDamage());
                    target.takeDamage(fireArm.getCurrentDamage() + 10);
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

    public boolean cantShoot() {
        boolean cantShoot = false;
        if (fireArm == null) {
            cantShoot = true;
        } else {
            int usage = fireArm.getBULLET_USE();
            if ((bulletsNumber - usage) < 0) {
                cantShoot = true;
            }
        }
        return cantShoot;
    }

    public Rune getOathRune() {
        return oathRune;
    }
}
