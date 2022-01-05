package bloodborne.items;

import java.util.Map;

public abstract class Weapon extends Item{

    public Weapon(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    public String getIcon(){
        return "weapons/" + getID() + ".png";
    }

    public abstract int getCurrentDamage();
}
