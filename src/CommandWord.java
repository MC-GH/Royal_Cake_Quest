public enum CommandWord {
    GO("go"),
    LOOK("look"),
    INVENTORY("inventory"),
    BACK("back"),
    TAKE("take"),
    DROP("drop"),
    EAT("eat"),
    TALK("talk"),
    LOAD("load"),
    FIRE("fire"),
    COOK("cook"),
    QUIT("quit"),
    HELP("help"),
    UNKNOWN("?");

    private String commandString;

    //Constructor
    CommandWord(String commandString) {
        this.commandString = commandString;
    }

    public String toString() {
        return commandString;
    }
}
