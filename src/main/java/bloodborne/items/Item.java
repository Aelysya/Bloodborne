package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class Item {

    private final String ID;
    private final String DESCRIPTION;
    protected final Map<String, String> ATTRIBUTES;
    private boolean isTaken;

    public Item(String id, String description, Map<String, String> att) {
        ID = id;
        DESCRIPTION = description;
        isTaken = false;
        ATTRIBUTES = att;
    }

    public String getID() {
        return ID;
    }

    public String getImage() {
        return ATTRIBUTES.get("image");
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getNAME() {
        return ATTRIBUTES.get("name");
    }

    public String getCategory() {
        return ATTRIBUTES.get("category");
    }

    public boolean isTaken() {
        return isTaken;
    }

    public String take(Hunter hunter) {
        isTaken = true;
        hunter.addItem(this);
        return "You took the " + ATTRIBUTES.get("name");
    }

    public String use(Hunter hunter, SoundManager soundManager) {
        return "You can't use that.";
    }
}
