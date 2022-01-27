package bloodborne.npcs;

import bloodborne.entities.Hunter;

import java.util.Map;

public class NPC {

    private final String ID;
    private final String DESCRIPTION;
    protected final Map<String, String> ATTRIBUTES;

    public NPC(String id, String description, Map<String, String> att) {
        ID = id;
        DESCRIPTION = description;
        ATTRIBUTES = att;
    }

    public String getID() {
        return ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String talk(Hunter hunter){
        return "You should not see this sentence";
    }
}
