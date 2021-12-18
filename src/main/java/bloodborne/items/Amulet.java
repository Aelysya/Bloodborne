package bloodborne.items;

import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class Amulet extends Item{

    private boolean isUsed;

    public Amulet(String id, String description, Map<String, String> att) {
        super(id, description, att);
        isUsed = false;
    }

    @Override
    public boolean isUsed() {
        return isUsed;
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager) {
        isUsed = true;
        soundManager.playSoundEffect("amulet_used.wav");
        return "You used Olek's amulet and reveal a hidden path. You can now go east but you feel that there will be no coming back.";
    }
}
