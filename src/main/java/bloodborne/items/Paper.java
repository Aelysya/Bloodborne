package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class Paper extends Item{

    private final int DAMAGE_BOOST;
    private final String TYPE;

    public Paper(String id, String description, Map<String, String> att) {
        super(id, description, att);
        DAMAGE_BOOST = Integer.parseInt(att.get("damageBoost"));
        TYPE = att.get("type");
    }

    public String getTYPE(){
        return TYPE;
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
                hunter.boostDamage(DAMAGE_BOOST, this, soundManager);
                txt = "You apply the paper onto your trick weapon. It will now do more damage for some attacks.";
            }
        }
        return txt;
    }
}
