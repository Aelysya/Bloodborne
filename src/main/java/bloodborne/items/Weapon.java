package bloodborne.items;

import java.util.Map;

public abstract class Weapon extends Item{

    protected final int DAMAGE;

    public Weapon(String id, String description, Map<String, String> att) {
        super(id, description, att);
        DAMAGE = Integer.parseInt(att.get("damage"));
    }

    public String getIcon(){
        return ATTRIBUTES.get("imagePath");
    }

    public abstract int getCurrentDamage();
}
