package bloodborne.json;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class WorldDataLoader {

    public static Reader getItems(String zoneName) {
        return getAsReader(zoneName, "Items");
    }

    public static Reader getEnemies(String zoneName) {
        return getAsReader(zoneName, "Enemies");
    }

    public static Reader getPlaces(String zoneName) {
        return getAsReader(zoneName, "Places");
    }

    public static Reader getProps(String zoneName) {
        return getAsReader(zoneName, "Props");
    }

    public static Reader getNpcs(String zoneName) {
        return getAsReader(zoneName, "Npcs");
    }

    private static Reader getAsReader(String zoneName, String resource) {
        String path = String.format("%s/%s.json", zoneName, resource);
        return new InputStreamReader(Objects.requireNonNull(WorldDataLoader.class.getResourceAsStream(path)));
    }
}
