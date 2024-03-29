package bloodborne.items;

import bloodborne.entities.Entity;
import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class Coldblood extends Item {

    public Coldblood(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager, Entity target) {
        soundManager.playSoundEffect("itemsUse/gain-echoes.wav");
        hunter.gainBloodEchoes(Integer.parseInt(getATTRIBUTES().get("value")));
        hunter.removeOneItemFromStack(this);
        return "You crush the coldblood in your fist and gain blood echoes.";
    }
}
