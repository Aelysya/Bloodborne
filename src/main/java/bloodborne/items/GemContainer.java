package bloodborne.items;

import bloodborne.exceptions.ReflectionException;
import bloodborne.json.Initializable;
import bloodborne.world.World;

import java.util.Map;

public class GemContainer extends Item implements Initializable {

    private Item gemContained;

    public GemContainer(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
        gemContained = null;
    }

    @Override
    public void initialize(World world) throws ReflectionException {
        gemContained = world.getItemById(getATTRIBUTES().get("itemId"));
    }
}
