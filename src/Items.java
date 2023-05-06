import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A list of items.
 *
 * @author  Poul Henriksen
 * @version 2016.02.29
 */
public class Items
{
    // A map of item names to items.
    private HashMap<String, Item> items;

    /**
     * Create a new item list.
     */
    public Items()
    {
        items = new HashMap<>();
    }

    /**
     * Return an Iterator over the items.
     * @return An Iterator.
     */
    public Iterator<Item> iterator()
    {
        return items.values().iterator();
    }

    /**
     * Remove the given item.
     * @param itemName The name of the item to be removed.
     */
    //Tolowercase to make sure we are always searching for the lowercase key
    public Item remove(String itemName)
    {
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
    //Ergens wordt items.get() aangeroepen met een null parameter
    public Item get(String name)
    {
        if(name != null) {
            return items.get(name.toLowerCase());
        }
            return null;
    }

    /**
     * Return a string listing the descriptions of the
     * items in the list.
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
    public double getTotalWeight()
    {
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
