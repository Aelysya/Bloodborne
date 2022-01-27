package bloodborne.json;

import java.util.Map;

public class ElementTemplate {

    private final String ID;
    private final String DESCRIPTION;
    private final Map<String, String> ATTRIBUTES;

    public ElementTemplate(String id, String description, Map<String, String> attributes) {
        ID = id;
        DESCRIPTION = description;
        ATTRIBUTES = attributes;
    }

    public String getID() {
        return ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public Map<String, String> getATTRIBUTES() {
        return ATTRIBUTES;
    }
}
