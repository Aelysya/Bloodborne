package bloodborne.entities;

import bloodborne.items.*;
import bloodborne.sounds.SoundManager;

import java.util.*;

public class Hunter extends Entity {

    private int damageBoost;
    private int boostLeft;
    private final Inventory INVENTORY;
    private TrickWeapon trickWeapon;
    private Firearm firearm;
    private int vialsNumber;
    private int bulletsNumber;
    private DamageType damageType;
    private int bloodEchoes;
    private int insight;
    private final List<Rune> RUNE_LIST;
    private Rune oathRune;
    private boolean lastAttackIsVisceral;
    private boolean firstDeathHappened;
    private int maxHP;
    private final Map<String, Integer> STATS;
    private String timeOfNight;

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
        damageType = DamageType.BASE;
        bloodEchoes = 0;
        insight = 0;
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
        timeOfNight = "day";
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

    public void removeOneItemFromStack(Item item) {
        INVENTORY.removeOneItemFromStack(item);
    }

    public void removeStackFromInventory(Item item) {
        INVENTORY.removeStackFromInventory(item);
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

    public void gainInsight(int amount) {
        insight += amount;
    }

    public void spendBloodEchoes(int amount) {
        bloodEchoes -= amount;
    }

    public void spendInsight(int amount) {
        insight -= amount;
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
                soundManager.playSoundEffect("itemsUse/heal.wav");
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
            s.append("You use the ").append(item.getNAME()).append(", your wounds heal and a dark part of you wants more of it.");
        }
        INVENTORY.removeOneItemFromStack(item);
        return s.toString();
    }

    public String equipTrickWeapon(TrickWeapon weapon) {
        if (trickWeapon != null) {
            this.INVENTORY.addItem(trickWeapon);
        }
        INVENTORY.removeOneItemFromStack(weapon);
        trickWeapon = weapon;

        return "You equipped the " + weapon.getNAME() + " as your trick weapon.";
    }

    public String equipFirearm(Firearm weapon) {
        if (firearm != null) {
            this.INVENTORY.addItem(firearm);
        }
        INVENTORY.removeOneItemFromStack(weapon);
        firearm = weapon;

        return "You equipped the " + weapon.getNAME() + " as your firearm.";
    }

