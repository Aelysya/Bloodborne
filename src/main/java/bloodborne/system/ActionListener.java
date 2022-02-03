package bloodborne.system;

import bloodborne.entities.Boss;
import bloodborne.entities.Enemy;
import bloodborne.entities.Hunter;
import bloodborne.npcs.Gilbert;
import bloodborne.npcs.Iosefka;
import bloodborne.environment.Prop;
import bloodborne.items.HealingItem;
import bloodborne.items.Item;
import bloodborne.sounds.SoundManager;
import bloodborne.world.World;

import java.util.HashMap;
import java.util.Map;

public class ActionListener {

    private final SoundManager SOUND_MANAGER;
    private final Hunter HUNTER;
    private final World WORLD;
    private final CommandHandler COMMAND_HANDLER;
    private final Game GAME;

    public ActionListener(SoundManager soundManager, Hunter hunter, World world, CommandHandler commandHandler, Game game) {
        SOUND_MANAGER = soundManager;
        HUNTER = hunter;
        WORLD = world;
        COMMAND_HANDLER = commandHandler;
        GAME = game;
    }

    public void goListener() {
        if (WORLD.getCurrentPlace().isBossArena()) { //Instantly enter combat mode if entering boss arena
            Boss boss = WORLD.getCurrentPlace().getBoss();
            if (!boss.isDead()) { //If the boss not dead
                GAME.initiateFightFunction(boss.getNAME());
            }
        }
    }

    public void teleportListener() {
        if (WORLD.getCurrentPlace().isBossArena()) { //Instantly enter combat mode if entering boss arena
            Boss boss = WORLD.getCurrentPlace().getBoss();
            if (!boss.isDead()) { //If the boss not dead
                GAME.initiateFightFunction(boss.getNAME());
            }
        }
    }

    public void lookListener(Prop p) {

    }

    public void activateListener() {

    }

    public void useListener(Item item) {
        switch (item.getNAME()) {
            case "Iosefka's blood vial" -> {
                Iosefka i = (Iosefka) WORLD.getNpcById("iosefka");
                i.setHunterHasIosefkasBloodVial(false);
            }
        }
    }

    public void takeListener(Item item) {
        switch (item.getID()) {
            case "iosefkas-blood-vial" -> {
                if (HUNTER.getINVENTORY().hasItem(WORLD.getItemById("iosefkas-blood-vial"))) {
                    GAME.writeInstantly("You can't have more than one copy of this item.");
                } else {
                    Iosefka i = (Iosefka) WORLD.getNpcById("iosefka");
                    i.setHunterHasIosefkasBloodVial(true);
                    HUNTER.addItem(WORLD.getItemById("iosefkas-blood-vial"));
                }
            }
            case "saw-cleaver" -> {//The 3 original trick weapons to choose from, 1 is taken the other two are gone
                WORLD.getCurrentPlace().removeItemByID("threaded-cane");
                WORLD.getCurrentPlace().removeItemByID("hunter-axe");
            }
            case "hunter-axe" -> {
                WORLD.getCurrentPlace().removeItemByID("threaded-cane");
                WORLD.getCurrentPlace().removeItemByID("saw-cleaver");
            }
            case "threaded-cane" -> {
                WORLD.getCurrentPlace().removeItemByID("saw-cleaver");
                WORLD.getCurrentPlace().removeItemByID("hunter-axe");
            }

            case "hunter-pistol" -> WORLD.getCurrentPlace().removeItemByID("hunter-blunderbuss"); //The 2 original firearms to choose from, 1 is taken the other is gone
            case "hunter-blunderbuss" -> WORLD.getCurrentPlace().removeItemByID("hunter-pistol");
        }
    }

    public void initiateFightListener() {

    }

    public void talkListener() {

    }

    public void resolveFightListener() {

    }

    public void deadEnemyListener(Enemy enemy) {
        switch (enemy.getID()) {
            case "clinic-scourge-beast" -> WORLD.getPlaceById("clinic").switchToAltDescription();
            case "back-clinic-emissary" -> WORLD.getPlaceById("back-clinic").switchToAltDescription();
            case "boss-cleric-beast" -> ((Gilbert) WORLD.getNpcById("gilbert")).hunterKilledClericBeast();
        }
    }
}
