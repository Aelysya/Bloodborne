package bloodborne.items;

import bloodborne.entities.Entity;
import bloodborne.entities.Hunter;
import bloodborne.exceptions.ReflectionException;
import bloodborne.json.Initializable;
import bloodborne.sounds.SoundManager;
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

    @Override
    public String use(Hunter hunter, SoundManager soundManager, Entity target) { //TODO create gem system to make this method work
        /*soundManager.playSoundEffect("itemsUse/crush-gem-container.wav");
        hunter.removeOneItemFromStack(this);
        hunter.addItem(gemContained);
        return "You crush the item in your fist, you find a blood gem inside it's remnants.";*/
        return "Method not yet implemented, create the gem system";
    }
}
