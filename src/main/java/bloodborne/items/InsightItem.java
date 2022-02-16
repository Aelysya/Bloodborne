package bloodborne.items;

import bloodborne.entities.Entity;
import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class InsightItem extends Item{

    public InsightItem(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager, Entity target) {
        soundManager.playSoundEffect("itemsUse/gain-insight.wav");
        hunter.gainInsight(Integer.parseInt(getATTRIBUTES().get("value")));
        hunter.removeOneItemFromStack(this);
        return "You crush the item in your fist, your vision expands and you gain insight.";
    }
}
