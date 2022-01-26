package bloodborne.environment;

import java.util.Map;

public class Headstone extends Prop{

    public Headstone(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public String getHeadstoneSimpleName(){
        String[] cutId = getID().split("-", 2);
        return cutId[0];
    }
}
