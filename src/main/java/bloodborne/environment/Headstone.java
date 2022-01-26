package bloodborne.environment;

import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.exceptions.ReflectionException;
import bloodborne.world.World;

import java.util.Map;

public class Headstone extends Prop {

    public Headstone(String id, String description, Map<String, String> att) {
        super(id, description, att);
    }

    @Override
    public void initialize(World world) {

    }

    public String getHeadstoneSimpleName() {
        String[] cutId = getID().split("-", 2);
        return cutId[0];
    }
}
