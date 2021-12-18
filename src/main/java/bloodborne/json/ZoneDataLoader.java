package bloodborne.json;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class ZoneDataLoader {

    public static Reader getItems(String zoneName) {
        return getAsReader(zoneName, "Items");
    }

    public static Reader getNPCs(String zoneName) {
        return getAsReader(zoneName, "NPCs");
    }

    public static Reader getPlaces(String zoneName) {
        return getAsReader(zoneName, "Places");
    }

    public static Reader getProps(String zoneName) {
        return getAsReader(zoneName, "Props");
    }

    private static Reader getAsReader(String zoneName, String resource) {
        String path = String.format("%s/%s.json", zoneName, resource);

        return new InputStreamReader(Objects.requireNonNull(ZoneDataLoader.class.getResourceAsStream(path)));
    }
}
