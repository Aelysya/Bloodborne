package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class BoostItem extends Item {

    public BoostItem(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public String getTYPE() {
        return getATTRIBUTES().get("type");
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        String txt;
        if (hunter.getTrickWeapon() == null) {
            txt = "You can't use this right now, you don't have any trick weapon equipped.";
        } else {
            if (hunter.getDamageBoost() != 0) {
                txt = "You recently used a boost item onto your weapon and it's effect has not ran out yet.";
            } else {
                hunter.boostDamage(Integer.parseInt(getATTRIBUTES().get("damageBoost")), this, soundManager);
                txt = "You empower your trick weapon. It will now do more damage for some attacks.";
            }
        }
        return txt;
    }
}
