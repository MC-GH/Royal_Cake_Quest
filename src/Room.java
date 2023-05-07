import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Class Room - a room in the game.
 * A "Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  For each existing exit, the room
 * stores a reference to the neighboring room.
 * A room can also store several items and hold a character.
 */

public class Room {
    private String description;
    private HashMap<String, Room> exits;
    private Items items;
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
     * Return a long description of this room with info such as current room,
     * exits, items in the room and character (if there is one).
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
        for(String key : exits.keySet()) {
            returnString += " " + key;
        }
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
     * Puts an item into the Items list of the room.
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
    public Item removeItem(String name) {
        return items.remove(name);
    }

    /**
     * Places a Character object in the room.
     * @param c The Character to be placed in the room.
     */
    public void setCharacter(Character c) {
        this.character = c;
    }

    public Character getCharacter() {
        return this.character;
    }

    /**
     * Places the Item the Character is holding into the Items collection of the room
     * and clears the character from the room. Can only be called once.
     * @return the name of the Item retrieved from the Character.
     */
    public String getCharacterItem() {
        if(character != null) {
            Item characterItem = character.getItem();
            items.put(characterItem.getName(), characterItem);
            character = null;
            return characterItem.getName();
        }
        return null;
    }

    /**
     * @return a String of the name of the item the Character is requesting from the player.
     */
    public String getItemRequested() {
        if (character != null) {
            return character.getItemRequested();
        }
        return null;
    }

    /**
     * Triggers the dialogue with the character (if there is one)
     * @return a String of dialogue from the Character, or another String if there is no Character.
     */
    public String talk() {
        if(character != null) {
            return character.speak();
        }
        return "You mumble a bit to yourself. There is no one else here.";
    }
}


