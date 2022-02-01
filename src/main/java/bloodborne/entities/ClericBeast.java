package bloodborne.entities;

import java.util.Map;

public class ClericBeast extends Boss{

    public ClericBeast(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes, 20, 0.1, 1.5, 1.0, 1.0, 210, 0);
    }
}
