package bloodborne.items;

import java.util.Map;

public class Rune extends Item {

    public Rune(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public String getEquipText() {
        return getATTRIBUTES().get("equipText");
    }

    public int getTier() {
        return Integer.parseInt(getATTRIBUTES().get("tier"));
    }

}
