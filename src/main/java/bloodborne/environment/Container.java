package bloodborne.environment;

import bloodborne.entities.Hunter;
import bloodborne.items.Item;
import bloodborne.json.Initializable;
import bloodborne.world.World;

import java.util.Map;

public class Container extends Prop implements Initializable {

    private Item containedItem;

    public Container(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
        containedItem = null;
    }

    @Override
    public String activate(Hunter hunter) {
        String activationDescriptionText;
        if (!hasBeenActivated()) {
            hunter.addItem(containedItem);
            activationDescriptionText = super.activate(hunter);
        } else {
            activationDescriptionText = "There is nothing left here.";
        }
        return activationDescriptionText;
    }

    @Override
    public void initialize(World world) {
        containedItem = world.getItemById(getATTRIBUTES().get("content"));
    }
}
