package bloodborne.conditions;

import bloodborne.exceptions.MalFormedJsonException;
import bloodborne.items.Item;
import bloodborne.world.World;

public class HasKey extends Condition {

    private final Item ITEM;

    public HasKey(World world, String itemId) throws MalFormedJsonException {
        super(world);
        ITEM = world.getItemById(itemId);
        if (ITEM == null) {
            throw new MalFormedJsonException("Prop not present in json files : " + itemId);
        }
    }

    @Override
    public boolean checkCondition() {
        return WORLD.getHUNTER().hasItem(ITEM);
    }
}
