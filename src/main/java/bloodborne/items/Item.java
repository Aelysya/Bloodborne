package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.json.ElementTemplate;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class Item extends ElementTemplate {

    private boolean isTaken;

    public Item(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
        isTaken = false;
    }

    public String take(Hunter hunter) {
        isTaken = true;
        hunter.addItem(this);
        return "You took the " + getATTRIBUTES().get("name");
    }

    public String use(Hunter hunter, SoundManager soundManager) {
        return "You can't use that.";
    }

    public String getNAME() {
        return getATTRIBUTES().get("name");
    }

    public String getCategory() {
        return getATTRIBUTES().get("category");
    }

    public void setTaken(boolean isTaken) {
        this.isTaken = isTaken;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public String getImage() {
        return "images/items/" + getATTRIBUTES().get("image");
    }
}
