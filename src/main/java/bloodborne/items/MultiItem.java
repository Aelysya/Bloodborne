package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.exceptions.ReflectionException;
import bloodborne.json.Initializable;
import bloodborne.world.World;

import java.util.Map;

public class MultiItem extends Item implements Initializable {

    private Item baseItem;

    public MultiItem(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
        baseItem = null;
    }

    @Override
    public void initialize(World world) throws ReflectionException {
        baseItem = world.getItemById(getATTRIBUTES().get("itemId"));
        System.out.println(baseItem.getNAME());
    }

    @Override
    public String take(Hunter hunter) {
        setTaken(true);
        for (int i = 0; i < Integer.parseInt(getATTRIBUTES().get("amount")); i++) {
            hunter.addItem(baseItem);
        }
        return "You took the " + getATTRIBUTES().get("name");
    }

    public String getNAME() {
        return baseItem.getATTRIBUTES().get("name");
    }

    public String getCategory() {
        return baseItem.getATTRIBUTES().get("category");
    }
}
