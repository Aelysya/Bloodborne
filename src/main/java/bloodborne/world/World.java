package bloodborne.world;

import bloodborne.entities.Enemy;
import bloodborne.entities.Hunter;
import bloodborne.npcs.NPC;
import bloodborne.environment.Prop;
import bloodborne.items.Item;

import java.util.HashMap;
import java.util.Map;

public class World {

    public static final String STARTING_LOCATION = "clinic";

    private final Map<String, Item> ITEMS;
    private final Map<String, Prop> PROPS;
    private final Map<String, Enemy> ENEMIES;
    private final Map<String, NPC> NPCS;
    private final Map<String, Place> PLACES;
    private final Map<String, Place> LANTERN_PLACES;

    private Place currentPlace;
    private final Hunter HUNTER;

    public World(Hunter hunter) {
        ITEMS = new HashMap<>();
        PROPS = new HashMap<>();
        ENEMIES = new HashMap<>();
        NPCS = new HashMap<>();
        PLACES = new HashMap<>();
        LANTERN_PLACES = new HashMap<>();

        HUNTER = hunter;
    }

    public Hunter getHUNTER() {
        return HUNTER;
    }

    public Place getCurrentPlace() {
        return currentPlace;
    }

    public void changePlace(Place newPlace) {
        currentPlace = newPlace;
        currentPlace.visit();
    }

    public void addItem(Item item) {
        this.ITEMS.put(item.getID(), item);
    }

    public void addProp(Prop prop) {
        this.PROPS.put(prop.getID(), prop);
    }

    public void addNpc(NPC npc) {
        this.NPCS.put(npc.getID(), npc);
    }

    public void addEnemy(Enemy enemy) {
        this.ENEMIES.put(enemy.getID(), enemy);
    }

    public void addPlace(Place place) {
        this.PLACES.put(place.getID(), place);
    }

    public void addLanternPlace(Place place) {
        this.LANTERN_PLACES.put(place.getID(), place);
    }

    public Map<String, Item> getItems() {
        return ITEMS;
    }

    public Map<String, Prop> getProps() {
        return PROPS;
    }

    public Map<String, Enemy> getEnemies() {
        return ENEMIES;
    }

    public Map<String, Place> getPlaces() {
        return PLACES;
    }

    public Map<String, Place> getLanternPlaces() {
        return LANTERN_PLACES;
    }

    public Enemy getEnemyById(String enemyId) {
        return ENEMIES.get(enemyId);
    }

    public Item getItemById(String itemId) {
        return ITEMS.get(itemId);
    }

    public Place getPlaceById(String placeId) {
        return this.PLACES.get(placeId);
    }

    public Prop getPropById(String propId) {
        return PROPS.get(propId);
    }

    public NPC getNpcById(String npcId) {
        return NPCS.get(npcId);
    }

    public String generateHeadstoneText(String headstoneName) {
        StringBuilder s = new StringBuilder();
        for (Place p : LANTERN_PLACES.values()) {
            if (headstoneName.equals(p.getHEADSTONE()) && p.hasBeenVisited()) {
                s.append(p.getHEADSTONE_INDEX()).append(" -> ").append(p.getNAME()).append("\n");
            }
        }
        s.append("\nEnter the number corresponding to the destination you want to reawaken at or Cancel to stay in the dream");
        return s.toString();
    }
}