    public String equipRune(Rune rune, int position) {
        if (position != -1) { //-1 is when we have less than 3 runes equipped and just want to add one more
            RUNE_LIST.add(position, rune);
            INVENTORY.addItem(RUNE_LIST.remove(position + 1));
        }
        RUNE_LIST.add(rune);
        INVENTORY.removeOneItemFromStack(rune);
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

    public String switchTrickWeaponState(SoundManager soundManager) {
        String txt;
        if (trickWeapon == null) {
            txt = "You don't have any trick weapon equipped";
        } else {
            txt = "You switched your trick weapon's state, check your stats to see the changes";
            trickWeapon.switchMode();
            soundManager.playSoundEffect(trickWeapon.getSwitchSound());
        }
        return txt;
    }

    public void boostDamage(int boost, BoostItem boostItem, SoundManager soundManager) {
        damageBoost = boost;
        boostLeft = 5;
        removeOneItemFromStack(boostItem);
        if (boostItem.getTYPE().equals("fire")) {
            soundManager.playSoundEffect("fire-applied.wav");
            damageType = DamageType.FIRE;
        } else {
            soundManager.playSoundEffect("bolt_applied.wav");
            damageType = DamageType.BOLT;
        }
    }

    public String resolveFightTurn(Entity target, String action, SoundManager soundManager) {
        StringBuilder explanationText = new StringBuilder();
        Enemy enemy = (Enemy) target;
        double finalDodgeRate = calculateDodgeRate(action) + enemy.getAttackSpeed();
        if (action.equals("heal") || action.equals("heal-from-item") || action.equals("use")) { //If the player chose to heal or use an item, its turn ends and the enemy attacks immediately
            explanationText.append(enemy.attack(this, finalDodgeRate, soundManager));
        } else {
            if (action.equals("range") && firearm == null) {
                explanationText.append("You have no firearm equipped !\n");
            } else if (action.equals("range") && cantShoot()) {
                explanationText.append("You don't have enough bullets left to use your firearm !\n");
            } else {
                int finalDamage = calculateDamage(action);
                lastAttackIsVisceral = false;
                if (action.equals("range")) {
                    soundManager.playSoundEffect(firearm.getAttackSound());
                    bulletsNumber -= firearm.getBULLET_USE();
                    if (enemy instanceof MultiEnemy) { //TODO Clean up the code later, repetitions might be reduced ?
                        explanationText.append("You shoot at your enemies, ");
                        int counter = 1;
                        for (Enemy e : ((MultiEnemy) enemy).getEnemies()) {
                            if (e.isDead()) {
                                counter++;
                            } else {
                                if (Math.random() > firearm.getHIT_RATE()) { //Shot is missed
                                    explanationText.append("you missed the n°").append(counter).append(" enemy\n");
                                    counter++;
                                } else {
                                    explanationText.append("you hit the n°").append(counter).append(" enemy and did ");
                                    if (Math.random() < firearm.getVISCERAL_RATE()) { //Player performs a visceral attack, regenerating 20% health and cancelling the enemy's attack
                                        lastAttackIsVisceral = true;
                                        e.stunForXTurns(1);
                                        explanationText.append("it at the right timing and perform a visceral attack on him. It regenerates a bit of your life and ")
                                                .append(e.takeDamage(action, calculateDamage("visceral"), soundManager));
                                        soundManager.playSoundEffect("weapons/visceral-attack.wav");
                                        regenAfterVisceral();
                                    } else { //Classic ranged attack
                                        explanationText.append(e.takeDamage(finalDamage, soundManager)).append(" damage\n");
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        explanationText.append("You shoot at your enemy, ");
                        if (Math.random() > firearm.getHIT_RATE()) { //Shot is missed
                            explanationText.append("but you missed\n");
                        } else {
                            if (Math.random() < firearm.getVISCERAL_RATE()) { //Player performs a visceral attack, regenerating 20% health and cancelling the enemy's attack
                                lastAttackIsVisceral = true;
                                enemy.stunForXTurns(1);
                                explanationText.append("and did it at the right timing and perform a visceral attack on him. It regenerates a bit of your life and ")
                                        .append(enemy.takeDamage(action, calculateDamage("visceral"), soundManager));
                                soundManager.playSoundEffect("weapons/visceral-attack.wav");
                                regenAfterVisceral();
                            } else { //Classic ranged attack
                                explanationText.append(enemy.takeDamage(action, finalDamage, soundManager));
                            }
                        }
                    }
                } else {
                    if (enemy instanceof MultiEnemy) {
                        explanationText.append(enemy.takeDamage(action, finalDamage, soundManager));
                    } else {
                        explanationText.append("You attack your enemy in melee range, ");
                        if (Math.random() < enemy.getDodgeRate()) {
                            explanationText.append("he avoided the attack\n");
                        } else {
                            explanationText.append(enemy.takeDamage(action, finalDamage, soundManager));
                            switch (damageType) {
                                case FIRE -> soundManager.playSoundEffect("enemy-hit-fire.wav");
                                case BOLT -> soundManager.playSoundEffect("enemy-hit-bolt.wav");
                                default -> soundManager.playSoundEffect("enemy-hit.wav");
                            }
                        }
                    }
                }
            }
            if (!enemy.isDead()) {
                explanationText.append(enemy.attack(this, finalDodgeRate, soundManager));
            }
        }
        return explanationText.toString();
    }

    public double calculateDodgeRate(String action) {
        double calculatedDodgeRate = getDodgeRate();
        switch (action) {
            case "heavy-melee" -> calculatedDodgeRate *= 0.75;
            case "charged-melee" -> calculatedDodgeRate *= 0.6;
            case "range", "heal", "heal-from-item" -> calculatedDodgeRate += 0.2;
        }
        return calculatedDodgeRate;
    }

    public int calculateDamage(String action) {
        int calculatedDamage;
        if (action.equals("range")) {
            calculatedDamage = getFirearmDamage() + (int) (calculateBonusDamage("Bloodtinge") * firearm.getBloodScaling());
        } else {
            if (trickWeapon == null) {
            calculatedDamage = 3;
            } else {
                calculatedDamage = getDamage()
                        + (int) (calculateBonusDamage("Bloodtinge") * trickWeapon.getBloodScaling())
                        + (int) (calculateBonusDamage("Arcane") * trickWeapon.getArcaneScaling())
                        + (int) (calculateBonusDamage("Strength") * trickWeapon.getStrengthScaling())
                        + (int) (calculateBonusDamage("Skill") * trickWeapon.getSkillScaling());
            }
            switch (action) {
                case "heavy-melee" -> calculatedDamage *= 1.5;
                case "charged-melee" -> calculatedDamage *= 1.9;
                case "visceral" -> calculatedDamage *= 2.5;
            }
        }
        return calculatedDamage;
    }

    public double calculateBonusDamage(String stat) {
        int statPoints = STATS.get(stat);
        double bonusDamage;
        if (statPoints <= 25) {
            bonusDamage = statPoints;
        } else if (statPoints <= 50) {
            bonusDamage = 25 + (0.5 * (statPoints - 25));
        } else {
            bonusDamage = 25 + (0.5 * 25) + (0.3 * (statPoints - 50));
        }
        return bonusDamage;
    }

    public boolean cantShoot() {
        boolean cantShoot = false;
        if (firearm == null) {
            cantShoot = true;
        } else {
            int usage = firearm.getBULLET_USE();
            if ((bulletsNumber - usage) < 0) {
                cantShoot = true;
            }
        }
        return cantShoot;
    }

    @Override
    public String takeDamage(int damage, SoundManager soundManager) {
        int finalDamage = damage;
        //TODO Add damage reduction due to armor
        if (hasRune("Lake rune")) {
            finalDamage--;
        } //TODO Make the lake runes system to reduce the damage
        if ((healthPoints - finalDamage) < 0) {
            healthPoints = 0;
        } else {
            healthPoints -= finalDamage;
        }
        return "" + finalDamage;
    }

    public void regenAfterVisceral() {
        if (hasRune("Heir rune")) {
            gainBloodEchoes(500);
        }
        if (healthPoints != maxHP) {
            healthPoints += (int) (maxHP * 0.2);
            if (healthPoints > maxHP) {
                healthPoints = maxHP;
            }
        }
    }

    public Rune getOathRune() {
        return oathRune;
    }

    public Item getItemByName(String itemName) {
        itemName = itemName.toLowerCase(Locale.ROOT);
        Item returnedItem;
        if (trickWeapon != null && itemName.equals(trickWeapon.getNAME().toLowerCase(Locale.ROOT))) {
            returnedItem = trickWeapon;
        } else if (firearm != null && itemName.equals(firearm.getNAME().toLowerCase(Locale.ROOT))) {
            returnedItem = firearm;
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

    public int getInsight() {
        return insight;
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

    public Firearm getFirearm() {
        return firearm == null ? null : firearm;
    }

    public int getNumberOfRunes() {
        return RUNE_LIST.size();
    }

    @Override
    public int getDamage() {
        return (trickWeapon == null) ? 3 : trickWeapon.getCurrentDamage() + this.damageBoost;
    }

    public int getFirearmDamage() {
        return (firearm == null) ? 0 : firearm.getCurrentDamage();
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
        return firearm == null ? 0 : firearm.getHIT_RATE();
    }

    public double getVisceralRate() {
        return firearm == null ? 0 : firearm.getVISCERAL_RATE();
    }

    public int getBulletConsumption() {
        return firearm == null ? 0 : firearm.getBULLET_USE();
    }

    public String getTrickWeaponIcon() {
        return trickWeapon == null ? "images/items/empty.png" : trickWeapon.getImage();
    }

    public String getFirearmIcon() {
        return firearm == null ? "images/items/empty.png" : firearm.getImage();
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

    public void setTimeOfNight(String time) {
        timeOfNight = time;
    }

    public void firstDeath() {
        firstDeathHappened = true;
        fullRegen();
    }

    public boolean hasItem(Item item) {
        return INVENTORY.hasItem(item);
    }

    public String getTimeOfNight() {
        return timeOfNight;
    }
}
