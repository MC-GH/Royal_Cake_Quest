import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Player {
    private String name;
    private Room currentRoom;
    private Stack<Room> trail;
    private Items items = new Items();
    private double maxWeight;
    private int moves = 0;
    private int maxMoves = 30;

    private Room lampTarget;

    public Player(String name) {
        this.name = name;
        trail = new Stack<>();
        this.maxWeight = 6;
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


    public void goBack() {
        if(trail.empty()) {
            System.out.println("You have nowhere to go back to!");
        } else {
            //Removes last added object of Stack, and assign it as the currentRoom.
            currentRoom = trail.pop();
            moves++;
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
        //We can't pick it up
        else {
            return null;
        }
    }

    /**
     * Store all the needed Ingredient names in an ArrayList
     *
     */
    public void cook() {
        ArrayList<String> neededIngredients = new ArrayList<>(Arrays.asList("Butter", "Sugar", "Flour", "Egg"));
        ArrayList<Item> list = items.getArrayList();
        long itemsFound= list.stream()
                .filter(item-> neededIngredients.contains(item.getName()))
                .map(item -> item.getName())
                .distinct()
                .count();

        if(itemsFound == 4) {
            Item reward = new Item("cake", "a delicious royal cake", 2.0);
            items.put(reward.getName(), reward);
        } else {
            System.out.println("You don't seem to have the necessary ingredients to cook the dessert.");
            System.out.println("Try searching for a recipe to find out which ingredients you need.");
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
                System.out.println("You hide the " + item.getName() + " back in a random " + coveringItem.getName());
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

        if(itemName.equals("cookie") || itemName.equals("mushrooms") ||itemName.equals("chocolate")) {
            //Check if we have one of the items in our inventory. Remove from player inventory if we do.
            Item item = items.remove(itemName);
//          //Then check if one of the items is in the room. Remove from room if there is.
            if(item == null) {
                item = currentRoom.removeItem(itemName);
            }
            if(item != null) {
                switch(item.getName()) {
                    case "Cookie":
                        maxWeight += 3;
                        System.out.println("You feel stronger.");
                        return item;
                    case "Mushrooms":
                        maxMoves -= 5;
                        maxWeight -= 1;
                        return item;
                    case "Chocolate":
                        maxMoves += 10;
                        return item;
                }
            }
        }
        return null;
    }

    public String getInventoryDescription() {
        String returnString = "Player " + getName() + " has at the moment following item(s) in his inventory: ";
        if(items.isEmpty()) {
            return returnString + "\nInventory is empty";
        }
        //Set up a formatting for the totalweight to display only 1 digit after comma.
        //Otherwise due to binary rounding errors we can get numbers with several digits
        String totalWeight = String.format("%.1f", items.getTotalWeight());
        return returnString + items.getLongDescription() + "\nTotal weight: " + totalWeight + " / " + maxWeight + " kg";
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
    public String getLongDescription()
    {
        String returnString = currentRoom.getLongDescription();
        returnString += "\n" + getInventoryDescription() + "\n" +
        "Moves played: " + moves + "/" + maxMoves;
        return returnString;
    }

    public String getMoves() {
        return "Moves remaining: " + moves + "/" + maxMoves;
    }

    public boolean isDead()
    {
        return moves > maxMoves;
    }

    /**
     * Charger the beamer to the current room
     */
    public void chargeLamp()
    {
        lampTarget = currentRoom;
    }

    /**
     * Fires the beamer
     */
    //Implement a method: once we FIRE the Lamp, we clear the Stack!
    public boolean fireLamp()
    {
        if(lampTarget != null) {
            enterRoom(lampTarget);
            //Once the fire method has been used => we clear the Stack.
            trail.clear();
            return true;
        }
        return false;
    }

}
