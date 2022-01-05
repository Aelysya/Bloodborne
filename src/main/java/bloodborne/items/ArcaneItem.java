package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;
import java.util.Map;

public class ArcaneItem extends Item{

    public ArcaneItem(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) { //TODO Make ArcaneItem methods
        return "You can't use that.";
    }
}
