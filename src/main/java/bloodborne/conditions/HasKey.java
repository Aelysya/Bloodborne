package bloodborne.conditions;

import bloodborne.zone.Zone;

public class HasKey extends Condition{

    private final String ITEM_ID;

    public HasKey(Zone zone, String itemId) {
        super(zone);
        ITEM_ID = itemId;
    }

    @Override
    public boolean checkCondition() {
        return ZONE.getHUNTER().hasItem(ITEM_ID);
    }
}
