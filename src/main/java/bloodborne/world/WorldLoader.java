package bloodborne.world;

import bloodborne.entities.Enemy;
import bloodborne.environment.ConditionalProp;
import bloodborne.environment.Container;
import bloodborne.npcs.NPC;
import bloodborne.environment.Prop;
import bloodborne.exceptions.*;
import bloodborne.items.Item;
import bloodborne.json.WorldDataLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WorldLoader {

    public static void loadZone(String zoneName, World world) {
        try {
            loadItems(zoneName, world);
            loadEnemies(zoneName, world);
            loadProps(zoneName, world);
            loadNpcs(zoneName, world);
            loadPlaces(zoneName, world);
            world.changePlace(world.getPlaceById(World.STARTING_LOCATION));

            initializeProps(world);
            initializePlaces(world);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadItems(String zoneName, World world) throws MalFormedJsonException, ReflectionException {
        Reader reader = WorldDataLoader.getItems(zoneName);
        Type itemMapType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> parsedJson = new Gson().fromJson(reader, itemMapType);
        HashMap<String, Object> loadedItems = loadElement(parsedJson, "items");

        for (Object item : loadedItems.values()) {
            world.addItem((Item) item);
        }
    }

    private static void loadEnemies(String zoneName, World world) throws MalFormedJsonException, ReflectionException {
        Reader reader = WorldDataLoader.getEnemies(zoneName);
        Type enemiesMapType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> parsedJson = new Gson().fromJson(reader, enemiesMapType);
        HashMap<String, Object> loadedEnemies = loadElement(parsedJson, "entities");

        for (Object enemy : loadedEnemies.values()) {
            world.addEnemy((Enemy) enemy);
        }
    }

    private static void loadProps(String zoneName, World world) throws ReflectionException, MalFormedJsonException {
        Reader reader = WorldDataLoader.getProps(zoneName);
        Type propMapType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> parsedJson = new Gson().fromJson(reader, propMapType);
        HashMap<String, Object> loadedProps = loadElement(parsedJson, "environment");

        for (Object prop : loadedProps.values()) {
            world.addProp((Prop) prop);
        }
    }

    private static void loadNpcs(String zoneName, World world) throws ReflectionException, MalFormedJsonException {
        Reader reader = WorldDataLoader.getNpcs(zoneName);
        Type npcMapType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> parsedJson = new Gson().fromJson(reader, npcMapType);
        HashMap<String, Object> loadedNpcs = loadElement(parsedJson, "npcs");

        for (Object npc : loadedNpcs.values()) {
            world.addNpc((NPC) npc);
        }
    }

    private static void loadPlaces(String zoneName, World world) throws MalFormedJsonException {
        Reader reader = WorldDataLoader.getPlaces(zoneName);
        Type placeMapType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        List<Map<String, Object>> parsedJson = new Gson().fromJson(reader, placeMapType);

        for (Map<String, Object> map : parsedJson) {
            String id = (String) map.get("id");
            String name = (String) map.get("name");
            String area = (String) map.get("area");
            String headstone = (String) map.get("headstone");
            String headstoneIndexString = (String) map.get("headstoneIndex");
            String description = (String) map.get("description");
            String altDescription = (String) map.get("altDescription");
            boolean hasLantern = Boolean.parseBoolean((String) map.get("hasLantern"));
            Map items = (Map) map.get("items");
            Map props = (Map) map.get("props");
            Map exits = (Map) map.get("exits");
            Map enemies = (Map) map.get("enemies");
            Map npcs = (Map) map.get("npcs");

            if (id == null || name == null || area == null || headstone == null || description == null || altDescription == null) {
                throw new MalFormedJsonException("Place : " + id + name + area + description + altDescription);
            }
            int headstoneIndex = headstoneIndexString == null ? -1 : Integer.parseInt(headstoneIndexString);

            Place place = new Place(id, name, area, headstone, headstoneIndex, description, altDescription, hasLantern, items, props, exits, enemies, npcs);
            world.addPlace(place);
            if (hasLantern) {
                world.addLanternPlace(place);
            }
        }
    }

    private static HashMap<String, Object> loadElement(List<Map<String, Object>> parsedJson, String elementType) throws MalFormedJsonException, ReflectionException {
        HashMap<String, Object> loadedElements = new HashMap<>();

        for (Map<String, Object> map : parsedJson) {
            String id = (String) map.get("id");
            String type = (String) map.get("type");
            String description = (String) map.get("description");
            Map<String, String> attributes = (Map<String, String>) map.get("attributes");

            if (id == null || type == null || description == null) {
                throw new MalFormedJsonException(elementType + id + type + description);
            }
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

    private static void initializeProps(World world) throws ReflectionException {
        for (Prop prop : world.getProps().values()) {
            if (prop instanceof Container) {
                ((Container) prop).initialize(world);
            } else if (prop instanceof ConditionalProp) {
                ((ConditionalProp) prop).initialize(world);
            }
        }
    }

    private static void initializePlaces(World world) throws MalFormedJsonException, UnknownPlaceException, UnknownExitTypeException, ItemNotFoundException, NPCNotFoundException, ReflectionException {
        for (Place place : world.getPlaces().values()) {
            place.initialize(world);
        }
        for (Place place : world.getPlaces().values()) {
            for (Exit exit : place.getEXITS().values()) {
                exit.initialize(world);
            }
        }
    }

    public static Exit createExit(Map<String, Object> exitData, World world, String destinationId) throws UnknownPlaceException, UnknownExitTypeException {
        String type = (String) exitData.get("type");
        Map<String, String> exitAttributes = (Map) exitData.get("attributes");
        Place destination = world.getPlaceById(destinationId);
        if (destination == null) {
            throw new UnknownPlaceException(" for place : " + destinationId);
        }
        if (type.equals("Exit")) {
            return new Exit(destination, exitAttributes);
        } else if (type.equals("ConditionalExit")) {
            return new ConditionalExit(destination, exitAttributes);
        } else {
            throw new UnknownExitTypeException("type : " + type);
        }
    }
}
