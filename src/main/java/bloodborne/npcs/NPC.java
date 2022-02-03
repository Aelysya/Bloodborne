package bloodborne.npcs;

import bloodborne.entities.Hunter;
import bloodborne.exceptions.ReflectionException;
import bloodborne.json.ElementTemplate;
import bloodborne.json.Initializable;
import bloodborne.world.World;

import java.util.Map;

public class NPC extends ElementTemplate implements Initializable {

    public NPC(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
    }

    public String talk(Hunter hunter){
        return "You should not see this sentence";
    }

    @Override
    public void initialize(World world) throws ReflectionException {}
}
