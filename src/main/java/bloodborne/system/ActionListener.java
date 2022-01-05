package bloodborne.system;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;
import bloodborne.zone.Zone;

public class ActionListener {

    private final SoundManager SOUND_MANAGER;
    private final Hunter HUNTER;
    private final Zone ZONE;

    public ActionListener(SoundManager soundManager, Hunter hunter, Zone zone){
        SOUND_MANAGER = soundManager;
        HUNTER = hunter;
        ZONE = zone;
    }

    public void goListener(){

    }

    public void teleportListener(){

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
