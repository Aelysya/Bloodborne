package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class ArcaneItem extends ItemWithDamageScaling {

    public ArcaneItem(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManage) {
        return ""; //TODO method use ArcaneItem
    }
}
