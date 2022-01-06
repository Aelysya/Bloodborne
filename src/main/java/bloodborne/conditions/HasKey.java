package bloodborne.conditions;

import bloodborne.world.World;

public class HasKey extends Condition{

    private final String ITEM_ID;

    public HasKey(World world, String itemId) {
        super(world);
        ITEM_ID = itemId;
    }

    @Override
    public boolean checkCondition() {
        return WORLD.getHUNTER().hasItem(ITEM_ID);
    }
}
