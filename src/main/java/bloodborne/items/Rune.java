package bloodborne.items;

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

}
