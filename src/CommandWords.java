import java.util.HashMap;

/**
 * This class holds a HashMap of the valid CommandWords that can be used.
 * Keys (Strings) and values (CommandWord objects) are stored.
 */

public class CommandWords {
    private HashMap<String, CommandWord> validCommands;

    /**
     * Constructor - fill the HashMap with CommandWords. (key and value)
     */
    public CommandWords() {
        validCommands = new HashMap<>();
        for(CommandWord command : CommandWord.values()) {
            if(command != CommandWord.UNKNOWN) {
                validCommands.put(command.toString(), command);
            }
        }
    }

    /**
     * Check whether a given String is a valid command word.
     * @return true if a given string is stored as valid key in the HashMap.
     * false if it isn't.
     */
    public boolean isCommand(String aString) {
        return validCommands.containsKey(aString);
    }

    public void showAll() {
        validCommands.keySet().forEach(command -> System.out.print(command + " "));
        System.out.println();
    }

    /**
     * Find the CommandWord associated with a command word.
     * @param commandWord The word to look up (as a string).
     * @return The CommandWord correspondng to commandWord, or UNKNOWN
     * if it is not a valid command word.
     */
    public CommandWord getCommandWord(String commandWord) {
        CommandWord command = validCommands.get(commandWord);
        if(command != null) {
            return command;
        }
        else {
            return CommandWord.UNKNOWN;
        }
    }
}

