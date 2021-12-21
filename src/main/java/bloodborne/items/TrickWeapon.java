package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class TrickWeapon extends Weapon{

    private boolean isSwitched;

    public TrickWeapon(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public int getCurrentDamage() {
        return isSwitched ? Integer.parseInt(ATTRIBUTES.get("switchedDamage")) : Integer.parseInt(ATTRIBUTES.get("damage"));
    }

    public double getCurrentDodgeRate(){
        return isSwitched ? Double.parseDouble(ATTRIBUTES.get("switchedDodgeRate")) : Double.parseDouble(ATTRIBUTES.get("baseDodgeRate"));
    }

    public void switchMode(){
        isSwitched = !isSwitched;
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        return "You can't use that.";
    }
}
