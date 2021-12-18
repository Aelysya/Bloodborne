package bloodborne.system;

import bloodborne.GameController;
import bloodborne.entities.*;
import bloodborne.environment.*;
import bloodborne.exceptions.TooFewArgumentsException;
import bloodborne.items.FireArm;
import bloodborne.items.Item;
import bloodborne.items.TrickWeapon;
import bloodborne.items.Weapon;
import bloodborne.sounds.SoundManager;
import bloodborne.zone.Place;
import bloodborne.zone.Zone;
import bloodborne.zone.ZoneLoader;

import javax.swing.*;
import java.util.Locale;

import static java.lang.Thread.sleep;

enum TextAnalyzer {EXPLORATION, FIGHT, DEATH, QUIT, START, WIN}

public class Game {

    private final String DEATH_TEXT = """
            ----------------------------
            It seems this world has finally crushed you.
            Your journey ends here for now.
            See you soon, Hunter
            ----------------------------
            """;

    private String zoneName = "centralYharnam"; //TODO make the player choose the zone at beginning
    private final Hunter HUNTER;
    private final CommandHandler COMMAND_HANDLER;
    private final GameController CONTROLLER;
    private final SoundManager SOUND_MANAGER;
    private final Zone ZONE;
    private Entity currentlyFoughtEntity;
    private TextAnalyzer analyzer;

    public Game(GameController Controller) {
        CONTROLLER = Controller;
        COMMAND_HANDLER = new CommandHandler(this);
        SOUND_MANAGER = new SoundManager();
        SOUND_MANAGER.setLoopingSound("ambient_theme.wav");
        HUNTER = new Hunter();
        ZONE = new Zone(HUNTER);
        ZoneLoader.loadZone(zoneName, ZONE);
        currentlyFoughtEntity = null;
        analyzer = TextAnalyzer.START;
    }

    public void writeInstantly(String txt){
        System.out.println(txt);
        CONTROLLER.writeInstantly(txt);
    }

    public void analyseText(String textLine) {
        System.out.println(textLine);
        String line = textLine.toLowerCase(Locale.ROOT);

        try {
            switch (analyzer) {
                case QUIT -> COMMAND_HANDLER.quitTextAnalyzer(line);
                case FIGHT -> COMMAND_HANDLER.fightTextAnalyzer(line);
                case START -> COMMAND_HANDLER.startTextAnalyzer(line);
                case DEATH -> COMMAND_HANDLER.deathTextAnalyzer(line);
                case WIN -> COMMAND_HANDLER.winTextAnalyzer(line);
                case EXPLORATION -> COMMAND_HANDLER.explorationTextAnalyzer(line);
            }

        } catch (TooFewArgumentsException | InterruptedException e) {
            writeInstantly("No target for the command.");
        }
    }

