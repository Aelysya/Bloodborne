package bloodborne.environment;

import java.util.Map;

public class Headstone extends Prop {

    public Headstone(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
    }

    public String getHeadstoneSimpleName() {
        String[] cutId = getID().split("-", 2);
        return cutId[0];
    }
}
