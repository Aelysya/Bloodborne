package bloodborne.environment;

import bloodborne.entities.Hunter;
import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.exceptions.ReflectionException;
import bloodborne.world.World;

import java.util.Map;

public abstract class Prop {

    private final String ID;
    private final String DESCRIPTION;
    protected final Map<String, String> ATTRIBUTES;
    private boolean hasBeenActivated;
    private boolean hasBeenLookedOnce;

    public Prop(String id, String description, Map<String, String> att) {
        ID = id;
        DESCRIPTION = description;
        ATTRIBUTES = att;
        hasBeenActivated = false;
        hasBeenLookedOnce = false;
    }

    public abstract void initialize(World world);

    public String getID() {
        return ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public boolean HasBeenActivated() {
        return hasBeenActivated;
    }

    public boolean hasBeenLooked() {
        return hasBeenLookedOnce;
    }

    public String look(Hunter hunter) {
        if (Boolean.parseBoolean(ATTRIBUTES.get("containsConsumables")) && !hasBeenLookedOnce) {
            addConsumablesToHunterInventory(hunter);
        }
        hasBeenLookedOnce = true;
        return DESCRIPTION;
    }

    public void addConsumablesToHunterInventory(Hunter hunter) {
        if (ATTRIBUTES.get("vialsContained") != null) {
            hunter.addVials(Integer.parseInt(ATTRIBUTES.get("vialsContained")));
        }
        if (ATTRIBUTES.get("bulletsContained") != null) {
            hunter.addBullets(Integer.parseInt(ATTRIBUTES.get("bulletsContained")));
        }
    }

    public String activate(Hunter hunter) {
        String txt;
        if (Boolean.parseBoolean(ATTRIBUTES.get("isActionable"))) {
            if (Boolean.parseBoolean(ATTRIBUTES.get("isTrapped"))) {
                if (!hasBeenActivated) {
                    hasBeenActivated = true;
                    hunter.takeDamage(Integer.parseInt(ATTRIBUTES.get("damageDone")));
                    if (hunter.isDead()) {
                        txt = ATTRIBUTES.get("deathDescription");
                    } else {
                        addConsumablesToHunterInventory(hunter);
                        txt = ATTRIBUTES.get("activationText");
                    }
                } else {
                    txt = "You already activated this";
                }
            } else {
                if (!hasBeenActivated) {
                    hasBeenActivated = true;
                    txt = ATTRIBUTES.get("activationText");
                } else {
                    txt = "You already activated this";
                }
            }
        } else {
            txt = "You can't activate this.";
        }
        return txt;
    }
}
