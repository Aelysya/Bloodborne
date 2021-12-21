package bloodborne.environment;

import bloodborne.entities.Hunter;

import java.util.Map;

public class Friendly extends Prop{

    public Friendly(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public String talk() {
        return ATTRIBUTES.get("speech");
    }

    @Override
    public String activate(Hunter hunter) {
        return "You can't activate this.";
    }
}
