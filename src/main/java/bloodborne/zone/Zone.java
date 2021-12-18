package bloodborne.zone;

import bloodborne.entities.Enemy;
import bloodborne.entities.Hunter;
import bloodborne.environment.Prop;
import bloodborne.items.Item;

import java.util.HashMap;
import java.util.Map;

public class Zone {

    public static final String STARTING_LOCATION = "clinic"; //TODO make player chose the zone at the beginning

    private final Map<String, Item> ITEMS;
    private final Map<String, Prop> PROPS;
    private final Map<String, Enemy> ENEMIES;
    private final Map<String, Place> PLACES;

    private Place currentPlace;
    private final Hunter HUNTER;


    public Zone(Hunter hunter) {
        ITEMS = new HashMap<>();
        PROPS = new HashMap<>();
        ENEMIES = new HashMap<>();
        PLACES = new HashMap<>();

        HUNTER = hunter;
    }

    public Hunter getHUNTER(){
        return HUNTER;
    }

    public Place getCurrentPlace(){
        return currentPlace;
    }

    public void changePlace(Place newPlace){
        currentPlace = newPlace;
    }

    public void addItem(Item item) {
        this.ITEMS.put(item.getID(), item);
    }

    public void addProp(Prop prop) {
        this.PROPS.put(prop.getID(), prop);
    }

    public void addEnemy(Enemy enemy) {
        this.ENEMIES.put(enemy.getID(), enemy);
    }

    public void addPlace(Place place) {
        this.PLACES.put(place.getNAME(), place);
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

    public Enemy getEnemyById(String enemyId) {
        return ENEMIES.get(enemyId);
    }

    public Item getItemById(String itemId) {
        return ITEMS.get(itemId);
    }

    public Place getPlaceByName(String placeName) {
        return this.PLACES.get(placeName);
    }

    public Prop getPropById(String propId) {
        return PROPS.get(propId);
    }
}
