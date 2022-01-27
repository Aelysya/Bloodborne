package bloodborne.json;

import bloodborne.exceptions.ReflectionException;
import bloodborne.world.World;

public interface Initializable {

    void initialize(World world) throws ReflectionException;
}
