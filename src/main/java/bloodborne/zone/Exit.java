package bloodborne.zone;

import bloodborne.exceptions.ReflectionException;

import java.util.Map;

public class Exit {

    private final Place OUT;
    protected final Map<String, String> ATTRIBUTES;

    public Exit(Place out, Map<String, String> att){
        OUT = out;
        ATTRIBUTES = att;
    }

    public void initialize(Zone zone) throws ReflectionException{

    }

    public String getConditionFalseText(){
        return ATTRIBUTES.get("conditionFalseText") == null ? "" : ATTRIBUTES.get("conditionFalseText");
    }

    public boolean isTraversable(){
        return true;
    }

    public Place getOUT(){
        return OUT;
    }
}
