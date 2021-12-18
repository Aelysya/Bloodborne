package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public abstract class Item {

    private final String ID;
    private final String NAME;
    private final String DESCRIPTION;
    private boolean isTaken;
    protected final Map<String, String> ATTRIBUTES;

    public Item(String id, String description, Map<String, String> att) {
        ID = id;
        NAME = att.get("name");
        DESCRIPTION = description;
        isTaken = false;
        ATTRIBUTES = att;
    }

    public String getID() {
        return ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getNAME() {
        return NAME;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public String take(Hunter hunter) {
        isTaken = true;
        hunter.addItem(this);
        return "You took the " + NAME;
    }

    public abstract String use(Hunter hunter, SoundManager soundManager);

    public boolean isUsed() {
        return true;
    }
}
