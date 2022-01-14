package bloodborne.system;

import bloodborne.entities.Boss;
import bloodborne.entities.Entity;
import bloodborne.entities.Hunter;
import bloodborne.environment.Prop;
import bloodborne.sounds.SoundManager;
import bloodborne.world.World;

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
            default -> System.err.println("Invalid headstone name, please check the code.");
        }
    }

    public void activateListener(){

    }

    public void useListener(){

    }

    public void takeListener(){

    }

    public void initiateFightListener(){

    }

    public void talkListener(){

    }

    public void resolveFightListener(){

    }

    public void deadEnemyListener(Entity enemy){
        String enemyID = enemy.getID();
        switch (enemyID) {
            case "clinic-scourge-beast" -> WORLD.getPlaceById("clinic").switchToAltDescription();
            case "back-clinic-emissary" -> WORLD.getPlaceById("back-clinic").switchToAltDescription();
        }
    }
}
