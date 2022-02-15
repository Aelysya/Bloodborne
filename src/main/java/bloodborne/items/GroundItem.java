package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.exceptions.ReflectionException;
import bloodborne.json.Initializable;
import bloodborne.sounds.SoundManager;
import bloodborne.world.World;

import java.util.Map;

public class GroundItem extends Item implements Initializable {

    private Item baseItem;

    public GroundItem(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
        baseItem = null;
    }

    @Override
    public void initialize(World world) throws ReflectionException {
        baseItem = world.getItemById(getATTRIBUTES().get("itemId"));
    }

    @Override
    public String take(Hunter hunter, SoundManager soundManager) {
        String explanationText;
        if (!isTaken()) {
            setTaken(true);
            for (int i = 0; i < Integer.parseInt(getATTRIBUTES().get("amount")); i++) {
                hunter.addItem(baseItem);
            }
            soundManager.playSoundEffect("take-item.wav");
            explanationText = "You took the " + getATTRIBUTES().get("name");
        } else {
            explanationText = "You already took this item.";
        }
        return explanationText;
    }

    @Override
    public String getDESCRIPTION() {
        return baseItem.getDESCRIPTION();
    }

    public String getCategory() {
        return baseItem.getATTRIBUTES().get("category");
    }
}
