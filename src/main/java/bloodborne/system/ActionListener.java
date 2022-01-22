package bloodborne.system;

import bloodborne.entities.Boss;
import bloodborne.entities.Entity;
import bloodborne.entities.Hunter;
import bloodborne.environment.Iosefka;
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

    public ActionListener(SoundManager soundManager, Hunter hunter, World world, CommandHandler commandHandler, Game game){
        SOUND_MANAGER = soundManager;
        HUNTER = hunter;
        WORLD = world;
        COMMAND_HANDLER = commandHandler;
        GAME = game;
    }

    public void goListener(){
        if (WORLD.getCurrentPlace().isBossArena()){ //Instantly enter combat mode if entering boss arena
            Boss boss = WORLD.getCurrentPlace().getBoss();
            if (!boss.isDead()){ //If the boss not dead
                GAME.initiateFightFunction(boss.getNAME());
            }
        }
    }

    public void teleportListener(){
        if (WORLD.getCurrentPlace().isBossArena()){ //Instantly enter combat mode if entering boss arena
            Boss boss = WORLD.getCurrentPlace().getBoss();
            if (!boss.isDead()){ //If the boss not dead
                GAME.initiateFightFunction(boss.getNAME());
            }
        }
    }

    public void lookListener(Prop p){
        switch (p.getID()){
            case "yharnam-headstone" -> {
                GAME.writeHeadstoneText("yharnam");
                GAME.setAnalyzer(TextAnalyzer.YHARNAM_HEADSTONE);
            }
            case "frontier-headstone" -> {
                GAME.writeHeadstoneText("frontier");
                GAME.setAnalyzer(TextAnalyzer.FRONTIER_HEADSTONE);
            }
        }
    }

    public void activateListener(){

    }

    public void useListener(Item item){
        switch(item.getNAME()){
            case "Iosefka's blood vial" -> {
                Iosefka i = (Iosefka) WORLD.getPropById("iosefka");
                i.setHunterHasIosefkasBloodVial(false);
            }
        }
    }

    public void takeListener(Item item){
        switch(item.getID()){
            case "iosefkas-blood-vial" -> {
                if (HUNTER.getINVENTORY().hasItem("iosefkas-blood-vial")){
                    GAME.writeInstantly("You can't have more than one copy of this item.");
                } else {
                    Iosefka i = (Iosefka) WORLD.getPropById("iosefka");
                    i.setHunterHasIosefkasBloodVial(true);
                    Map<String, String> m = new HashMap<>();
                    m.put("name", "Iosefka's blood vial");
                    m.put("image", "consumables/iosefkas-blood-vial.png");
                    m.put("healValue", "0.7");
                    m.put("category", "consumables");
                    HealingItem vial = new HealingItem("iosefkas-blood-vial", "Blood vial acquired from Iosefka's clinic. This refined blood, highly invigorating, restores a larger amount of HP. The product of a slow and careful refinement process, this rare blood vial appears to be a clinic original.", m);
                    HUNTER.addItem(vial);
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

    public void initiateFightListener(){

    }

    public void talkListener(){

    }

    public void resolveFightListener(){

    }

    public void deadEnemyListener(Entity enemy){
        switch (enemy.getID()) {
            case "clinic-scourge-beast" -> WORLD.getPlaceById("clinic").switchToAltDescription();
            case "back-clinic-emissary" -> WORLD.getPlaceById("back-clinic").switchToAltDescription();
        }
    }
}
