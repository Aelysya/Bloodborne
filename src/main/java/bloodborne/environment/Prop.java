package bloodborne.environment;

import bloodborne.entities.Hunter;
import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.exceptions.ReflectionException;
import bloodborne.world.World;

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

    public void initialize(World world)  throws ReflectionException, MalFormedJsonException {}

    public String getID() {
        return ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public boolean hasBeenLooked(){
        return hasBeenLooked;
    }

    public String lookReaction(Hunter hunter){
        if(Boolean.parseBoolean(ATTRIBUTES.get("containsConsumables")) && !hasBeenLooked){
            addConsumables(hunter);
            hasBeenLooked = true;
        }
        return getDESCRIPTION();
    }

    public String activate(Hunter hunter){
        String txt;
        if(Boolean.parseBoolean(ATTRIBUTES.get("isActionable"))) {
            if(Boolean.parseBoolean(ATTRIBUTES.get("isTrapped"))){
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
