package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class FireArm extends Weapon{

    private final double HIT_RATE;
    private final double VISCERAL_RATE;
    private final int BULLET_USE;

    public FireArm(String id, String description, Map<String, String> att) {
        super(id, description, att);
        HIT_RATE = Double.parseDouble(att.get("hitRate"));
        VISCERAL_RATE = Double.parseDouble(att.get("visceralRate"));
        BULLET_USE = Integer.parseInt(att.get("bulletUse"));
    }


    @Override
    public int getCurrentDamage() {
        return DAMAGE;
    }

    public double getHIT_RATE(){
        return HIT_RATE;
    }

    public double getVISCERAL_RATE() {
        return VISCERAL_RATE;
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        return "You can't use that.";
    }

    public int getBULLET_USE() {
        return BULLET_USE;
    }
}
