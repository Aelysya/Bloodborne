package bloodborne.environment;

import bloodborne.entities.Hunter;

import java.util.Map;

public abstract class NPC extends Prop {

    public NPC(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public abstract String talk(Hunter hunter);
}
