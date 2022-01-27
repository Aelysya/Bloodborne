package bloodborne.system;

import bloodborne.GameController;
import bloodborne.entities.Boss;
import bloodborne.entities.Enemy;
import bloodborne.entities.Entity;
import bloodborne.entities.Hunter;
import bloodborne.environment.Container;
import bloodborne.environment.Headstone;
import bloodborne.npcs.NPC;
import bloodborne.environment.Prop;
import bloodborne.exceptions.TooFewArgumentsException;
import bloodborne.items.*;
import bloodborne.sounds.SoundManager;
import bloodborne.world.Place;
import bloodborne.world.World;
import bloodborne.world.WorldLoader;

import java.util.Locale;


public class Game {

    private final Hunter HUNTER;
    private final CommandHandler COMMAND_HANDLER;
    private final GameController CONTROLLER;
    private final SoundManager SOUND_MANAGER;
    private final ActionListener ACTION_LISTENER;
    private TextAnalyzer analyzer;
    private final World WORLD;
    private Place lastLanternPlace;
    private Entity currentlyFoughtEntity;
    private Rune memorizedRune;

    public Game(GameController Controller) {
        HUNTER = new Hunter();
        COMMAND_HANDLER = new CommandHandler(this);
        CONTROLLER = Controller;
        CONTROLLER.setHunter(HUNTER);
        SOUND_MANAGER = new SoundManager();
        SOUND_MANAGER.setLoopingSound("central-yharnam.wav");
        analyzer = TextAnalyzer.START;
        WORLD = new World(HUNTER);
        lastLanternPlace = WORLD.getPlaceById("clinic");
        currentlyFoughtEntity = null;
        ACTION_LISTENER = new ActionListener(SOUND_MANAGER, HUNTER, WORLD, COMMAND_HANDLER, this);
        WorldLoader.loadZone("central-yharnam", WORLD);
    }

    public void writeInstantly(String txt) {
        System.out.println(txt);
        CONTROLLER.writeInstantly(txt);
    }

    public void analyseText(String textLine) {
        System.out.println(textLine);
        String line = textLine.toLowerCase(Locale.ROOT);
        try {
            switch (analyzer) {
                case START -> COMMAND_HANDLER.startTextAnalyzer(line);
                case EXPLORATION -> COMMAND_HANDLER.explorationTextAnalyzer(line);
                case FIGHT -> COMMAND_HANDLER.fightTextAnalyzer(line);
                case DREAM_BACK -> COMMAND_HANDLER.dreamBackTextAnalyzer(line);
                case YHARNAM_HEADSTONE -> COMMAND_HANDLER.yharnamHeadstoneTextAnalyzer(line);
                //case FRONTIER_HEADSTONE -> COMMAND_HANDLER.frontierHeadstoneTextAnalyzer(line);
                //case UNSEEN_HEADSTONE -> COMMAND_HANDLER.unseenHeadstoneTextAnalyzer(line);
                case RUNE -> COMMAND_HANDLER.runeTextAnalyzer(line);
                case DEATH -> COMMAND_HANDLER.deathTextAnalyzer(line);
                //case LEVEL_UP -> COMMAND_HANDLER.levelUpTextAnalyzer(line);
                //case MERCHANT -> COMMAND_HANDLER.merchantTextAnalyzer(line);
                //case UPGRADE -> COMMAND_HANDLER.upgradeTextAnalyzer(line);
                case QUIT_FROM_DEATH -> COMMAND_HANDLER.quitFromDeathTextAnalyzer(line);
                case QUIT -> COMMAND_HANDLER.quitTextAnalyzer(line);
            }
        } catch (TooFewArgumentsException e) {
            CONTROLLER.writeInstantly("No target for the command.");
        }
    }

