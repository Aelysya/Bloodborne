package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class Coldblood extends Item{


    public Coldblood(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        soundManager.playSoundEffect("gain_echoes.wav");
        hunter.gainBloodEchoes(Integer.parseInt(ATTRIBUTES.get("value")));
        return "You crush the coldblood in your hand and gain blood echoes.";
    }
}
