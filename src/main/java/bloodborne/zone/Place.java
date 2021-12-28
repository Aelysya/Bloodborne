package bloodborne.zone;

import bloodborne.entities.Enemy;
import bloodborne.entities.Entity;
import bloodborne.environment.Prop;
import bloodborne.exceptions.*;
import bloodborne.items.Item;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Place {

    private final String NAME;
    private final String DESCRIPTION;
    private final String IMAGE_PATH;
    private final String SONG_PATH;

    private final Map<String, String> ITEMS_STRING;
    private final Map<String, String> PROPS_STRING;
    private final Map<String, Object> EXITS_OBJECT;
    private final Map<String, String> NPCS_STRING;

    private final HashMap<String, Item> ITEMS;
    private final HashMap<String, Prop> PROPS;
    private final HashMap<String, Exit> EXITS;
    private final HashMap<String, Entity> NPCS;

    private boolean showAltDescription;

    public Place(String name, String description, String imagePath, String songPath, Map<String, String> items, Map<String, String> props, Map<String, Object> exits, Map<String, String> npcs) {
        NAME = name;
        DESCRIPTION = description;
        IMAGE_PATH = imagePath;
        SONG_PATH = songPath;

        ITEMS_STRING = items;
        PROPS_STRING = props;
        EXITS_OBJECT = exits;
        NPCS_STRING = npcs;

        ITEMS = new HashMap<>();
        PROPS = new HashMap<>();
        EXITS = new HashMap<>();
        NPCS = new HashMap<>();

        showAltDescription = false;
    }

    public String getNAME() {
        return NAME;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getIMAGE_PATH() {
        return IMAGE_PATH;
    }

    public String getSONG_PATH() {
        return SONG_PATH;
    }

    public HashMap<String, Prop> getPROPS() {
        return PROPS;
    }

    public HashMap<String, Exit> getEXITS() {
        return EXITS;
    }

    public HashMap<String, Entity> getNPCS() {
        return NPCS;
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

    public Entity getNpcByName(String target) {
        return NPCS.get(target);
    }

    public void switchToAltDescription(){
        showAltDescription = true;
    }

    public void initialize(Zone zone) throws MalFormedJsonException, UnknownPlaceException, UnknownExitTypeException, ItemNotFoundException, NPCNotFoundException {
        for (Map.Entry<String, String> entry : ITEMS_STRING.entrySet()) {
            String itemName = entry.getKey();
            String itemIdName = entry.getValue();
            Item item = zone.getItemById(itemIdName);
            if (item == null){
                throw new ItemNotFoundException(itemName + " is not present in the zone for place : " + NAME);
            } else {
                ITEMS.put(itemName, item);
            }
        }
        for (Map.Entry<String, String> entry : NPCS_STRING.entrySet()) {
            String npcName = entry.getKey();
            String npcIdName = entry.getValue();
            Enemy enemy = zone.getEnemyById(npcIdName);
            if (enemy == null) {
                throw new NPCNotFoundException(npcIdName + " is not present in the zone for place : " + NAME);
            }
            NPCS.put(npcName, enemy);
        }
        for (Map.Entry<String, String> entry : PROPS_STRING.entrySet()) {
            String propName = entry.getKey();
            Prop prop = zone.getPropById(entry.getValue());
            if (prop == null) {
                throw new MalFormedJsonException("the prop : " + entry.getValue() + " doesn't exist");
            }
            PROPS.put(propName, prop);

        }
        for (Map.Entry<String, Object> entry : EXITS_OBJECT.entrySet()) {
            String exitDirection = entry.getKey();
            Map<String, Object> exitAttributes = (Map) entry.getValue();
            EXITS.put(exitDirection, ZoneLoader.createExit(exitAttributes, zone, (String) exitAttributes.get("destination")));
        }
    }
}
