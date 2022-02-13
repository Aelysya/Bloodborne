package bloodborne.world;

import bloodborne.entities.Boss;
import bloodborne.entities.Enemy;
import bloodborne.entities.Entity;
import bloodborne.npcs.NPC;
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
    private final String HEADSTONE;
    private final int HEADSTONE_INDEX;
    private final String DESCRIPTION;
    private final String ALT_DESCRIPTION;
    private final boolean HAS_LANTERN;
    private boolean showAltDescription;
    private boolean hasBeenVisited;

    private final Map<String, String> ITEMS_STRING;
    private final Map<String, String> PROPS_STRING;
    private final Map<String, Object> EXITS_OBJECT;
    private final Map<String, String> ENEMIES_STRING;
    private final Map<String, String> NPCS_STRING;

    private final HashMap<String, Item> ITEMS;
    private final HashMap<String, Prop> PROPS;
    private final HashMap<String, Exit> EXITS;
    private final HashMap<String, Entity> ENEMIES;
    private final HashMap<String, NPC> NPCS;

    public Place(String id, String name, String zone, String headstone, int headstoneIndex, String description, String altDescription, boolean hasLantern, Map<String, String> items, Map<String, String> props, Map<String, Object> exits, Map<String, String> enemies, Map<String, String> npcs) {
        ID = id;
        NAME = name;
        ZONE = zone;
        HEADSTONE = headstone;
        HEADSTONE_INDEX = headstoneIndex;
        DESCRIPTION = description;
        ALT_DESCRIPTION = altDescription;
        HAS_LANTERN = hasLantern;
        showAltDescription = false;
        hasBeenVisited = false;

        ITEMS_STRING = items;
        PROPS_STRING = props;
        EXITS_OBJECT = exits;
        ENEMIES_STRING = enemies;
        NPCS_STRING = npcs;

        ITEMS = new HashMap<>();
        PROPS = new HashMap<>();
        EXITS = new HashMap<>();
        ENEMIES = new HashMap<>();
        NPCS = new HashMap<>();
    }

    public void initialize(World world) throws MalFormedJsonException, UnknownPlaceException, UnknownExitTypeException, ItemNotFoundException, NPCNotFoundException {
        for (Map.Entry<String, String> entry : ITEMS_STRING.entrySet()) {
            String itemName = entry.getKey();
            Item item = world.getItemById(entry.getValue());
            if (item == null) {
                throw new ItemNotFoundException(itemName + " is not present in the world for place : " + NAME);
            } else {
                ITEMS.put(itemName, item);
            }
        }
        for (Map.Entry<String, String> entry : ENEMIES_STRING.entrySet()) {
            String enemyName = entry.getKey();
            Enemy enemy = world.getEnemyById(entry.getValue());
            if (enemy == null) {
                throw new NPCNotFoundException(enemyName + " is not present in the world for place : " + NAME);
            } else {
                ENEMIES.put(enemyName, enemy);
            }
        }
        for (Map.Entry<String, String> entry : PROPS_STRING.entrySet()) {
            String propName = entry.getKey();
            Prop prop = world.getPropById(entry.getValue());
            if (prop == null) {
                throw new MalFormedJsonException("the prop : " + entry.getValue() + " doesn't exist");
            } else {
                PROPS.put(propName, prop);
            }
        }
        for (Map.Entry<String, String> entry : NPCS_STRING.entrySet()) {
            String npcName = entry.getKey();
            NPC npc = world.getNpcById(entry.getValue());
            if (npc == null) {
                throw new MalFormedJsonException("the npc : " + entry.getValue() + " doesn't exist");
            } else {
                NPCS.put(npcName, npc);
            }
        }
        for (Map.Entry<String, Object> entry : EXITS_OBJECT.entrySet()) {
            String exitDirection = entry.getKey();
            Map<String, Object> exitAttributes = (Map) entry.getValue();
            EXITS.put(exitDirection, WorldLoader.createExit(exitAttributes, world, (String) exitAttributes.get("destination")));
        }
    }

    public void switchToAltDescription() {
        showAltDescription = true;
    }

    public void visit() {
        hasBeenVisited = true;
    }

    public boolean isBossArena() {
        boolean hasBoss = false;
        for (Entity e : ENEMIES.values()) {
            if (e instanceof Boss) {
                hasBoss = true;
                break;
            }
        }
        return hasBoss;
    }

    public Boss getBoss() {
        Boss boss = null;
        for (Entity e : ENEMIES.values()) {
            if (e instanceof Boss) {
                boss = (Boss) e;
                break;
            }
        }
        return boss;
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

    public String getHEADSTONE() {
        return HEADSTONE;
    }

    public int getHEADSTONE_INDEX() {
        return HEADSTONE_INDEX;
    }

    public String getDESCRIPTION() {
        StringBuilder s = new StringBuilder();
        s.append(showAltDescription ? ALT_DESCRIPTION : DESCRIPTION).append("\n\n");
        for (Exit e : EXITS.values()) {
            if (!e.getDescription().equals("")) {
                s.append(e.getDescription()).append("\n");
            }
        }
        return s.toString();
    }

    public String getIMAGE() {
        StringBuilder s = new StringBuilder();
        s.append("zones/").append(ZONE).append("/").append(ID);
        if (showAltDescription) {
            s.append("-alt");
        }
        s.append(".jpg");
        return s.toString();
    }

    public String getSONG() {
        return ZONE + ".wav";
    }

    public boolean hasBeenVisited() {
        return hasBeenVisited;
    }

    public boolean hasLantern() {
        return HAS_LANTERN;
    }

    public HashMap<String, Item> getITEMS() {
        return ITEMS;
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

    public HashMap<String, NPC> getNPCS() {
        return NPCS;
    }

    public Item getItemByName(String itemName) {
        Item item = null;
        String iName;
        for (Item i : ITEMS.values()) {
            iName = i.getNAME().toLowerCase(Locale.ROOT);
            if (iName.equals(itemName.toLowerCase(Locale.ROOT))) {
                item = i;
                break;
            }
        }
        return item;
    }

    public Prop getPropByName(String propName) {
        return PROPS.get(propName);
    }

    public Exit getExitByName(String direction) {
        return EXITS.get(direction);
    }

    public Entity getEnemyByName(String target) {
        return ENEMIES.get(target);
    }

    public NPC getNpcByName(String npcName) {
        return NPCS.get(npcName);
    }

    public void removeItemByID(String itemID) {
        for (Item i : ITEMS.values()) {
            if (i.getID().equals(itemID)) {
                ITEMS.remove(i.getNAME().toLowerCase(Locale.ROOT));
                System.out.println("Removed item " + i.getID() + " from place " + getID());
                break;
            }
        }
    }
}
