import java.util.*;

/**
 * A class to be used to store multiple Items (in a room or a player inventory)
 */
public class Items {
    private HashMap<String, Item> items;

    public Items()
    {
        items = new HashMap<>();
    }

    /**
     * @return An ArrayList of the Item objects in the collection.
     */
    public ArrayList<Item> getArrayList() {
        return new ArrayList<>(items.values());
    }

    /**
     * Remove the given item.
     * @param itemName The name of the item to be removed.
     */
    //Tolowercase to make sure we are always searching for the lowercase key
    public Item remove(String itemName) {
        return items.remove(itemName.toLowerCase());
    }

    /**
     * Put the given item in the list.
     * @param name The name of the item.
     * @param item The item.
     */

    //toLowerCase to make sure all keys are entered in lowercase
    public void put(String name, Item item)
    {
        items.put(name.toLowerCase(), item);
    }

    /**
     * Return the item with the given name
     * @param name The name of the item to return
     * @return The named item, or null if it is not in the list.
     */

    //Use toLowerCase() method to make sure the keys are searched without a capital letter
    public Item get(String name) {
        if(name != null) {
            return items.get(name.toLowerCase());
        }
            return null;
    }

    /**
     * Return a string listing the descriptions and weight of the
     * items in the list.
     * @return A String with the descriptions of the Items.
     */
    public String getLongDescription() {
        String returnString = "";
        for(Item item: items.values()) {
            returnString += "\n" + item.getName() + " (" + item.getDescription() + ") with weight of " + item.getWeight() + " kg";
        }
        return returnString;
    }

    /**
     * Return the total weight of all items in the list.
     * @return The total weight
     */
    public double getTotalWeight() {
        double weight = 0;
        for(Item item: items.values()) {
            weight += item.getWeight();
        }
        return weight;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
