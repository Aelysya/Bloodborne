package bloodborne.entities;

import bloodborne.items.Item;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Inventory {

    private final Map<String, Item> ITEMS;

    public Inventory(){
        ITEMS = new HashMap<>();
    }

    public void addItem(Item item){
        ITEMS.put(item.getID(), item);
    }

    public void removeItem(Item item){
        ITEMS.remove(item.getID());
    }

    public void removeItemByName(String itemName) {
        for(Item i : ITEMS.values()){
            if(i.getNAME().equals(itemName)){
                ITEMS.remove(i.getID());
                break;
            }
        }
    }

    public Map<String, Item> getItems(){
        return ITEMS;
    }

    public Item getItemByName(String itemName){
        Item item = null;
        String iName;
        for(Item i : ITEMS.values()){
            iName = i.getNAME().toLowerCase(Locale.ROOT);
            if(iName.equals(itemName)){
                item = i;
                break;
            }
        }
        return item;
    }

    public Item getItemById(String itemId){
        return ITEMS.get(itemId);
    }

    public int getNumberOfItems(){
        return ITEMS.size();
    }

    public boolean hasItem(Item item){
        return ITEMS.containsKey(item.getID());
    }
}
