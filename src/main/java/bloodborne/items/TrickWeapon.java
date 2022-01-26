package bloodborne.items;

import java.util.Map;

public class TrickWeapon extends Weapon {

    private boolean isSwitched;

    public TrickWeapon(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public int getCurrentDamage() {
        return isSwitched ? Integer.parseInt(ATTRIBUTES.get("switchedDamage")) : Integer.parseInt(ATTRIBUTES.get("baseDamage"));
    }

    public double getCurrentDodgeRate() {
        return isSwitched ? Double.parseDouble(ATTRIBUTES.get("switchedDodgeRate")) : Double.parseDouble(ATTRIBUTES.get("baseDodgeRate"));
    }

    public void switchMode() {
        isSwitched = !isSwitched;
    }
}
