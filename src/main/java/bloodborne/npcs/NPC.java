package bloodborne.npcs;

import bloodborne.entities.Hunter;
import bloodborne.json.ElementTemplate;

import java.util.Map;

public class NPC extends ElementTemplate {

    public NPC(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
    }

    public String talk(Hunter hunter){
        return "You should not see this sentence";
    }
}
