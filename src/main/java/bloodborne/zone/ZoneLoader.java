package bloodborne.zone;

import bloodborne.entities.Enemy;
import bloodborne.environment.Prop;
import bloodborne.exceptions.*;
import bloodborne.items.Item;
import bloodborne.json.ZoneDataLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ZoneLoader {

    public static void loadZone(String zoneName, Zone zone) {
        try {
            loadItems(zoneName, zone);
            loadEnemies(zoneName, zone);
            loadProps(zoneName, zone);
            loadPlaces(zoneName, zone);
            zone.changePlace(zone.getPlaceByName(Zone.STARTING_LOCATION));

            initializeProps(zone);
            initializePlaces(zone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadItems(String zoneName, Zone zone) throws MalFormedJsonException, ReflectionException, IOException {
        Reader reader = ZoneDataLoader.getItems(zoneName);
        Type itemMapType = new TypeToken<List<Map<String, Object>>>() {}.getType();
        List<Map<String, Object>> parsedJson = new Gson().fromJson(reader, itemMapType);
        HashMap<String, Object> loadedItems = loadElement(parsedJson, "items", zone);

        for (Object item : loadedItems.values()) {
            zone.addItem((Item) item);
        }
    }

    private static void loadEnemies(String zoneName, Zone zone) throws MalFormedJsonException, ReflectionException, IOException{
        Reader reader = ZoneDataLoader.getNPCs(zoneName);
        Type enemiesMapType = new TypeToken<List<Map<String, Object>>>() {}.getType();
        List<Map<String, Object>> parsedJson = new Gson().fromJson(reader, enemiesMapType);
        HashMap<String, Object> loadedEnemies = loadElement(parsedJson, "entities", zone);

        for (Object enemy : loadedEnemies.values()) {
            zone.addEnemy((Enemy) enemy);
        }
    }

    private static void loadProps(String zoneName, Zone zone) throws IOException, ReflectionException, MalFormedJsonException {
        Reader reader = ZoneDataLoader.getProps(zoneName);
        Type propMapType = new TypeToken<List<Map<String, Object>>>() {}.getType();
        List<Map<String, Object>> parsedJson = new Gson().fromJson(reader, propMapType);
        HashMap<String, Object> loadedProps = loadElement(parsedJson, "environment", zone);

        for (Object prop : loadedProps.values()) {
            zone.addProp((Prop) prop);
        }
    }

    private static void loadPlaces(String zoneName, Zone zone) throws MalFormedJsonException, ReflectionException, IOException {
        Reader reader = ZoneDataLoader.getPlaces(zoneName);
        Type placeMapType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> parsedJson = new Gson().fromJson(reader, placeMapType);

        for (Map<String, Object> map : parsedJson) {
            String name = (String) map.get("name");
            String description = (String) map.get("description");
            String imagePath = (String) map.get("imagePath");
            String songPath = (String) map.get("songPath");
            Map items = (Map) map.get("items");
            Map props = (Map) map.get("props");
            Map exits = (Map) map.get("exits");
            Map enemies = (Map) map.get("NPCs");

            if (name == null || description == null || imagePath == null || songPath == null) {
                throw new MalFormedJsonException("Place : " + name + description + imagePath + songPath);
            }

            Place place = new Place(name, description, imagePath, songPath, items, props, exits, enemies);
            zone.addPlace(place);
        }
    }

    private static HashMap<String, Object> loadElement(List<Map<String, Object>> parsedJson, String elementType, Zone zone) throws MalFormedJsonException, ReflectionException {
        HashMap<String, Object> loadedElements = new HashMap<>();

        for (Map<String, Object> map : parsedJson) {
            String id = (String) map.get("id");
            String type = (String) map.get("type");
            String description = (String) map.get("description");
            Object attributes = map.get("attributes");

            if (id == null || type == null || description == null)
                throw new MalFormedJsonException(elementType + id + type + description);

            try {
                Class<?> elementClass = Class.forName("bloodborne." + elementType + "." + type);
                Constructor<?> elementConstructor = elementClass.getConstructor(String.class, String.class, Map.class);
                Object newElement = elementConstructor.newInstance(id, description, attributes);
                loadedElements.put(id, newElement);

            } catch (ClassNotFoundException e) {
                throw new ReflectionException("Class not found for : " + elementType + ": " + type);
            } catch (NoSuchMethodException e) {
                throw new ReflectionException("Constructor (String id, String description, Map<String, String> attributes) not found in : " + type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return loadedElements;
    }

    private static void initializeProps(Zone zone) throws ReflectionException, MalFormedJsonException {
        for (Prop prop : zone.getProps().values()) {
            prop.initialize(zone);
        }
    }

    private static void initializePlaces(Zone zone) throws MalFormedJsonException, UnknownPlaceException, UnknownExitTypeException, ItemNotFoundException, NPCNotFoundException, ReflectionException {
        for (Place place : zone.getPlaces().values()) {
            place.initialize(zone);
        }
        for (Place place : zone.getPlaces().values()) {
            for (Exit exit : place.getEXITS().values()) {
                exit.initialize(zone);
            }
        }
    }

    public static Exit createExit(Map<String, Object> exitData, Zone zone, String destinationId) throws  UnknownPlaceException, UnknownExitTypeException {
        String type = (String) exitData.get("type");
        Map<String, String> exitAttributes = (Map) exitData.get("attributes");
        Place destination = zone.getPlaceByName(destinationId);
        if (destination == null) {
            throw new UnknownPlaceException(" for place : " + destinationId);
        }
        if(type.equals("Exit")) {
            return new Exit(destination, exitAttributes);
        } else if(type.equals("ConditionalExit")) {
            return new ConditionalExit(destination, exitAttributes);
        } else {
            throw new UnknownExitTypeException("type : " + type);
        }
    }
}
