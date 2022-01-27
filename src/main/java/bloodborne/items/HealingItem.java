package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class HealingItem extends Item {

    public HealingItem(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        soundManager.playSoundEffect("used-bloodvial.wav");
        return hunter.healFromItem(Double.parseDouble(getATTRIBUTES().get("healValue")), this);
    }
}
