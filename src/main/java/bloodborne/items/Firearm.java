package bloodborne.items;

import java.util.Map;

public class Firearm extends Weapon {

    public Firearm(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public int getCurrentDamage() {
        return Integer.parseInt(getATTRIBUTES().get("damage"));
    }

    public double getHIT_RATE() {
        return Double.parseDouble(getATTRIBUTES().get("hitRate"));
    }

    public double getVISCERAL_RATE() {
        return Double.parseDouble(getATTRIBUTES().get("visceralRate"));
    }

    public int getBULLET_USE() {
        return Integer.parseInt(getATTRIBUTES().get("bulletUse"));
    }
}
