package bloodborne.environment;

import bloodborne.entities.Hunter;
import bloodborne.json.ElementTemplate;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class Prop extends ElementTemplate {

    private boolean hasBeenActivated;
    private boolean hasBeenLookedOnce;

    public Prop(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
        hasBeenActivated = false;
        hasBeenLookedOnce = false;
    }

    public String look(Hunter hunter) {
        if (!hasBeenLookedOnce) {
            addConsumablesToHunterInventory(hunter);
        }
        hasBeenLookedOnce = true;
        return getDESCRIPTION();
    }

    public void addConsumablesToHunterInventory(Hunter hunter) {
        if (getATTRIBUTES().get("vialsContained") != null) {
            hunter.addVials(Integer.parseInt(getATTRIBUTES().get("vialsContained")));
        }
        if (getATTRIBUTES().get("bulletsContained") != null) {
            hunter.addBullets(Integer.parseInt(getATTRIBUTES().get("bulletsContained")));
        }
    }

    public String activate(Hunter hunter) {
        String activationDescriptionText;
        if (Boolean.parseBoolean(getATTRIBUTES().get("isActionable"))) {
            if (!hasBeenActivated) {
                hasBeenActivated = true;
                addConsumablesToHunterInventory(hunter);
                if (Boolean.parseBoolean(getATTRIBUTES().get("isTrapped"))) {
                    hunter.takeDamage(Integer.parseInt(getATTRIBUTES().get("damageDone")), new SoundManager());
                }
                activationDescriptionText = hunter.isDead() ? getATTRIBUTES().get("deathDescription") : getATTRIBUTES().get("activationText");
            } else {
                activationDescriptionText = "You already activated this";
            }
        } else {
            activationDescriptionText = "You can't activate this.";
        }
        return activationDescriptionText;
    }

    public boolean hasBeenActivated() {
        return hasBeenActivated;
    }

    public boolean hasBeenLooked() {
        return hasBeenLookedOnce;
    }
}
