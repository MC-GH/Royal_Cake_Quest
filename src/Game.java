import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *  This class is the main class of the "World of Zuul" application.
 *  "World of Zuul" is a very simple, text based adventure game.  Users
 *  can walk around some scenery. That's all. It should really be extended
 *  to make it more interesting!
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Game
{
    private Parser parser;
    private Player player;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        parser = new Parser();
        player = new Player("Michaël");
        Room startRoom = createRooms();
        //Initialize startroom for Player, so when going back through the Stack it does not end at null
        player.setStartRoom(startRoom);
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private Room createRooms()
    {
        //Cellar rooms
        Room cellarStorage = new Room("in the old dusty storage room");
        Room chapel = new Room("in the chapel");
        Room prison = new Room("in the prison room");
        Room guardHall = new Room("in the chamber of the guards. This must be where they sleep");
        //First floor rooms
        Room castleSquare = new Room("outside in the main square of the castle");
        Room mainHall = new Room("in the Main Hall of the castle. This is where the Lord welcomes his guests");
        Room weaponsRoom = new Room("in the armory. Armour and weapons are displayed here");
        Room knightsHall = new Room("in the Knight's Hall. This is where the knights sleep");
        Room diningHall = new Room("in the dining Hall. The food will be served here.");
        Room kitchen = new Room("in the kitchen. It smells nice");
        Room kitchenStorage = new Room("in the kitchen storage. There is so many food in here");
        //Upstairs rooms
        Room servantsHall = new Room("in the servant's Hall. The maids sleep here");
        Room upstairsHall = new Room("in the upstairs Hall. Watch out for the stairs");
        Room royalNightHall = new Room("in the sleeping Hall of the Castle Lord. You better not make a mess here");

        //Cellar rooms
        cellarStorage.setExits(null, chapel, guardHall, null, kitchenStorage,null);
        chapel.setExits(null, null, prison, cellarStorage, null, null);
        prison.setExits(chapel, null, null, guardHall, castleSquare, null);
        guardHall.setExits(cellarStorage, prison, null, null, null, null);
        //First floor rooms
        castleSquare.setExits(mainHall, null, null, null, null, prison);
        mainHall.setExits(diningHall, weaponsRoom, castleSquare, kitchenStorage, upstairsHall, null);
        weaponsRoom.setExits(knightsHall, null, null, mainHall, null, null);
        knightsHall.setExits(null, null, weaponsRoom, diningHall, null, null);
        diningHall.setExits(null, knightsHall, mainHall, kitchen, null, null);
        kitchen.setExits(null, diningHall, kitchenStorage, null, null, null);
        kitchenStorage.setExits(kitchen, mainHall, null, null, null, cellarStorage);
        //Upstairs rooms
        servantsHall.setExits(null, upstairsHall, null, null, null, null);
        upstairsHall.setExits(null, royalNightHall, null, servantsHall, null, mainHall);
        royalNightHall.setExits(null, null, null, upstairsHall, null, null);

        //Random toewijzing van items aan kamers:
        //ArrayList maken items, alle Items erin

        //Recipe item should be in kitchen
        //Key to the prison cell should be in the guardHall.
        ArrayList<Item> items = new ArrayList<>();
        items.add( new Item("Book", "A book of recipes", 0.5));
        items.add( new Item("candle","An unused candle", 0.1));
        items.add( new Item("cookie", "A magic cookie", 0.1));
        ArrayList<Room> rooms = new ArrayList<>();
        //Create new ArrayList from rooms
        rooms.addAll(Arrays.asList(castleSquare));
        Random r = new Random();

        //For each item, assign it to a Random Room
        for(int i = 0; i < items.size(); i++) {
            Room randomRoom = rooms.get(r.nextInt(rooms.size()));
            randomRoom.addItem(items.get(i));
            System.out.println("Item " + items.get(i).getName() + " has been added  " + randomRoom.getShortDescription());
        }

       //Creating a character and setting up the character dialogue.
        Character prisoner = new Character("Lancelot", new Item("egg", "A magic egg used to create very special desserts", 0.1), "key");

        prisoner.setIntroductionString("Hello, my name is " + prisoner.getName() +
                "\nI have been thrown in this cell for stealing some food from the kitchen." +
                "\nIf you can bring me the key to my cell, I will reward you with a magic egg." +
                "\nTalk to me again when you have found it.");

        prisoner.setFollowUpString("Have you found the key to my cell yet? Come back to me when you have found it.");
        prisoner.setFinalString("You unlock the cell door of the prisoner. Before he leaves he turns to you and says:" +
                "\n\"Thanks! Have this magic egg as a reward!\"" +
                "\nAs you place the magic egg in your inventory, you see the prisoner quietly sneaking out of the room.");
        //Assigning the character to a room
        prison.setCharacter(prisoner);


        guardHall.addItem(new Item("key", "A key to the prisoner's cell", 0.2));

        //Test covered item functionality
        Item coveringItem = new Item("rubble", "some rubble, there seems to be something underneath", 2.0);
        Item coveredItem = new Item("letter", "an old dusty letter", 0.5);
        coveringItem.setCoveredItem(coveredItem);
        castleSquare.addItem(coveringItem);
        castleSquare.addItem(new Item("sugar","a small bag of sugar", 1.0));
        castleSquare.addItem(new Item("flour", "a small bag of flour", 2.0));
        castleSquare.addItem(new Item("butter", "a stick of butter", 2.0));
        //In de keuken een recept plaatsen met de instructies op


        return castleSquare;
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            if(player.isDead()) {
                printDead();
                finished = true;
            }
            if(player.getItem("cake") != null) {
                printVictory();
                finished = true;
            }

        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("==========================================================================");
        System.out.println("==================== WELCOME TO THE ROYAL CAKE QUEST! ====================");
        System.out.println("==========================================================================");
        System.out.println("======= The King is throwing a feast tonight for important guests. =======");
        System.out.println("=== As the assistant of the cook, you are in charge of the Royal Cake. ===");
        System.out.println("==== Unfortunately, the ingredients are spread throughout the castle. ====");
        System.out.println("===== You need to find the ingredients and assemble (COOK) the cake. =====");
        System.out.println("================ Hurry up, the guests are getting hungry! ================");
        System.out.println("==================== Type " + CommandWord.HELP.toString() + " if you need some help. ====================");
        System.out.println("==========================================================================");
        System.out.println("=== TIP: start by searching for a recipe so you know what to look for. ===");
        System.out.println("TIP2: you are carrying a magic lamp which can store your current location.");
        System.out.println("============ Use command LOAD to store your current location. ============");
        System.out.println("==== Use command FIRE to teleport to your saved location. Use wisely! ====");
        System.out.println("==========================================================================");


        System.out.println();
        printLocationInfo();
    }

    private void printLocationInfo() {
        System.out.println(player.getCurrentRoom().getLongDescription());
        System.out.println();
    }

    public void printVictory() {
        System.out.println("You managed to bake the cake and save the party. Congratulations! You have won the game.");
    }

    private void printDead() {
        System.out.println("Oh no, you did not manage to bake the dessert on time. The party has to be cancelled!");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        CommandWord commandWord = command.getCommandWord();
        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean..");
                break;
            case HELP:
                printHelp();
                break;
            case GO:
                goRoom(command);//Methode van Player object
                break;
            case LOOK:
                look(); //Methode van Player object
                break;
            case INVENTORY:
                inventory();//Methode van Player object
                break;
            case BACK:
                goBack(command);//Methode van Player object
                break;
            case TAKE:
                take(command);//Methode van Player object
                break;
            case DROP:
                drop(command);//Methode van Player object
                break;
            case TALK:
                talk();
                break;
            case LOAD:
                charge();
                break;
            case FIRE:
                fire();
                break;
            case EAT:
                eat(command);//Methode van Player object
                break;
            case COOK:
                cook();
                break;
            case QUIT:
                wantToQuit = quit(command);
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp()
    {
        System.out.println("Player " + player.getName() + " is lost and alone, and wanders");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.getCommandList();
    }

    /**
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {

            enterRoom(nextRoom);
        }
    }

    /**
     * Voert de opgegeven ruimte in en drukt de beschrijving af
     * @param nextRoom
     */
    private void enterRoom(Room nextRoom) {
        player.enterRoom(nextRoom);
        printLocationInfo();
    }

    /**
     * Ga terug naar de vorige ruimte.
     */

    private void goBack(Command command) {
        player.goBack(command);
        printLocationInfo();
    }

    /**
     * Try to take an item from the current room. If the item is there,
     * pick it up, if not print an error message.
     */
    private void take(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            System.out.println("What do you want to take?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.pickUpItem(itemName);

        if(item == null) {
            System.out.println("Can't pick up the item: " + itemName);
        } else {
            System.out.println("Picked up " + item.getDescription());
            System.out.println(player.getInventoryDescription());
        }
    }

    /**
     * Drops an item into the current room. If the player carries the item
     * drop it, if not print an error message.
     */
    private void drop(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            System.out.println("What do you want to drop?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.dropItem(itemName);

        if(item == null) {
            System.out.println("You don't carry the item: " + itemName);
        } else {
            //The item exists in the inventory
            System.out.print("Dropped ");
            //if it concerns a covered item, we print the description of the revealed item
            if(item.isCovered()) {
                System.out.println(item.getCoveredItem().getDescription());
            } else {
                System.out.println(item.getDescription());
            }
//            System.out.println("Dropped " + item.getDescription());
            System.out.println(player.getInventoryDescription());
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    private void look() {
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    private void talk() {
        String itemToFind = player.getCurrentRoom().getItemRequested();
        if(player.getItem(itemToFind) != null && player.getCurrentRoom().getCharacter().hasBeenVisited()) {
            //By calling this method: the CharacterItem is put in the Items collection of the room.
            //Store the key (String) of the item returned by the Character
            String rewardItem = player.getCurrentRoom().getCharacterItem();
            //Use the pickup method to place the item in the inventory of the player
            player.pickUpItem(rewardItem);
        } else {
            //Het gewenste item is niet in het bezit, dus standaard dialoog wordt uitegevoerd
            System.out.println(player.getCurrentRoom().talk());
        }
    }

    private void inventory() {
        System.out.println(player.getInventoryDescription());
    }

    /**
     * Try to take an item from the current room. If the item is there,
     * pick it up, if not print an error message.
     */
    private void eat(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to eat...
            System.out.println("What do you want to eat?");
            return;
        }
        String itemName = command.getSecondWord();
        Item item = player.eat(itemName);
        if(item == null) {
            System.out.println("Could not eat " + itemName);
        }
        else {
            System.out.println("Ate " + item.getDescription());
            //Currently this does not remove the item from the Items HashMap
        }
    }

    public void cook() {
        player.cook();
        //If dessert is successfull, add it to the Items of the player, ending the game.
    }

    private void charge()
    {
        player.chargeLamp();
        System.out.println("Beamer charged.");
    }

    private void fire()
    {
        if(player.fireLamp()) {
            System.out.println("Beamer fired.");
            System.out.println(player.getLongDescription());
        }
        else {
            System.out.println("Beamer not charged.");
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}


