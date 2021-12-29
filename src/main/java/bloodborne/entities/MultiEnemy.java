package bloodborne.entities;

import java.util.Map;

public class MultiEnemy extends Enemy{

    public MultiEnemy(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    //TODO override attack method
}