    public void setAnalyzer(TextAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void startGame() {
        setAnalyzer(TextAnalyzer.EXPLORATION);
        CONTROLLER.transitionImage(WORLD.getCurrentPlace().getIMAGE());
        String START_TEXT = """
                """;
        //CONTROLLER.writeLetterByLetter(WORLD.getCurrentPlace().getDESCRIPTION());
        CONTROLLER.updateDirectionalArrows(WORLD.getCurrentPlace());
        CONTROLLER.updateRunes();
    }

    public void death() {
        String deathText = """
                ----------------------------
                You died, do you want to reappear at the last lantern ? [Y/N]
                ----------------------------
                """;
        SOUND_MANAGER.playSoundEffect("you-died.wav");
        if (!HUNTER.hasFirstDeathHappened()) {
            firstDeath();
        } else {
            CONTROLLER.transitionImage("you-died.jpg");
            CONTROLLER.writeLetterByLetter(deathText);
            setAnalyzer(TextAnalyzer.DEATH);
        }
    }

    public void firstDeath() {
        String explanationText = """
                You have just died, but you wake up a few seconds later in a strange place lit by the moonlight.
                Everything is calm, you feel no danger here, you are perfectly safe.
                At the bottom of the stone staircase is a woman, on closer inspection you realize that she is in fact a human-sized doll.
                The stairs lead to a workshop. As you climb the stairs for the first time, messengers appear and hand you gifts.
                You have to choose a trick weapon between the SAW CLEAVER (T), the HUNTER AXE (T) and the THREADED CANE (T), and a firearm between the HUNTER PISTOL (T) and the HUNTER BLUNDERBUSS (T).
                """;
        HUNTER.firstDeath();
        setAnalyzer(TextAnalyzer.EXPLORATION);
        currentlyFoughtEntity = null;
        CONTROLLER.updateHUD();
        CONTROLLER.deathTransition();
        WORLD.changePlace(WORLD.getPlaceById("hunter's-dream"));
        CONTROLLER.writeLetterByLetter(explanationText);
        SOUND_MANAGER.setLoopingSound(WORLD.getCurrentPlace().getSONG());
        CONTROLLER.updateDirectionalArrows(WORLD.getCurrentPlace());
    }

    public void warpBackToLastLantern() {
        currentlyFoughtEntity = null;
        setAnalyzer(TextAnalyzer.EXPLORATION);
        HUNTER.fullRegen();
        CONTROLLER.updateHUD();
        teleportFunction(lastLanternPlace.getID());
    }

    public void writeHeadstoneText(String headstoneName) {
        CONTROLLER.writeLetterByLetter(WORLD.generateHeadstoneText(headstoneName));
    }

    public void checkIfWarpFromDreamPossible(String destinationID) {
        if (WORLD.getPlaceById(destinationID).hasBeenVisited()) {
            teleportFunction(destinationID);
            setAnalyzer(TextAnalyzer.EXPLORATION);
        } else {
            writeInstantly("This destination either doesn't exist or has not yet been discovered. Choose another destination.");
        }
    }

    public void muteGame() {
        SOUND_MANAGER.mute();
    }

    public void unMuteGame() {
        SOUND_MANAGER.unMute();
    }

    public void goFunction(String direction) {
        Place currentPlace = WORLD.getCurrentPlace();
        if (currentPlace.hasLantern() && direction.equals("hunter's-dream")) {
            teleportFunction("hunter's-dream");
        } else if (!currentPlace.getEXITS().containsKey(direction)) {
            CONTROLLER.writeInstantly("There is no such exit here.");
        } else {
            if (currentPlace.getExitByName(direction).isTraversable()) {
                WORLD.changePlace(currentPlace.getExitByName(direction).getOUT());
                SOUND_MANAGER.playSoundEffect("change-place.wav");
                currentPlace = WORLD.getCurrentPlace();
                if (!currentPlace.getSONG().equals("")) {
                    SOUND_MANAGER.setLoopingSound(currentPlace.getSONG());
                }
                if (!currentPlace.getIMAGE().equals("")) {
                    CONTROLLER.transitionImage(currentPlace.getIMAGE());
                } else {
                    CONTROLLER.transitionImage("placeholder.png");
                }
                CONTROLLER.writeLetterByLetter(currentPlace.getDESCRIPTION());
                if (currentPlace.hasLantern()) {
                    lastLanternPlace = currentPlace;
                }
                ACTION_LISTENER.goListener();
            } else {
                CONTROLLER.writeInstantly(currentPlace.getExitByName(direction).getConditionFalseText());
            }
        }
        CONTROLLER.updateDirectionalArrows(currentPlace);
    }

    public void teleportFunction(String destination) { //To use the teleport command, use the id of the place you want to go to
        if (WORLD.getPlaceById(destination) != null) {
            if (destination.equals("hunter's-dream")) {
                WORLD.changePlace(WORLD.getPlaceById("hunter's-dream"));
            } else {
                WORLD.changePlace(WORLD.getPlaceById(destination));
                SOUND_MANAGER.playSoundEffect("change-place.wav");
            }
            Place currentPlace = WORLD.getCurrentPlace();
            if (!currentPlace.getSONG().equals("")) {
                SOUND_MANAGER.setLoopingSound(currentPlace.getSONG());
            }
            if (!currentPlace.getIMAGE().equals("")) {
                CONTROLLER.transitionImage(currentPlace.getIMAGE());
            } else {
                CONTROLLER.transitionImage("placeholder.png");
            }
            CONTROLLER.writeInstantly(currentPlace.getDESCRIPTION());
            if (currentPlace.hasLantern()) {
                lastLanternPlace = currentPlace;
            }
            ACTION_LISTENER.teleportListener();
        } else {
            CONTROLLER.writeInstantly("Place id invalid");
        }
        CONTROLLER.updateDirectionalArrows(WORLD.getCurrentPlace());
    }

    public void lookFunction(String target) {
        Place currentPlace = WORLD.getCurrentPlace();
        Item item = currentPlace.getItemByName(target);
        Prop prop = currentPlace.getPROPS().get(target);
        Entity eTarget = WORLD.getCurrentPlace().getEnemyByName(target);
        if (prop != null) { //If target is a prop
            CONTROLLER.writeLetterByLetter(prop.look(HUNTER));
            if (prop instanceof Headstone){
                writeHeadstoneText(((Headstone) prop).getHeadstoneSimpleName());
            }
            ACTION_LISTENER.lookListener(currentPlace.getPROPS().get(target));
        } else if (item != null) { //If target is an item from the current place
            if (item.isTaken()) {
                if (HUNTER.getItemByName(target) != null) { //If target is an item in the inventory or is one of the weapon equipped
                    item = HUNTER.getItemByName(target);
                    CONTROLLER.writeLetterByLetter(item.getDESCRIPTION());
                } else { //If item was in place but is now taken and already used, so doesn't exist anymore
                    CONTROLLER.writeInstantly("You try to look at something that doesn't exist.");
                }
            } else {
                CONTROLLER.writeLetterByLetter(item.getDESCRIPTION());
            }
        } else if (HUNTER.getItemByName(target) != null) { //If target is an item in the inventory or is one of the weapon equipped and not in the current place
            item = HUNTER.getItemByName(target);
            CONTROLLER.writeLetterByLetter(item.getDESCRIPTION());
        } else if (eTarget != null) { //If target is an enemy
            CONTROLLER.writeLetterByLetter(eTarget.getDESCRIPTION());
        } else if (target.equals("") || target.equals("around")) { //If no target, describe the current place
            CONTROLLER.writeLetterByLetter(currentPlace.getDESCRIPTION());
        } else {
            CONTROLLER.writeInstantly("You try to look at something that doesn't exist.");
        }
        CONTROLLER.updateHUD();
    }

    public void activateFunction(String target) {
        Prop prop = WORLD.getCurrentPlace().getPropByName(target);
        if (prop != null) {
            CONTROLLER.writeLetterByLetter(prop.activate(HUNTER));
            if (prop instanceof Container && !prop.hasBeenActivated()) {
                SOUND_MANAGER.playSoundEffect("open-chest.wav");
            }
            CONTROLLER.updateHUD();
            ACTION_LISTENER.activateListener();
            if (HUNTER.isDead()) {
                death();
            }
        } else {
            CONTROLLER.writeInstantly("You can't activate this.");
        }
        CONTROLLER.updateInventory("default");
    }

    public void talkFunction(String targetNpc) {
        NPC npc = WORLD.getCurrentPlace().getNpcByName(targetNpc);
        if (npc != null) {
            CONTROLLER.writeLetterByLetter(npc.talk(HUNTER));
            ACTION_LISTENER.talkListener();
        } else {
            CONTROLLER.writeInstantly("You try to speak to someone that doesn't exist.");
        }
        CONTROLLER.updateInventory("default");
    }

    public void useFunction(String object) {
        String objString = object.toLowerCase(Locale.ROOT);
        Item item = HUNTER.getItemByName(object);
        if (item != null) {
            CONTROLLER.writeLetterByLetter(item.use(HUNTER, SOUND_MANAGER));
            if (currentlyFoughtEntity != null) {
                CONTROLLER.writeInstantly(currentlyFoughtEntity.attack(HUNTER, SOUND_MANAGER));
                if (HUNTER.isDead()) {
                    death();
                }
                ACTION_LISTENER.resolveFightListener();
            }
            CONTROLLER.updateHUD();
            ACTION_LISTENER.useListener(item);
        } else if (objString.equals("blood vial")) {
            healFunction();
        } else {
            CONTROLLER.writeInstantly("You try to use something that you don't have or doesn't exist.");
        }
        CONTROLLER.updateHUD();
        CONTROLLER.updateInventory("default");
    }

    public void takeFunction(String itemName) {
        Item item = WORLD.getCurrentPlace().getItemByName(itemName);
        if (item != null) {
            if (!item.isTaken()) {
                CONTROLLER.writeInstantly(item.take(HUNTER));
                SOUND_MANAGER.playSoundEffect("take-item.wav");
                ACTION_LISTENER.takeListener(item);
            } else {
                CONTROLLER.writeInstantly("You already took this item.");
            }
        } else {
            CONTROLLER.writeInstantly("You try to take something that doesn't exist.");
        }
        CONTROLLER.updateInventory("default");
    }


    public void equipFunction(String object) {
        Item item = HUNTER.getItemByName(object);
        if (item != null) {
            if (item instanceof Weapon) {
                if (item instanceof TrickWeapon) {
                    CONTROLLER.writeInstantly(HUNTER.equipTrickWeapon((TrickWeapon) item));
                } else {
                    CONTROLLER.writeInstantly(HUNTER.equipFireArm((FireArm) item));
                }
                SOUND_MANAGER.playSoundEffect("weapon-equip.wav");
                CONTROLLER.updateWeapons();
            } else if (item instanceof Rune) {
                if (HUNTER.getNumberOfRunes() >= 3) {
                    setAnalyzer(TextAnalyzer.RUNE);
                    memorizedRune = (Rune) item;
                    CONTROLLER.writeInstantly("You already have 3 runes equipped, which one do you want to replace ? [1/2/3/Cancel]");
                } else {
                    CONTROLLER.writeInstantly(HUNTER.equipRune((Rune) item, -1)); //-1 to just add the rune to the list
                    SOUND_MANAGER.playSoundEffect("weapon-equip.wav");
                    CONTROLLER.updateRunes();
                }
            } else {
                CONTROLLER.writeInstantly("This item is not a weapon nor a rune.");
            }
        } else {
            CONTROLLER.writeInstantly("You try to equip a weapon that you don't have or doesn't exist.");
        }
        CONTROLLER.updateHUD();
        CONTROLLER.updateInventory("default");
    }

    public void runeDecisionFunction(int position) {
        CONTROLLER.writeInstantly(HUNTER.equipRune(memorizedRune, position));
        SOUND_MANAGER.playSoundEffect("weapon-equip.wav");
        CONTROLLER.updateRunes();
        setAnalyzer(TextAnalyzer.EXPLORATION);
    }

    public void initiateFightFunction(String target) {
        Entity eTarget = WORLD.getCurrentPlace().getEnemyByName(target.toLowerCase(Locale.ROOT));
        if (eTarget instanceof Enemy) {
            if (eTarget.isDead()) {
                CONTROLLER.writeInstantly("This enemy is already dead.");
            } else {
                CONTROLLER.writeLetterByLetter("You engage the enemy. Now you must fight or flee.");
                setAnalyzer(TextAnalyzer.FIGHT);
                currentlyFoughtEntity = eTarget;
                ACTION_LISTENER.initiateFightListener();
            }
        } else {
            CONTROLLER.writeInstantly("You try to engage a fight with something that doesn't exist.");
        }
    }

    public void quitFunction() {
        CONTROLLER.writeInstantly("Do you really want to quit ? All progress will be lost [Y/N] ");
        setAnalyzer(TextAnalyzer.QUIT);
    }

    public void resolveFight(String decision) {
        if (decision.equals("melee")) { //Player's turn
            CONTROLLER.writeInstantly(HUNTER.attack(currentlyFoughtEntity, SOUND_MANAGER));
        } else { //Ranged attack
            CONTROLLER.writeInstantly(HUNTER.shoot(currentlyFoughtEntity, SOUND_MANAGER));
        }
        if (currentlyFoughtEntity.isDead()) {
            checkEntityKilledIsBoss(currentlyFoughtEntity);
            CONTROLLER.updateHUD();
            ACTION_LISTENER.resolveFightListener();
            return;
        } else { //Enemy's turn if not dead
            CONTROLLER.writeInstantly(currentlyFoughtEntity.attack(HUNTER, SOUND_MANAGER));
            if (HUNTER.isDead()) {
                death();
            }
        }
        CONTROLLER.updateHUD();
        ACTION_LISTENER.resolveFightListener();
    }

    public void checkEntityKilledIsBoss(Entity enemy) {
        Enemy e = (Enemy) enemy;
        if (e instanceof Boss) {
            CONTROLLER.transitionImage("prey-slaughtered.png");
            SOUND_MANAGER.playSoundEffect("prey-slaughtered.wav");
            SOUND_MANAGER.setLoopingSound("central-yharnam.wav");
        } else {
            if (!HUNTER.isLastAttackVisceral()) {
                SOUND_MANAGER.playSoundEffect("enemy-killed.wav");
            }
            CONTROLLER.writeLetterByLetter("You defeated your enemy and survived this fight.");
        }
        CONTROLLER.writeLetterByLetter(e.loot(HUNTER));
        CONTROLLER.updateInventory("default");
        setAnalyzer(TextAnalyzer.EXPLORATION);
        ACTION_LISTENER.deadEnemyListener(currentlyFoughtEntity);
        currentlyFoughtEntity = null;
    }

    public void switchFunction() {
        CONTROLLER.writeInstantly(HUNTER.switchTrickWeaponState());
        CONTROLLER.updateWeapons();
    }

    public void fleeFunction() {
        if (!(currentlyFoughtEntity instanceof Boss)) {
            HUNTER.takeDamage(5);
            if (HUNTER.isDead()) {
                CONTROLLER.updateHUD();
                CONTROLLER.writeLetterByLetter("You were too weak too flee and your enemy caught up with you. He executes you.");
                death();
            } else {
                CONTROLLER.updateHUD();
                CONTROLLER.writeLetterByLetter("You run away from the fight but your enemy won't let you do it easily, you take some damage as you flee pitifully.");
            }
            setAnalyzer(TextAnalyzer.EXPLORATION);
        } else {
            CONTROLLER.writeInstantly("You can't flee from a boss fight. Kill or be killed.");
        }
    }

    public void healFunction() {
        CONTROLLER.writeLetterByLetter(HUNTER.heal(SOUND_MANAGER));
        if (currentlyFoughtEntity != null) {
            CONTROLLER.writeInstantly(currentlyFoughtEntity.attack(HUNTER, SOUND_MANAGER));
            if (HUNTER.isDead()) {
                death();
            }
            ACTION_LISTENER.resolveFightListener();
        }
        CONTROLLER.updateHUD();
        CONTROLLER.updateInventory("default");
    }

    public void killFunction() {
        HUNTER.takeDamage(99);
        CONTROLLER.updateHUD();
    }
}
