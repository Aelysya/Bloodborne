package bloodborne.conditions;

import bloodborne.zone.Zone;

public class UsedItem extends Condition{

    private final String ITEM_ID;

    public UsedItem(Zone zone, String itemId) {
        super(zone);
        this.ITEM_ID = itemId;
    }

    @Override
    public boolean checkCondition() {
        return ZONE.getItemById(this.ITEM_ID).isUsed();
    }
}
