package bloodborne.environment;

import bloodborne.entities.Hunter;
import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.exceptions.ReflectionException;
import bloodborne.items.Item;
import bloodborne.world.World;

import java.util.Map;

public class Container extends Prop {

    private Item containedItem;
    private boolean isLooted;

    public Container(String id, String description, Map<String, String> att) {
        super(id, description, att);
        containedItem = null;
        isLooted = false;
    }

    public boolean isLooted() {
        return isLooted;
    }

    @Override
    public String activate(Hunter hunter) {
        String txt;
        if (isLooted) {
            txt = "There is nothing left here.";
        } else {
            addConsumablesToHunterInventory(hunter);
            hunter.addItem(containedItem);
            isLooted = true;
            txt = ATTRIBUTES.get("activationText");
        }
        return txt;
    }

    @Override
    public void initialize(World world) {
        containedItem = world.getItemById(ATTRIBUTES.get("content"));
    }
}
