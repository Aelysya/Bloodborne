package bloodborne.system;

import bloodborne.entities.Boss;
import bloodborne.entities.Hunter;
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

    public void lookListener(){

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

    public void deadEnemyListener(){

    }
}
