package bloodborne.entities;

import java.util.Map;

public class FatherGascoigne extends Boss{

    public FatherGascoigne(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes, 15, 0.2, 1.0, 1.0, 1.0, 160, 60);
    }

    public Boolean isBeast() {
        return isInThirdPhase();
    }
}