    public void setAnalyzer(TextAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void startGame() {
        setAnalyzer(TextAnalyzer.EXPLORATION);
        CONTROLLER.transitionImage(ZONE.getCurrentPlace().getIMAGE_PATH());
        String START_TEXT = """
                As you open your eyes, memories flood into you head and you remember everything...
                Two days ago, three hunters were sent to cleanse the city of Yharnam of people corrupted by the blood plague and eliminate it's source.
                They still haven't returned and are therefore considered dead. It is now your duty to finish their work. When you arrived you were seriously injured in the accident with your transport.
                You now wake up in what seems to be a medical clinic. You have lost all your equipment, including your weapons...
                You don't have time to wonder why you are still alive or how you ended up there. A big monster is still roaming the streets of Yharnam and you have to deal with it.
                You quickly grab the two blood vials left on the beside table next to you and get up.
                """;
        CONTROLLER.writeInstantly(START_TEXT + "\n\n" + ZONE.getCurrentPlace().getDESCRIPTION());
    }//TODO change to letter by letter printing

    public void death() {
        SOUND_MANAGER.playSoundEffect("you_died.wav");
        CONTROLLER.transitionImage("you_died.jpg");
        CONTROLLER.writeInstantly(DEATH_TEXT + "\nEnter Y to quit");
        setAnalyzer(TextAnalyzer.DEATH);
    }

    public void muteGame() {
        SOUND_MANAGER.mute();
    }

    public void unMuteGame() {
        SOUND_MANAGER.unMute();
    }

    public void goFunction(String direction) {
        Place currentPlace = ZONE.getCurrentPlace();
        if (!currentPlace.getEXITS().containsKey(direction)) {
            CONTROLLER.writeInstantly("There is no such exit here.");
        } else {
            if (currentPlace.getExitByName(direction).isTraversable()) {
                ZONE.changePlace(currentPlace.getExitByName(direction).getOUT());
                SOUND_MANAGER.playSoundEffect("change_zone.wav");
                currentPlace = ZONE.getCurrentPlace();
                if (!currentPlace.getSONG_PATH().equals("")) {
                    SOUND_MANAGER.setLoopingSound(currentPlace.getSONG_PATH());
                }
                if (!currentPlace.getIMAGE_PATH().equals("")) {
                    CONTROLLER.transitionImage(currentPlace.getIMAGE_PATH());
                } else {
                    CONTROLLER.transitionImage("placeholder.png");
                }
                CONTROLLER.writeInstantly(currentPlace.getDESCRIPTION());
            } else {
                CONTROLLER.writeInstantly(currentPlace.getExitByName(direction).getConditionFalseText());
            }
        }
    }//TODO change to letter by letter printing

    public void teleportFunction(String destination) {
        if(ZONE.getPlaceByName(destination) != null){
            ZONE.changePlace(ZONE.getPlaceByName(destination));
            SOUND_MANAGER.playSoundEffect("change_zone.wav");
            Place currentPlace = ZONE.getCurrentPlace();
            if (!currentPlace.getSONG_PATH().equals("")) {
                SOUND_MANAGER.setLoopingSound(currentPlace.getSONG_PATH());
            }
            if (!currentPlace.getIMAGE_PATH().equals("")) {
                CONTROLLER.transitionImage(currentPlace.getIMAGE_PATH());
            } else {
                CONTROLLER.transitionImage("placeholder.png");
            }
            CONTROLLER.writeInstantly(currentPlace.getDESCRIPTION());
        } else {
            CONTROLLER.writeInstantly("Place id invalid");
        }
    }

    public void lookFunction(String target) {
        Place currentPlace = ZONE.getCurrentPlace();
        Item item = ZONE.getCurrentPlace().getItemByName(target);
        Entity eTarget = ZONE.getCurrentPlace().getNpcByName(target);
        if (currentPlace.getPROPS().get(target) != null) { //If target is a prop
            CONTROLLER.writeInstantly(currentPlace.getPROPS().get(target).lookReaction(HUNTER));
        } else if (item != null) { //If target is an item from the current zone
            if (item.isTaken()){
                if (HUNTER.getItemByName(target) != null) { //If target is an item in the inventory or is one of the weapon equipped
                    item = HUNTER.getItemByName(target);
                    CONTROLLER.writeInstantly(item.getDESCRIPTION());
                } else { //If item was in zone but is now taken and already used, so doesn't exist anymore
                    CONTROLLER.writeInstantly("You try to look at something that doesn't exist.");
                }
            } else {
                CONTROLLER.writeInstantly(item.getDESCRIPTION());
            }
        } else if (HUNTER.getItemByName(target) != null) { //If target is an item in the inventory or is one of the weapon equipped and not in the current zone
            item = HUNTER.getItemByName(target);
            CONTROLLER.writeInstantly(item.getDESCRIPTION());
        }  else if (eTarget != null) { //If target is an enemy
            CONTROLLER.writeInstantly(eTarget.getDESCRIPTION());
        } else if (target.equals("")) { //If no target, describe the current zone
            CONTROLLER.writeInstantly(currentPlace.getDESCRIPTION());
        } else {
            CONTROLLER.writeInstantly("You try to look at something that doesn't exist.");
        }
        CONTROLLER.updateHUD(HUNTER);
    }//TODO change to letter by letter printing

    public void activateFunction(String target) {
        Prop prop = ZONE.getCurrentPlace().getPropByName(target);
        if (prop != null) {
            CONTROLLER.writeInstantly(prop.activate(HUNTER));
            if(prop instanceof Container && !((Container) prop).isLooted()){
                SOUND_MANAGER.playSoundEffect("open_chest.wav");
            }
            CONTROLLER.updateHUD(HUNTER);
            if(HUNTER.isDead()){
                death();
            }
        } else {
            CONTROLLER.writeInstantly("You can't activate this.");
        }
    }//TODO change to letter by letter printing

    public void useFunction(String object) {
        String objString = object.toLowerCase(Locale.ROOT);
        Item item = HUNTER.getItemByName(object);
        if (item != null) {
            if (objString.equals("amulet")){
                if(ZONE.getCurrentPlace().getNAME().equals("shortcut-house") && !(item.isUsed())){
                    CONTROLLER.writeInstantly(item.use(HUNTER, SOUND_MANAGER));
                } else {
                    CONTROLLER.writeInstantly("You hold the amulet up high but nothing happens.");
                }
            } else {
                CONTROLLER.writeInstantly(item.use(HUNTER, SOUND_MANAGER));
            }
        } else if (objString.equals("blood vial")){
            healFunction();
        } else {
            CONTROLLER.writeInstantly("You try to use something that you don't have or doesn't exist.");
        }
        CONTROLLER.updateHUD(HUNTER);
    }//TODO change to letter by letter printing

    public void takeFunction(String itemName) {
        Item item = ZONE.getCurrentPlace().getItemByName(itemName);
        if (item != null) {
            if (!item.isTaken()) {
                CONTROLLER.writeInstantly(item.take(HUNTER));
                SOUND_MANAGER.playSoundEffect("take_item.wav");
            } else {
                CONTROLLER.writeInstantly("You already took this item.");
            }
        } else {
            CONTROLLER.writeInstantly("You try to take something that doesn't exist.");
        }
    }//TODO change to letter by letter printing


    public void equipFunction(String weapon) {
        if (HUNTER.getItemByName(weapon) != null) {
            try {
                Weapon w = (Weapon) HUNTER.getItemByName(weapon);
                if(w instanceof TrickWeapon){
                    CONTROLLER.writeInstantly(HUNTER.equipTrickWeapon((TrickWeapon) w));
                } else {
                    CONTROLLER.writeInstantly(HUNTER.equipFireArm((FireArm) w));
                }
                SOUND_MANAGER.playSoundEffect("weapon_equip.wav");
                CONTROLLER.updateWeapons(HUNTER);
            } catch (ClassCastException e) {
                CONTROLLER.writeInstantly("This item is not a weapon");
            }
        } else {
            CONTROLLER.writeInstantly("You try to equip a weapon that you don't have or doesn't exist.");
        }
        CONTROLLER.updateHUD(HUNTER);
    }//TODO change to letter by letter printing

    public void initiateFightFunction(String target) {
        Entity eTarget = ZONE.getCurrentPlace().getNpcByName(target);
        if (eTarget instanceof Enemy) {
            CONTROLLER.writeInstantly("You engage the enemy. Now you must fight or flee.");
            setAnalyzer(TextAnalyzer.FIGHT);
            currentlyFoughtEntity = ZONE.getCurrentPlace().getNPCS().get(target);
        } else {
            CONTROLLER.writeInstantly("You try to engage a fight with something that doesn't exist.");
        }
    }//TODO change to letter by letter printing

    public void talkFunction(String npc) {
        Place currentPlace = ZONE.getCurrentPlace();
        Friendly friendly = (Friendly) currentPlace.getPROPS().get(npc);
        if (friendly != null) {
            CONTROLLER.writeInstantly(friendly.talk());
        } else {
            CONTROLLER.writeInstantly("You try to speak to someone that doesn't exist.");
        }
    }//TODO change to letter by letter printing

    public void statusFunction() {
        String s = "-----------------------------\n" +
                HUNTER.showInventory() +
                "-----------------------------";
        CONTROLLER.writeInstantly(s);
        SOUND_MANAGER.playSoundEffect("inventory_list.wav");
    }

    public void quitFunction() {
        writeInstantly("Do you really want to quit ? All progress will be lost [y/n] ");
        setAnalyzer(TextAnalyzer.QUIT);
    }

    public void resolveFight(String decision){
        //Player's turn
        if (decision.equals("melee")) {
            CONTROLLER.writeInstantly("You attack your enemy.");
            if (Math.random() < currentlyFoughtEntity.getDodgeRate()) { //The enemy dodges the attack
                CONTROLLER.writeInstantly("Your enemy avoided the attack.");
            } else { //The enemy takes the attack
                switch(HUNTER.getDamageType()){ //Sound effect depending on the current damage type of the player
                    case "FIRE" -> SOUND_MANAGER.playSoundEffect("enemy_hit_fire.wav");
                    case "BOLT" -> SOUND_MANAGER.playSoundEffect("enemy_hit_bolt.wav");
                    default -> SOUND_MANAGER.playSoundEffect("enemy_hit.wav");
                }
                CONTROLLER.writeInstantly("You did " + HUNTER.getDamage() + " damage to the monster !");
                if(HUNTER.attack(currentlyFoughtEntity)){ //The enemy is dead
                    checkEntityKilledIsBoss(currentlyFoughtEntity, false);
                    CONTROLLER.updateHUD(HUNTER);
                    return;
                }
                CONTROLLER.updateHUD(HUNTER);
            }
        } else { //Ranged attack
            if(!HUNTER.canShoot()){
                CONTROLLER.writeInstantly("You don't have enough quicksilver bullets left to shoot.");
                return;
            } else if (HUNTER.getFireArm() == null){
                CONTROLLER.writeInstantly("You have no gun equipped.");
                return;
            } else {
                CONTROLLER.writeInstantly("You shoot at your enemy.");
                SOUND_MANAGER.playSoundEffect("gunshot.wav");
                if (Math.random() > HUNTER.getFireArm().getHIT_RATE()){ //Player misses the shot
                    CONTROLLER.writeInstantly("You failed to hit your enemy.");
                    HUNTER.wasteBullet();
                } else {
                    if (Math.random() < HUNTER.getFireArm().getVISCERAL_RATE()){ //Player performs a visceral attack, regenerating health and cancelling the enemy's attack
                        CONTROLLER.writeInstantly("You shot your enemy at the right timing and perform a visceral attack on him. It regenerates a bit of your life.");
                        SOUND_MANAGER.playSoundEffect("visceral_attack.wav");
                        HUNTER.regenAfterVisceral(currentlyFoughtEntity.getDamage());
                        int dmgDone = HUNTER.getFireArmDamage()+10;
                        currentlyFoughtEntity.takeDamage(10);
                        HUNTER.shoot(currentlyFoughtEntity);
                        CONTROLLER.writeInstantly("You did " + dmgDone + " damage to the monster !");
                        if (currentlyFoughtEntity.isDead()) { //The enemy is dead
                            checkEntityKilledIsBoss(currentlyFoughtEntity, true);
                            CONTROLLER.updateHUD(HUNTER);
                            return;
                        }
                        CONTROLLER.updateHUD(HUNTER);
                        return; //To prevent the enemy from attacking (if not dead)
                    } else { //Player performs a classic ranged attack
                        CONTROLLER.writeInstantly("You did " + HUNTER.getFireArmDamage() + " damage to the monster !");
                        if(HUNTER.shoot(currentlyFoughtEntity)){ //Enemy is dead
                            checkEntityKilledIsBoss(currentlyFoughtEntity, false);
                            CONTROLLER.updateHUD(HUNTER);
                            return;
                        }
                        CONTROLLER.updateHUD(HUNTER);
                    }
                }
            }
        }
        //Enemy's turn if not dead
        if (Math.random() < HUNTER.getDodgeRate()){ //Player dodges the attack
            CONTROLLER.writeInstantly("You avoided your enemy's attack.");
        } else {
            CONTROLLER.writeInstantly("You took " + currentlyFoughtEntity.getDamage() + " damage !");
            if (currentlyFoughtEntity.attack(HUNTER)){ //Player is dead
                death();
                CONTROLLER.updateHUD(HUNTER);
                return;
            }
            CONTROLLER.updateHUD(HUNTER);
        }
    }

    public void checkEntityKilledIsBoss(Entity enemy, boolean fromVisceral){
        if (!(enemy instanceof Boss)) {
            CONTROLLER.writeInstantly("You defeated your enemy and survived this fight.");
            if (!fromVisceral) {
                SOUND_MANAGER.playSoundEffect("enemy_killed.wav");
            }
            setAnalyzer(TextAnalyzer.EXPLORATION);
        } else {
            CONTROLLER.transitionImage("prey_slaughtered.png");
            SOUND_MANAGER.playSoundEffect("prey_slaughtered.wav");
            SOUND_MANAGER.setLoopingSound("ambient_theme.wav");
            CONTROLLER.writeInstantly("Congratulations ! You managed to kill the Cleric Beast once and for all ! The source of the blood plague is no more and the hunters that came here before you did not die for nothing. But Yharnam will take a long time to recover from this disaster. It is unlikely the survivors will stop consuming blood to heals their diseases but your work gave them some spare time... until next time they need hunters' help...");
            CONTROLLER.writeInstantly("----------------\n\nYour job here is done. Enter Y to quit the game.");
            setAnalyzer(TextAnalyzer.WIN);
        }
    }//TODO change to letter by letter printing

    public void switchFunction() {
        CONTROLLER.writeInstantly(HUNTER.switchTrickWeaponState());
        CONTROLLER.updateHUD(HUNTER);
    }

    public void fleeFunction() {
        if (HUNTER.takeDamage(5)) {
            CONTROLLER.updateHUD(HUNTER);
            CONTROLLER.writeInstantly("You were too weak too flee and your enemy caught up with you. He executes you.\n" + DEATH_TEXT);
            death();
        } else {
            CONTROLLER.updateHUD(HUNTER);
            CONTROLLER.writeInstantly("You run away from the fight but your enemy won't let you do it easily, you take some damage as you flee pitifully.");
        }
        setAnalyzer(TextAnalyzer.EXPLORATION);
    }//TODO change to letter by letter printing

    public void healFunction() {
        CONTROLLER.writeInstantly(HUNTER.heal(SOUND_MANAGER));
        CONTROLLER.updateHUD(HUNTER);
    }//TODO change to letter by letter printing


    
    //Deprecated methods

    public void attackFunction() {
        CONTROLLER.writeInstantly("You attack your enemy.");
        int dmg = HUNTER.getDamage(); //In case of last buffed attack, if not present it only shows base weapon damage while boosted damage are dealt
        switch(HUNTER.getDamageType()){
            case "FIRE" -> SOUND_MANAGER.playSoundEffect("enemy_hit_fire.wav");
            case "BOLT" -> SOUND_MANAGER.playSoundEffect("enemy_hit_bolt.wav");
            default -> SOUND_MANAGER.playSoundEffect("enemy_hit.wav");
        }
        if (!HUNTER.attack(currentlyFoughtEntity)) {
            if (currentlyFoughtEntity.attack(HUNTER)) {
                CONTROLLER.updateHUD(HUNTER);
                death();
                return;
            }
            CONTROLLER.updateHUD(HUNTER);
            String s = "You did " + dmg + " damage to the monster !\nYou took " + currentlyFoughtEntity.getDamage() +
                    " damage !";
            CONTROLLER.writeInstantly(s);
        } else {
            checkEntityKilledIsBoss(currentlyFoughtEntity, false);
            CONTROLLER.updateHUD(HUNTER);
        }
    }

    public void shootFunction() {
        if(!HUNTER.canShoot()){
            CONTROLLER.writeInstantly("You don't have enough quicksilver bullets left to shoot.");
        } else if (HUNTER.getFireArm() == null){
            CONTROLLER.writeInstantly("You have no gun equipped.");
        } else {
            CONTROLLER.writeInstantly("You shoot with your gun.");
            SOUND_MANAGER.playSoundEffect("gunshot.wav");
            if (!HUNTER.shoot(currentlyFoughtEntity)) {
                if (currentlyFoughtEntity.attack(HUNTER)) {
                    CONTROLLER.updateHUD(HUNTER);
                    death();
                    return;
                }
                CONTROLLER.updateHUD(HUNTER);
                String s = "You did " + HUNTER.getFireArmDamage() + " damage to the monster !\nYou took " + currentlyFoughtEntity.getDamage() +
                        " damage !";
                CONTROLLER.writeInstantly(s);
            } else {
                checkEntityKilledIsBoss(currentlyFoughtEntity, false);
                CONTROLLER.updateHUD(HUNTER);
            }
        }
    }
}
