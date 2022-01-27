package bloodborne.npcs;

import bloodborne.entities.Hunter;
import bloodborne.items.HealingItem;

import java.util.HashMap;
import java.util.Map;

public class Iosefka extends NPC {

    private boolean hunterHasIosefkasBloodVial;
    private boolean talkedOnce;
    private final HealingItem VIAL;

    public Iosefka(String id, String description, Map<String, String> att) {
        super(id, description, att);
        hunterHasIosefkasBloodVial = false;
        talkedOnce = false;
        Map<String, String> m = new HashMap<>();
        m.put("name", "Iosefka's blood vial");
        m.put("image", "consumables/iosefkas-blood-vial.png");
        m.put("healValue", "0.7");
        m.put("category", "consumables");
        VIAL = new HealingItem("iosefkas-blood-vial", "Blood vial acquired from Iosefka's clinic. This refined blood, highly invigorating, restores a larger amount of HP. The product of a slow and careful refinement process, this rare blood vial appears to be a clinic original.", m);
    }

    @Override
    public String talk(Hunter hunter) {
        String speech;
        if (!talkedOnce) {
            talkedOnce = true;
            hunterHasIosefkasBloodVial = true;
            hunter.addItem(VIAL);
            speech = ATTRIBUTES.get("firstSpeech");
        } else {
            if (!hunterHasIosefkasBloodVial) {
                hunterHasIosefkasBloodVial = true;
                hunter.addItem(VIAL);
                speech = ATTRIBUTES.get("noVialSpeech");
            } else {
                speech = ATTRIBUTES.get("hasVialSpeech");
            }
        }
        return speech;
    }

    public void setHunterHasIosefkasBloodVial(boolean hasVial) {
        hunterHasIosefkasBloodVial = hasVial;
    }
}
