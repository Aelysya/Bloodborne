package bloodborne.environment;

import bloodborne.entities.Hunter;

import java.util.Map;

public class Friendly extends Prop{

    private final String SPEECH;

    public Friendly(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
        SPEECH = attributes.get("speech");
    }

    public String talk() {
        return SPEECH;
    }

    @Override
    public String activate(Hunter hunter) {
        return "You can't activate this.";
    }
}
