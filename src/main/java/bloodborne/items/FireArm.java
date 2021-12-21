package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class FireArm extends Weapon{

    public FireArm(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public int getCurrentDamage() {
        return Integer.parseInt(ATTRIBUTES.get("damage"));
    }

    public double getHIT_RATE(){
        return Double.parseDouble(ATTRIBUTES.get("hitRate"));
    }

    public double getVISCERAL_RATE() {
        return Double.parseDouble(ATTRIBUTES.get("visceralRate"));
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        return "You can't use that.";
    }

    public int getBULLET_USE() {
        return Integer.parseInt(ATTRIBUTES.get("bulletUse"));
    }
}
