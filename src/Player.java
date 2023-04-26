import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class Player {
    private String name;
    private Room currentRoom;
    private Stack<Room> trail;
    private Items items = new Items();
    private double maxWeight;
    private int moves = 0;
    private int maxMoves = 5;

    public Player(String name) {
        this.name = name;
        trail = new Stack<>();
        this.maxWeight = 15;
    }

    public void setStartRoom(Room room) {
        this.currentRoom = room;
    }

    public void enterRoom(Room room) {
        moves++;
        trail.push(getCurrentRoom());
        currentRoom = room;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void goBack(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("Back where?");
            return;
        }
        if(trail.empty()) {
            System.out.println("You have nowhere to go back to!");
        } else {
            //Removes last added object of Stack, and returns it.
            currentRoom = trail.pop();
        }
    }

    public String getName() {
        return name;
    }



    /**
     * Checks if we can pick up the given item. This depends on whether the item
     * actually is in the current room and if it is not too heavy.
     * @parem itemName The item to be picked up.
     * @return true if the item can be picked up, false otherwise.
     */
    private boolean canPickItem(String itemName)
    {
        boolean canPick = true;
        Item item = currentRoom.getItem(itemName);
        if(item == null) {
            canPick = false;
        }
        else {
            double totalWeight = items.getTotalWeight() + item.getWeight();
            if(totalWeight > maxWeight) {
                canPick = false;
                System.out.println("Not enough room in inventory!");
            }
        }
        return canPick;
    }

    /**
     * Tries to pick up the item from the current room.
     * @param itemName The item to be picked up.
     * @return If successful this method will return the item that was picked up.
     */
    public Item pickUpItem(String itemName)
    {
        if(canPickItem(itemName)) {
            //Return the original item to be removed from the room
            Item item = currentRoom.removeItem(itemName);
            if(item.isCovered()) {
                Item coveredItem = item.getCoveredItem();
                //Store the original Item as the covered item of the revealed item for later purposes
                coveredItem.setCoveredItem(item);
                items.put(coveredItem.getName(), coveredItem);
                System.out.println("The " + item.getName() + " actually reveals another item: " + coveredItem.getDescription());
                return coveredItem;
            } else {
                items.put(itemName, item);
                return item;
            }
        }
        //We can't pick it up -- not necessary?
        else {
            return null;
        }
    }
    //Voorbeeldmethode om in eigen spel te gebruiken
    public void cook() {
        Iterator<Item> it = items.iterator();
        //Create a HashSet (cannot contain doubles) - loop through the keyset of the Items
        //if(key.equals("egg")) add to HashSet.
        //Check length of HashSet = if 4 then we have all items. (as there can be no doubles)
        HashSet<Item> ingredients = new HashSet<>();
        while(it.hasNext()) {
            Item item = it.next();
            if(item.getName().equals("butter") || item.getName().equals("sugar")|| item.getName().equals("flour") || item.getName().equals("magic egg") ) {
                //Add the item to our cooking ingredients Hashset, but do not remove from items collection
                ingredients.add(items.get(item.getName()));
            }
        }
        if(ingredients.size() == 4) {
            Item reward = new Item("cake", "a delicious royal cake", 2.0);
            items.put(reward.getName(), reward);
        } else {
            System.out.println("You don't seem to have the necessary ingredients to cook the dessert.");
        }
    }

    /**
     * Tries to drop an item into the current room.
     * @param itemName The item to be dropped.
     *
     * @return If successful this method will return the item that was dropped.
     */
    public Item dropItem(String itemName)
    {
        Item item = items.remove(itemName);
        //Item exists in inventory
        if(item != null) {
            if(item.isCovered()) {
                //refer to the original covering item
                Item coveringItem = item.getCoveredItem();
                //set the item in the inventory back as the covered item
                coveringItem.setCoveredItem(item);
                currentRoom.addItem(coveringItem);
                System.out.println("You hide the " + item.getName() + " back in a " + coveringItem.getName());
                return coveringItem;
            }
            currentRoom.addItem(item);
        }
        return item;
    }

    /**
     * Eats the item if possible.
     * Only cookies can be eaten.
     * @param itemName The item to be eaten.
     */
    public Item eat(String itemName) {
        if(itemName.equals("cookie")) {
            //First see if we have a cookie in our inventory
            Item cookie = items.get(itemName);
            //Then check if there is a cookie in the room
            if(cookie == null) {
                cookie = currentRoom.removeItem(itemName);
            }
            if(cookie != null) {
                maxWeight += 1;
                return cookie;
            }
        }
        return null;
    }

    public String getInventoryDescription() {
        String returnString = "Player " + getName() + " has at the moment following item(s) in his inventory: ";
        if(items.isEmpty()) {
            return returnString + "\nInventory is empty";
        }
        return  returnString + items.getLongDescription() + "\nTotal weight: " + items.getTotalWeight();
    }
    //Return the item of the inventory, without removing it
    public Item getItem(String item) {
        if(items.get(item) != null) {
            return items.get(item);
        }
        return null;
    }
//
//    /**
//     * Returns a string describing the players current location and which
//     * items the player carries.
//     * @return A description of the room and items held.
//     */
//    public String getLongDescription()
//    {
//        String returnString = currentRoom.getLongDescription();
//        returnString += "\n" + getInventoryDescription();
//        return returnString;
//    }

    public boolean isDead()
    {
        return moves > maxMoves;
    }



}
