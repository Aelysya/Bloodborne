package bloodborne.npcs;

import bloodborne.entities.Hunter;
import bloodborne.items.Item;
import bloodborne.json.Initializable;
import bloodborne.world.World;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Gilbert extends NPC implements Initializable {

    private boolean talkedOnceDay;
    private boolean talkedOnceEvening;
    private boolean talkedOnceNight;
    private boolean talkedOnceCleric;
    private boolean hunterHasKilledClericBeast;
    private boolean hunterHeardOfByrgenwerth;
    private Item flamesprayer;

    public Gilbert(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
        talkedOnceDay = false;
        talkedOnceEvening = false;
        talkedOnceNight = false;
        talkedOnceCleric = false;
        hunterHasKilledClericBeast = false;
        hunterHeardOfByrgenwerth = false;
        flamesprayer = null;
    }

    @Override
    public void initialize(World world) {
        flamesprayer = world.getItemById("flamesprayer");
    }

    public void hunterKilledClericBeast() {
        hunterHasKilledClericBeast = true;
    }

    public void HunterHeardOfByrgenwerth() {
        hunterHeardOfByrgenwerth = true;
    }

    @Override
    public String talk(Hunter hunter) {
        String speech;
        switch (hunter.getTimeOfNight()) {
            case "evening" -> {
                if (!talkedOnceEvening) {
                    talkedOnceEvening = true;
                    hunter.addItem(flamesprayer);
                    speech = getATTRIBUTES().get("eveningSpeech");
                } else {
                    if (hunterHeardOfByrgenwerth) {
                        speech = getATTRIBUTES().get("byrgenwerthSpeech");
                    } else {
                        speech = getATTRIBUTES().get("repeatSpeech");
                    }
                }
            }
            case "night" -> {
                if (!talkedOnceNight) {
                    talkedOnceNight = true;
                    speech = getATTRIBUTES().get("nightSpeech-1");
                } else {
                    int randomInt = ThreadLocalRandom.current().nextInt(2, 6);
                    speech = getATTRIBUTES().get("nightSpeech-" + randomInt);
                }
            }
            case "blood moon" -> speech = "No one answers...";
            default -> {
                if (!talkedOnceDay) {
                    talkedOnceDay = true;
                    speech = getATTRIBUTES().get("firstSpeech");
                } else {
                    if (!hunterHasKilledClericBeast) {
                        speech = getATTRIBUTES().get("repeatSpeech");
                    } else {
                        if (!talkedOnceCleric) {
                            talkedOnceCleric = true;
                            speech = getATTRIBUTES().get("afterClericSpeech");
                        } else {
                            speech = getATTRIBUTES().get("repeatAfterClericSpeech");
                        }
                    }
                }
            }
        }
        return speech;
    }
}
