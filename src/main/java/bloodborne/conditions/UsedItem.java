package bloodborne.conditions;

import bloodborne.world.World;

public class UsedItem extends Condition{

    private final String ITEM_ID;

    public UsedItem(World world, String itemId) {
        super(world);
        this.ITEM_ID = itemId;
    }

    @Override
    public boolean checkCondition() {
        return WORLD.getItemById(this.ITEM_ID).isUsed();
    }
}
