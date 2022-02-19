package bloodborne.items;

import java.util.Map;

public class TrickWeapon extends Weapon {

    private boolean isSwitched;

    public TrickWeapon(String id, String description, Map<String, String> att) {
        super(id, description, att);
        isSwitched = false;
    }

    public void switchMode() {
        isSwitched = !isSwitched;
    }

    public String getSwitchSound() {
        return "weapons/trickWeapons/" + getID() + "-switch" + (!isSwitched ? "-back" : "") + ".wav";
    }

    @Override
    public int getCurrentDamage() {
        return isSwitched ? Integer.parseInt(getATTRIBUTES().get("switchedDamage")) : Integer.parseInt(getATTRIBUTES().get("baseDamage"));
    }

    @Override
    public String getAttackSound() {
        return "weapons/trickWeapons/" + getID() + ".wav";
    }

    public double getCurrentDodgeRate() {
        return isSwitched ? Double.parseDouble(getATTRIBUTES().get("switchedDodgeRate")) : Double.parseDouble(getATTRIBUTES().get("baseDodgeRate"));
    }
}
