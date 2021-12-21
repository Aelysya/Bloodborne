package bloodborne.environment;

import bloodborne.entities.Hunter;
import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.exceptions.ReflectionException;
import bloodborne.zone.Zone;
import java.util.Map;

public class Prop {

    private final String ID;
    private final String DESCRIPTION;
    private boolean isActivated;
    private boolean hasBeenLooked;
    protected final Map<String, String> ATTRIBUTES;

    public Prop(String id, String description, Map<String, String> att){
        ID = id;
        DESCRIPTION = description;
        isActivated = false;
        hasBeenLooked = false;
        ATTRIBUTES = att;
    }

    public void initialize(Zone zone)  throws ReflectionException, MalFormedJsonException {}

    public String getID() {
        return ID;
    }

    public String getNAME() {
        return ATTRIBUTES.get("name");
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getACTIVATION_TEXT(){
        return ATTRIBUTES.get("activationText");
    }

    public boolean isActivated() {
        return isActivated;
    }

    public boolean hasBeenLooked(){
        return hasBeenLooked;
    }

    public String lookReaction(Hunter hunter){
        if(ATTRIBUTES.get("isContainer") == null && !hasBeenLooked){
            addConsumables(hunter);
            hasBeenLooked = true;
        }
        return getDESCRIPTION();
    }

    public String activate(Hunter hunter){
        String txt;
        if(ATTRIBUTES.get("isActionable").equals("true")) {
            if(!(ATTRIBUTES.get("isTrapped") == null)){
                if (!isActivated) {
                    isActivated = true;
                    hunter.takeDamage(Integer.parseInt(ATTRIBUTES.get("damageDone")));
                    if(hunter.isDead()){
                        txt = ATTRIBUTES.get("deathDescription");
                    } else {
                        addConsumables(hunter);
                        txt = ATTRIBUTES.get("activationText");
                    }
                } else {
                    txt = "You already activated this";
                }
            } else {
                if (!isActivated) {
                    isActivated = true;
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

    public void addConsumables(Hunter hunter){
        if(ATTRIBUTES.get("vialsContained") != null){
            hunter.addVials(Integer.parseInt(ATTRIBUTES.get("vialsContained")));
        }
        if(ATTRIBUTES.get("bulletsContained") != null){
            hunter.addBullets(Integer.parseInt(ATTRIBUTES.get("bulletsContained")));
        }
    }
}
