package bloodborne.entities;

import bloodborne.items.Item;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Inventory {

    private Map<String, Item> items;

    public Inventory(){
        items = new HashMap<>();
    }

    public void addItem(Item item){
        items.put(item.getID(), item);
    }

    public void removeItem(Item item){
        items.remove(item.getID());
    }

    public void removeItemByName(String itemName) {
        for(Item i : items.values()){
            if(i.getNAME().equals(itemName)){
                items.remove(i);
                break;
            }
        }
    }

    public String showInventory(){
        StringBuilder s = new StringBuilder();

        for(Item item: items.values()) {
            s.append(item.getNAME())
                    .append("\n");
        }

        return s.toString();
    }

    public Item getItemByName(String itemName){
        Item item = null;
        String iName;
        for(Item i : items.values()){
            iName = i.getNAME().toLowerCase(Locale.ROOT);
            if(iName.equals(itemName)){
                item = i;
                break;
            }
        }
        return item;
    }

    public Item getItemById(String itemId){
        return items.get(itemId);
    }

    public int getNumberOfItems(){
        return items.size();
    }

    public boolean hasItem(String itemId){
        return items.containsKey(itemId);
    }
}
