package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class TrickWeapon extends Weapon{

    private final int SWITCHED_DAMAGE;
    private boolean isSwitched;
    private final double BASE_DODGE_RATE;
    private final double SWITCHED_DODGE_RATE;

    public TrickWeapon(String id, String description, Map<String, String> att) {
        super(id, description, att);
        SWITCHED_DAMAGE = Integer.parseInt(att.get("switchedDamage"));
        BASE_DODGE_RATE = Double.parseDouble(att.get("baseDodgeRate"));
        SWITCHED_DODGE_RATE = Double.parseDouble(att.get("switchedDodgeRate"));
    }


    @Override
    public int getCurrentDamage() {
        return isSwitched ? SWITCHED_DAMAGE : DAMAGE;
    }

    public double getCurrentDodgeRate(){
        return isSwitched ? SWITCHED_DODGE_RATE : BASE_DODGE_RATE;
    }

    public void switchMode(){
        isSwitched = !isSwitched;
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        return "You can't use that.";
    }
}
