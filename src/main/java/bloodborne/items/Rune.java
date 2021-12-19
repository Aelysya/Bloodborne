package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;
import java.util.Map;

public class Rune extends Item{

    public Rune(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public String getIcon(){
        return ATTRIBUTES.get("imagePath");
    }

    public String getEquipText(){
        return ATTRIBUTES.get("equipText");
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        return "You can't use this.";
    }
}
