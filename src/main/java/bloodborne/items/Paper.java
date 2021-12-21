package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class Paper extends Item{

    public Paper(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public String getTYPE(){
        return ATTRIBUTES.get("type");
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        String txt;
        if(hunter.getTrickWeapon() == null){
            txt = "You can't use this paper right now, you don't have any trick weapon equipped.";
        } else {
            if(hunter.getDamageBoost() != 0){
                txt = "You recently used a paper onto your weapon and it's effect has not ran out yet.";
            } else {
                hunter.boostDamage(Integer.parseInt(ATTRIBUTES.get("damageBoost")), this, soundManager);
                txt = "You apply the paper onto your trick weapon. It will now do more damage for some attacks.";
            }
        }
        return txt;
    }
}
