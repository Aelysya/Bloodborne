package bloodborne.items;

import bloodborne.entities.Entity;
import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class ArcaneItem extends ItemWithDamageScaling {

    public ArcaneItem(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManage, Entity target) {
        return ""; //TODO method use ArcaneItem
    }
}
