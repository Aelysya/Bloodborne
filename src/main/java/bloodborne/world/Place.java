package bloodborne.world;

import bloodborne.entities.Boss;
import bloodborne.entities.Enemy;
import bloodborne.entities.Entity;
import bloodborne.environment.Prop;
import bloodborne.exceptions.*;
import bloodborne.items.Item;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Place {

    private final String ID;
    private final String NAME;
    private final String ZONE;
    private final String DESCRIPTION;
    private final String ALT_DESCRIPTION;
    private boolean showAltDescription;

    private final Map<String, String> ITEMS_STRING;
    private final Map<String, String> PROPS_STRING;
    private final Map<String, Object> EXITS_OBJECT;
    private final Map<String, String> ENEMIES_STRING;

    private final HashMap<String, Item> ITEMS;
    private final HashMap<String, Prop> PROPS;
    private final HashMap<String, Exit> EXITS;
    private final HashMap<String, Entity> ENEMIES;

    public Place(String id, String name, String zone, String description, String altDescription, Map<String, String> items, Map<String, String> props, Map<String, Object> exits, Map<String, String> enemies) {
        ID = id;
        NAME = name;
        ZONE = zone;
        DESCRIPTION = description;
        ALT_DESCRIPTION = altDescription;
        showAltDescription = false;

        ITEMS_STRING = items;
        PROPS_STRING = props;
        EXITS_OBJECT = exits;
        ENEMIES_STRING = enemies;

        ITEMS = new HashMap<>();
        PROPS = new HashMap<>();
        EXITS = new HashMap<>();
        ENEMIES = new HashMap<>();
    }

    public String getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getZONE() {
        return ZONE;
    }

    public String getDESCRIPTION() {
        StringBuilder s = new StringBuilder();
        s.append(showAltDescription ? ALT_DESCRIPTION : DESCRIPTION);
        s.append("\n");
        for (Exit e : EXITS.values()){
            s.append(e.getDescription());
        }
        return s.toString();
    }

    public String getIMAGE() {
        return "zones/" + ZONE + "/" + ID + ".jpg";
    }

    public String getSONG() {
        return ZONE + ".wav";
    }

    public HashMap<String, Prop> getPROPS() {
        return PROPS;
    }

    public HashMap<String, Exit> getEXITS() {
        return EXITS;
    }

    public HashMap<String, Entity> getENEMIES() {
        return ENEMIES;
    }

    public Exit getExitByName(String direction) {
        return EXITS.get(direction);
    }

    public Prop getPropByName(String propName) {
        return PROPS.get(propName);
    }

    public Item getItemByName(String itemName) {
        Item item = null;
        String iName;
        for(Item i : ITEMS.values()){
            iName = i.getNAME().toLowerCase(Locale.ROOT);
            if(iName.equals(itemName)){
                item = i;
                break;
            }
        }
        return item;
    }

    public Entity getEnemyByName(String target) {
        return ENEMIES.get(target);
    }

    public Boss getBoss(){
        Boss boss = null;
        for (Entity e : ENEMIES.values()){
            if (e instanceof Boss){
                boss = (Boss) e;
                break;
            }
        }
        return boss;
    }

    public boolean isBossArena(){
        boolean hasBoss = false;
        for (Entity e : ENEMIES.values()){
            if (e instanceof Boss){
                hasBoss = true;
                break;
            }
        }
        return hasBoss;
    }

    public boolean hasLantern(){
        return false; //TODO Make lanterns respawns and ways to hunter's dream
    }

    public void switchToAltDescription(){
        showAltDescription = true;
    }

    public void initialize(World world) throws MalFormedJsonException, UnknownPlaceException, UnknownExitTypeException, ItemNotFoundException, NPCNotFoundException {
        for (Map.Entry<String, String> entry : ITEMS_STRING.entrySet()) {
            String itemName = entry.getKey();
            String itemIdName = entry.getValue();
            Item item = world.getItemById(itemIdName);
            if (item == null){
                throw new ItemNotFoundException(itemName + " is not present in the world for place : " + NAME);
            } else {
                ITEMS.put(itemName, item);
            }
        }
        for (Map.Entry<String, String> entry : ENEMIES_STRING.entrySet()) {
            String npcName = entry.getKey();
            String npcIdName = entry.getValue();
            Enemy enemy = world.getEnemyById(npcIdName);
            if (enemy == null) {
                throw new NPCNotFoundException(npcIdName + " is not present in the world for place : " + NAME);
            }
            ENEMIES.put(npcName, enemy);
        }
        for (Map.Entry<String, String> entry : PROPS_STRING.entrySet()) {
            String propName = entry.getKey();
            Prop prop = world.getPropById(entry.getValue());
            if (prop == null) {
                throw new MalFormedJsonException("the prop : " + entry.getValue() + " doesn't exist");
            }
            PROPS.put(propName, prop);

        }
        for (Map.Entry<String, Object> entry : EXITS_OBJECT.entrySet()) {
            String exitDirection = entry.getKey();
            Map<String, Object> exitAttributes = (Map) entry.getValue();
            EXITS.put(exitDirection, WorldLoader.createExit(exitAttributes, world, (String) exitAttributes.get("destination")));
        }
    }
}
