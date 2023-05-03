import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  For each existing exit, the room
 * stores a reference to the neighboring room.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 2016.02.29
 */

public class Room {
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private Items items;        // stores items available in this room.
    //placeholder for the character in this room
    private Character character;

    /**
     * Create a room described "description". Initially, it has no exits.
     * "description" is something like "in a kitchen" or "in an open court
     * yard".
     */
    public Room(String description) {
        this.description = description;
        exits = new HashMap<>();
        items = new Items();
    }

    /**
     * Define an exit from this room.
     */
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    //Assign all the neighbouring rooms at once, but only add the ones with a value to the HashMap.
    public void setExits(Room nRoom, Room eRoom, Room sRoom, Room wRoom, Room uRoom, Room dRoom) {
        String[] directions = {"north", "east", "south", "west", "up", "down"};
        Room[] rooms = {nRoom, eRoom, sRoom, wRoom, uRoom, dRoom};

        for (int i = 0; i < directions.length; i++) {
            if (rooms[i] != null) {
                exits.put(directions[i], rooms[i]);
            }
        }
    }

    /**
     * Return the description of the room (the one that was defined in the
     * constructor).
     */
    public String getShortDescription() {
        return description;
    }

    /**
     * Return a long description of this room, in the form:
     *     You are in the kitchen.
     *     Exits: north west
     */
    public String getLongDescription() {
        String returnString =  "You are " + description + ".\n" + getExitString() + "\nItems in this room: ";
        if(this.items.isEmpty()) {
            returnString += "\nNo items in this room";
        }

        returnString += items.getLongDescription();

        if(character != null) {
            returnString += "\n" + "There seems to be someone in this room. " +
                    "\nUse the talk command to talk to the person.";
        }

        return returnString;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     */
    private String getExitString() {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(Iterator<String> iter = keys.iterator(); iter.hasNext(); )
            returnString += " " + iter.next();
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     */
    public Room getExit(String direction) {
        return exits.get(direction);
    }

    /**
     * Puts an item into this room.
     * @param item The item put into the room.
     */
    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    /**
     * Returns the item if it is available, otherwise it returns null.
     * @param name The name of the item to be returned.
     * @return The named item, or null if it is not in the room.
     */
    public Item getItem(String name) {
        return items.get(name);
    }

    /**
     * Removes and returns the item if it is available, otherwise it returns null.
     * @param name The item to be removed.
     * @return The item if removed, null otherwise.
     */
    //Hashmap keys are stored in lowercase. If we need to remove an item from the items
    //collection triggered by talking to the character, no input passes via the parser.
    public Item removeItem(String name) {
        return items.remove(name);
    }

    //Character methods allowing the Player to interact with the Character
    public void setCharacter(Character c) {
        this.character = c;
    }

    public Character getCharacter() {
        return this.character;
    }

    //Character plaatst het Item in de collectie Items van de kamer (String key, Item value)
    //Return de naam (String) van het item
    //Character puts his item in the Items collection of the room
    public String getCharacterItem() {
        //The character.getItem() method can only be called once. Once called, the Item the character is holding is set to null
        Item characterItem = character.getItem();
        items.put(characterItem.getName(), characterItem);
        //Clear the character from the room (prisoner escapes)
        character = null;
        return characterItem.getName();
    }

    /**
     *
     * @return a String of the name of the item the Character is requesting from the player.
     */
    public String getItemRequested() {
        if(character != null) {
            return character.getItemRequested();
        }
        return null;
    }

    public String talk() {
        if(character != null) {
            return character.speak();
        }

        return "You mumble a bit to yourself. There is no one else here.";

    }
}


