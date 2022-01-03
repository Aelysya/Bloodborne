package bloodborne.system;

import bloodborne.GameController;
import bloodborne.entities.*;
import bloodborne.environment.*;
import bloodborne.exceptions.TooFewArgumentsException;
import bloodborne.items.*;
import bloodborne.sounds.SoundManager;
import bloodborne.zone.Place;
import bloodborne.zone.Zone;
import bloodborne.zone.ZoneLoader;
import java.util.Locale;


enum TextAnalyzer {EXPLORATION, FIGHT, RUNE, DEATH, QUIT, START, WIN}

public class Game {

    private final String DEATH_TEXT = """
            ----------------------------
            It seems this world has finally crushed you.
            Your journey ends here for now.
            See you soon, Hunter
            ----------------------------
            """;
    private final Hunter HUNTER;
    private final CommandHandler COMMAND_HANDLER;
    private final GameController CONTROLLER;
    private final SoundManager SOUND_MANAGER;
    private TextAnalyzer analyzer;
    private final Zone ZONE;
    private Entity currentlyFoughtEntity;
    //private String currentZone = "central-yharnam";
    private Rune memorizedRune;

    public Game(GameController Controller) {
        CONTROLLER = Controller;
        COMMAND_HANDLER = new CommandHandler(this);
        SOUND_MANAGER = new SoundManager();
        SOUND_MANAGER.setLoopingSound("ambient-theme.wav");
        HUNTER = new Hunter();
        ZONE = new Zone(HUNTER);
        ZoneLoader.loadZone("legacy-yharnam", ZONE);
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
                case RUNE -> COMMAND_HANDLER.runeTextAnalyzer(line);
                case START -> COMMAND_HANDLER.startTextAnalyzer(line);
                case DEATH -> COMMAND_HANDLER.deathTextAnalyzer(line);
                case WIN -> COMMAND_HANDLER.winTextAnalyzer(line);
                case EXPLORATION -> COMMAND_HANDLER.explorationTextAnalyzer(line);
            }

        } catch (TooFewArgumentsException | InterruptedException e) {
            CONTROLLER.writeInstantly("No target for the command.");
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
        CONTROLLER.writeLetterByLetter(START_TEXT + "\n\n" + ZONE.getCurrentPlace().getDESCRIPTION());
        CONTROLLER.updateDirectionalArrows(ZONE.getCurrentPlace());
    }

    public void death() {
        SOUND_MANAGER.playSoundEffect("you-died.wav");
        CONTROLLER.transitionImage("you-died.jpg");
        CONTROLLER.writeLetterByLetter(DEATH_TEXT + "\nEnter Y to quit");
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
                SOUND_MANAGER.playSoundEffect("change-zone.wav");
                currentPlace = ZONE.getCurrentPlace();
                if (!currentPlace.getSONG_PATH().equals("")) {
                    SOUND_MANAGER.setLoopingSound(currentPlace.getSONG_PATH());
                }
                if (!currentPlace.getIMAGE_PATH().equals("")) {
                    CONTROLLER.transitionImage(currentPlace.getIMAGE_PATH());
                } else {
                    CONTROLLER.transitionImage("placeholder.png");
                }
                CONTROLLER.writeLetterByLetter(currentPlace.getDESCRIPTION());
            } else {
                CONTROLLER.writeInstantly(currentPlace.getExitByName(direction).getConditionFalseText());
            }
        }
        CONTROLLER.updateDirectionalArrows(currentPlace);
    }

    public void teleportFunction(String destination) { //To use the teleport command, use the id of the place you want to go to
        if(ZONE.getPlaceByName(destination) != null){
            ZONE.changePlace(ZONE.getPlaceByName(destination));
            SOUND_MANAGER.playSoundEffect("change-zone.wav");
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
        CONTROLLER.updateDirectionalArrows(ZONE.getCurrentPlace());
    }

    public void lookFunction(String target) {
        Place currentPlace = ZONE.getCurrentPlace();
        Item item = ZONE.getCurrentPlace().getItemByName(target);
        Entity eTarget = ZONE.getCurrentPlace().getNpcByName(target);
        if (currentPlace.getPROPS().get(target) != null) { //If target is a prop
            CONTROLLER.writeLetterByLetter(currentPlace.getPROPS().get(target).lookReaction(HUNTER));
        } else if (item != null) { //If target is an item from the current zone
            if (item.isTaken()){
                if (HUNTER.getItemByName(target) != null) { //If target is an item in the inventory or is one of the weapon equipped
                    item = HUNTER.getItemByName(target);
                    CONTROLLER.writeLetterByLetter(item.getDESCRIPTION());
                } else { //If item was in zone but is now taken and already used, so doesn't exist anymore
                    CONTROLLER.writeInstantly("You try to look at something that doesn't exist.");
                }
            } else {
                CONTROLLER.writeLetterByLetter(item.getDESCRIPTION());
            }
        } else if (HUNTER.getItemByName(target) != null) { //If target is an item in the inventory or is one of the weapon equipped and not in the current zone
            item = HUNTER.getItemByName(target);
            CONTROLLER.writeLetterByLetter(item.getDESCRIPTION());
        }  else if (eTarget != null) { //If target is an enemy
            CONTROLLER.writeLetterByLetter(eTarget.getDESCRIPTION());
        } else if (target.equals("")) { //If no target, describe the current zone
            CONTROLLER.writeLetterByLetter(currentPlace.getDESCRIPTION());
        } else {
            CONTROLLER.writeInstantly("You try to look at something that doesn't exist.");
        }
        CONTROLLER.updateHUD(HUNTER);
    }

    public void activateFunction(String target) {
        Prop prop = ZONE.getCurrentPlace().getPropByName(target);
        if (prop != null) {
            CONTROLLER.writeLetterByLetter(prop.activate(HUNTER));
            if(prop instanceof Container && !((Container) prop).isLooted()){
                SOUND_MANAGER.playSoundEffect("open-chest.wav");
            }
            CONTROLLER.updateHUD(HUNTER);
            if(HUNTER.isDead()){
                death();
            }
        } else {
            CONTROLLER.writeInstantly("You can't activate this.");
        }
    }

    public void useFunction(String object) {
        String objString = object.toLowerCase(Locale.ROOT);
        Item item = HUNTER.getItemByName(object);
        if (item != null) {
            if (objString.equals("amulet")){
                if(ZONE.getCurrentPlace().getNAME().equals("shortcut-house") && !(item.isUsed())){
                    CONTROLLER.writeLetterByLetter(item.use(HUNTER, SOUND_MANAGER));
                } else {
                    CONTROLLER.writeInstantly("You hold the amulet up high but nothing happens.");
                }
            } else {
                CONTROLLER.writeLetterByLetter(item.use(HUNTER, SOUND_MANAGER));
            }
        } else if (objString.equals("blood vial")){
            healFunction();
        } else {
            CONTROLLER.writeInstantly("You try to use something that you don't have or doesn't exist.");
        }
        CONTROLLER.updateHUD(HUNTER);
    }

    public void takeFunction(String itemName) {
        Item item = ZONE.getCurrentPlace().getItemByName(itemName);
        if (item != null) {
            if (!item.isTaken()) {
                CONTROLLER.writeInstantly(item.take(HUNTER));
                SOUND_MANAGER.playSoundEffect("take-item.wav");
            } else {
                CONTROLLER.writeInstantly("You already took this item.");
            }
        } else {
            CONTROLLER.writeInstantly("You try to take something that doesn't exist.");
        }
    }


    public void equipFunction(String object) {
        Item item = HUNTER.getItemByName(object);
        if (item != null) {
            if (item instanceof Weapon){
                if(item instanceof TrickWeapon){
                    CONTROLLER.writeInstantly(HUNTER.equipTrickWeapon((TrickWeapon) item));
                } else {
                    CONTROLLER.writeInstantly(HUNTER.equipFireArm((FireArm) item));
                }
                SOUND_MANAGER.playSoundEffect("weapon-equip.wav");
                CONTROLLER.updateWeapons(HUNTER);
            } else if (item instanceof Rune){
                if(HUNTER.getNumberOfRunes() >= 3){
                    setAnalyzer(TextAnalyzer.RUNE);
                    memorizedRune = (Rune) item;
                    CONTROLLER.writeInstantly("You already have 3 runes equipped, which one do you want to replace ? [1/2/3/cancel]");
                } else {
                    CONTROLLER.writeInstantly(HUNTER.equipRune((Rune) item, -1)); //-1 to just add the rune to the list
                    SOUND_MANAGER.playSoundEffect("weapon-equip.wav");
                    CONTROLLER.updateRunes(HUNTER);
                }
            } else {
                CONTROLLER.writeInstantly("This item is not a weapon nor a rune.");
            }
        } else {
            CONTROLLER.writeInstantly("You try to equip a weapon that you don't have or doesn't exist.");
        }
        CONTROLLER.updateHUD(HUNTER);
    }

    public void runeDecisionFunction(int position){
        CONTROLLER.writeInstantly(HUNTER.equipRune(memorizedRune, position));
        SOUND_MANAGER.playSoundEffect("weapon-equip.wav");
        CONTROLLER.updateRunes(HUNTER);
        setAnalyzer(TextAnalyzer.EXPLORATION);
    }

    public void initiateFightFunction(String target) {
        Entity eTarget = ZONE.getCurrentPlace().getNpcByName(target);
        if (eTarget instanceof Enemy) {
            if (eTarget.isDead()){
                CONTROLLER.writeInstantly("This enemy is already dead.");
            } else {
                CONTROLLER.writeLetterByLetter("You engage the enemy. Now you must fight or flee.");
                setAnalyzer(TextAnalyzer.FIGHT);
                currentlyFoughtEntity = ZONE.getCurrentPlace().getNPCS().get(target);
            }
        } else {
            CONTROLLER.writeInstantly("You try to engage a fight with something that doesn't exist.");
        }
    }

    public void talkFunction(String npc) {
        Place currentPlace = ZONE.getCurrentPlace();
        Friendly friendly = (Friendly) currentPlace.getPROPS().get(npc);
        if (friendly != null) {
            CONTROLLER.writeLetterByLetter(friendly.talk());
        } else {
            CONTROLLER.writeInstantly("You try to speak to someone that doesn't exist.");
        }
    }

    public void inventoryFunction() {
        String s = "Inventory content :\n" +
                "-----------------------------\n" +
                HUNTER.showInventory() +
                "-----------------------------";
        CONTROLLER.writeInstantly(s);
        SOUND_MANAGER.playSoundEffect("inventory-list.wav");
    }

    public void quitFunction() {
        CONTROLLER.writeInstantly("Do you really want to quit ? All progress will be lost [y/n] ");
        setAnalyzer(TextAnalyzer.QUIT);
    }

    public void resolveFight(String decision){
        if (decision.equals("melee")) { //Player's turn
            CONTROLLER.writeInstantly(HUNTER.attack(currentlyFoughtEntity, SOUND_MANAGER));
        } else { //Ranged attack
            CONTROLLER.writeInstantly(HUNTER.shoot(currentlyFoughtEntity, SOUND_MANAGER));
        }
        if(currentlyFoughtEntity.isDead()){
            checkEntityKilledIsBoss(currentlyFoughtEntity);
            CONTROLLER.updateHUD(HUNTER);
            return;
        } else { //Enemy's turn if not dead
            CONTROLLER.writeInstantly(currentlyFoughtEntity.attack(HUNTER, SOUND_MANAGER));
            if (HUNTER.isDead()){
                death();
            }
        }
        CONTROLLER.updateHUD(HUNTER);
    }

    public void checkEntityKilledIsBoss(Entity enemy){
        if (enemy instanceof Boss) {
            CONTROLLER.transitionImage("prey-slaughtered.png");
            SOUND_MANAGER.playSoundEffect("prey-slaughtered.wav");
            SOUND_MANAGER.setLoopingSound("ambient-theme.wav");
            CONTROLLER.writeLetterByLetter("Congratulations ! You managed to kill the Cleric Beast once and for all ! The source of the blood plague is no more and the hunters that came here before you did not die for nothing. But Yharnam will take a long time to recover from this disaster. It is unlikely the survivors will stop consuming blood to heals their diseases but your work gave them some spare time... until next time they need hunters' help...");
            CONTROLLER.writeInstantly("----------------\n\nYour job here is done. Enter Y to quit the game.");
            setAnalyzer(TextAnalyzer.WIN);
        } else {
            CONTROLLER.writeLetterByLetter("You defeated your enemy and survived this fight.");
            if (!HUNTER.isLastAttackVisceral()){
                SOUND_MANAGER.playSoundEffect("enemy-killed.wav");
            }
            Enemy e = (Enemy) enemy;
            CONTROLLER.writeLetterByLetter(e.loot(HUNTER));
            setAnalyzer(TextAnalyzer.EXPLORATION);
        }
    }

    public void switchFunction() {
        CONTROLLER.writeInstantly(HUNTER.switchTrickWeaponState());
        CONTROLLER.updateWeapons(HUNTER);
    }

    public void usePaperInFightFunction(String object) {
        String objString = object.toLowerCase(Locale.ROOT);
        Item item = HUNTER.getItemByName(object);
        if (item != null) {
            if (item instanceof Paper){
                CONTROLLER.writeLetterByLetter(item.use(HUNTER, SOUND_MANAGER));
            } else {
                CONTROLLER.writeInstantly("You can't use this item in the middle of a fight");
            }
        } else if (objString.equals("blood vial")){
            healFunction();
        } else {
            CONTROLLER.writeInstantly("You try to use something that you don't have or doesn't exist.");
        }
        CONTROLLER.updateHUD(HUNTER);
    }

    public void fleeFunction() {
        if (!(currentlyFoughtEntity instanceof Boss)){
            HUNTER.takeDamage(5);
            if (HUNTER.isDead()) {
                CONTROLLER.updateHUD(HUNTER);
                CONTROLLER.writeLetterByLetter("You were too weak too flee and your enemy caught up with you. He executes you.\n" + DEATH_TEXT);
                death();
            } else {
                CONTROLLER.updateHUD(HUNTER);
                CONTROLLER.writeLetterByLetter("You run away from the fight but your enemy won't let you do it easily, you take some damage as you flee pitifully.");
            }
            setAnalyzer(TextAnalyzer.EXPLORATION);
        } else {
            CONTROLLER.writeInstantly("You can't flee from a boss fight. Kill or be killed.");
        }
    }

    public void healFunction() {
        CONTROLLER.writeLetterByLetter(HUNTER.heal(SOUND_MANAGER));
        CONTROLLER.updateHUD(HUNTER);
    }


    
    //Deprecated methods
/*
    public void attackFunction() {
        CONTROLLER.writeInstantly("You attack your enemy.");
        int dmg = HUNTER.getDamage(); //In case of last buffed attack, if not present it only shows base weapon damage while boosted damage are dealt
        switch(HUNTER.getDamageType()){
            case "FIRE" -> SOUND_MANAGER.playSoundEffect("enemy-hit-fire.wav");
            case "BOLT" -> SOUND_MANAGER.playSoundEffect("enemy-hit-bolt.wav");
            default -> SOUND_MANAGER.playSoundEffect("enemy-hit.wav");
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
        if(HUNTER.cantShoot()){
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
    }*/
}
