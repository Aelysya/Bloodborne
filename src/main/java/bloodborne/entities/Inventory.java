package bloodborne.entities;

import bloodborne.items.Item;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Inventory {

    private final Map<Item, Integer> ITEMS;

    public Inventory() {
        ITEMS = new HashMap<>();
    }

    public void addItem(Item item) {
        if (ITEMS.get(item) == null) {
            ITEMS.put(item, 1);
        } else {
            ITEMS.replace(item, ITEMS.get(item) + 1);
        }
    }

    public void removeOneItemFromStack(Item item) {
        String id = item.getID();
        for (Item i : ITEMS.keySet()) {
            if (i.getID().equals(id)) {
                if (ITEMS.get(i) == 1) {
                    ITEMS.remove(i);
                } else {
                    ITEMS.replace(i, ITEMS.get(i)-1);
                }
                break;
            }
        }
    }

    public void removeStackFromInventory(Item item) {
        String id = item.getID();
        for (Item i : ITEMS.keySet()) {
            if (i.getID().equals(id)) {
                ITEMS.remove(i);
                break;
            }
        }
    }

    public Map<Item, Integer> getItems() {
        return ITEMS;
    }

    public Item getItemByName(String itemName) {
        Item item = null;
        String iName;
        for (Item i : ITEMS.keySet()) {
            iName = i.getNAME().toLowerCase(Locale.ROOT);
            if (iName.equals(itemName)) {
                item = i;
                break;
            }
        }
        return item;
    }

    public int getNumberOfItems() {
        return ITEMS.size();
    }

    public boolean hasItem(Item item) {
        String id = item.getID();
        boolean hasItem = false;
        for (Item i : ITEMS.keySet()) {
            if (i.getID().equals(id)) {
                hasItem = true;
                break;
            }
        }
        return hasItem;
    }
}
