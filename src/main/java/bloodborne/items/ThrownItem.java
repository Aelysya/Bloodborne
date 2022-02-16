package bloodborne.items;

import bloodborne.entities.Entity;
import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class ThrownItem extends ItemWithDamageScaling {

    public ThrownItem(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager, Entity target) { //TODO Make ThrownItem methods
        //TODO Make switch to make all the specific actions depending on the item
        return "You can't use that.";
    }
}
