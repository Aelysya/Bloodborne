package bloodborne.npcs;

import bloodborne.entities.Hunter;
import bloodborne.items.HealingItem;
import bloodborne.items.Item;
import bloodborne.json.Initializable;
import bloodborne.world.World;

import java.util.Map;

public class Iosefka extends NPC implements Initializable {

    private boolean hunterHasIosefkasBloodVial;
    private boolean talkedOnce;
    private Item vial;

    public Iosefka(String id, String description, Map<String, String> att) {
        super(id, description, att);
        hunterHasIosefkasBloodVial = false;
        talkedOnce = false;
        vial = null;
    }

    @Override
    public void initialize(World world) {
        vial = world.getItemById("iosefkas-blood-vial");
    }

    @Override
    public String talk(Hunter hunter) {
        String speech;
        if (!talkedOnce) {
            talkedOnce = true;
            hunterHasIosefkasBloodVial = true;
            hunter.addItem(vial);
            speech = getATTRIBUTES().get("firstSpeech");
        } else {
            if (!hunterHasIosefkasBloodVial) {
                hunterHasIosefkasBloodVial = true;
                hunter.addItem(vial);
                speech = getATTRIBUTES().get("noVialSpeech");
            } else {
        speech = getATTRIBUTES().get("hasVialSpeech");
            }
        }
        return speech;
    }

    public void setHunterHasIosefkasBloodVial(boolean hasVial) {
        hunterHasIosefkasBloodVial = hasVial;
    }
}
