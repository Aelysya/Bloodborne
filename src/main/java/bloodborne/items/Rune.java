package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;
import java.util.Map;

public class Rune extends Item{

    public Rune(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public String getEquipText(){
        return ATTRIBUTES.get("equipText");
    }

    public int getTier(){
        return Integer.parseInt(ATTRIBUTES.get("tier"));
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        return "You can't use this.";
    }
}
