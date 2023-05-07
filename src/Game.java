import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *  This class is the main class of the "Royal Cake Quest" game.
 *  "Royal Cake Quest" is a simple game where the player can explore several rooms,
 *  pick up items and talk to characters. The goal is to find several ingredients
 *  spread throughout the Castle, and prepare a Cake by using the COOK command.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Michaël Cornelis
 * @version 2023.05.06
 */

public class Game {
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
     * Create all the Rooms, items, characters and assign them to eachother.
     * @return A Room object where the game will start.
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
        Room diningHall = new Room("in the dining Hall. The food will be served here");
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

        //Creating and assigning Item objects
        //2 items will have a fixed (not random) location
        guardHall.addItem(new Item("Key", "A key to the prisoner's cell", 0.2));
        kitchen.addItem(new Item("Recipe", "A recipe which reads: to prepare the Royal Cake, mix flour, butter, sugar and a magic egg",0.1));

        //Create an ArrayList with items that will be spread out randomly over the rooms
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Book", "an old history book", 0.5));
        items.add(new Item("Candle","an unused candle", 0.1));
        items.add(new Item("Cookie", "a magic cookie, looks tasty", 0.1));
        items.add(new Item("Sugar","a small bag of sugar", 1.0));
        items.add(new Item("Flour", "a small bag of flour", 2.0));
        items.add(new Item("Butter", "a stick of butter", 2.0));
        items.add(new Item("Sword", "a very sharp steel sword", 5.0));
        items.add(new Item("Chandelier", "a large decorative light fixture", 4.0));
        items.add(new Item("Goblet", "a fancy drinking glass", 0.8));
        items.add(new Item("Boots", "a pair of sturdy leather boots", 2.5));
        items.add(new Item("Torch", "a long wooden stick used for lighting", 2.2));
        items.add(new Item("Gauntlets", "a pair of protective gloves worn in battle", 2.4));
        items.add(new Item("Portrait", "an oil painting of the castle Lord", 4.0));
        items.add(new Item("Lamp", "a lamp that uses oil as fuel", 1.5));
        items.add(new Item("Mace", "a heavy weapon with a spiked head used for combat", 8.0));
        items.add(new Item("Mushrooms", "some strange mushrooms", 0.8));
        items.add(new Item("Scepter", "a ceremonial staff from the castle Lord", 3.5));
        items.add(new Item("Pot", "an old dirty clay pot", 2.2));
        items.add(new Item("Milk", "a jug of fresh cow milk", 1.5));
        items.add(new Item("Chocolate", "a bar of chocolate, gives extra energy", 0.2));

        //Set up an item that covers another item
        Item coveringItem = new Item("Box", "a box, there seems to be something in it", 0.2);
        Item coveredItem = new Item("Boot", "an old dirty boot", 0.5);
        coveringItem.setCoveredItem(coveredItem);
        items.add(coveringItem);

        //Store all the rooms in an ArrayList
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.addAll(Arrays.asList(cellarStorage, chapel, prison, guardHall, castleSquare, mainHall, weaponsRoom,
                knightsHall, diningHall, kitchen, kitchenStorage, servantsHall, upstairsHall, royalNightHall));

        //Create a new Random object and add all the items to a random room
        Random r = new Random();
        for(int i = 0; i < items.size(); i++) {
            Room randomRoom = rooms.get(r.nextInt(rooms.size()));
            randomRoom.addItem(items.get(i));
        }

       //Creating a character and setting up the character dialogue.
        Character prisoner = new Character("Lancelot", new Item("Egg", "A magic egg used to create very special desserts", 0.1), "key");

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
        //Return a Room object where the game will start
        return castleSquare;
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    private void play() {
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
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("====================================================================================");
        System.out.println("========================= WELCOME TO THE ROYAL CAKE QUEST! =========================");
        System.out.println("====================================================================================");
        System.out.println("The King is throwing a feast tonight for important guests. As the assistant of the");
        System.out.println("cook, you are in charge of the Royal Cake. Unfortunately, the ingredients are spread");
        System.out.println("throughout the castle. You need to find the ingredients and assemble (COOK) the cake.");
        System.out.println("====================================================================================");
        System.out.println("Type " + CommandWord.HELP + " if you need some help.");
        System.out.println("TIP: start by searching for a recipe so you know what to look for.");
        System.out.println("TIP 2: you are carrying a magic lamp which can store your current location.");
        System.out.println("Use LOAD to store your current location. Use FIRE to teleport to your saved location.");
        System.out.println("====================================================================================");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Print some info about the current room, such as the items in the room, the room exits,
     * the characters, and the remaining moves left of the player.
     */
    private void printLocationInfo() {
        System.out.println(player.getCurrentRoom().getLongDescription());
        System.out.println(player.getMoves());
        System.out.println();
    }

    private void printVictory() {
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
    private boolean processCommand(Command command) {
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
                goRoom(command);
                break;
            case LOOK:
                look();
                break;
            case INVENTORY:
                inventory();
                break;
            case BACK:
                goBack();
                break;
            case TAKE:
                take(command);
                break;
            case DROP:
                drop(command);
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
                eat(command);
                break;
            case COOK:
                cook();
                break;
            case QUIT:
                wantToQuit = quit(command);
        }
        return wantToQuit;
    }

    /**
     * Print out some help information such as current room and possible command words.
     */
    private void printHelp() {
        System.out.println("You are currently " + player.getCurrentRoom().getShortDescription() + ".");
        System.out.println("Your goal is to find the necessary ingredients to prepare the cake.");
        System.out.println("Try searching for a recipe to find out which ingredients you need.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.getCommandList();
    }

    /**
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) {
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
     * Enter room and print info about the room.
     */
    private void enterRoom(Room nextRoom) {
        player.enterRoom(nextRoom);
        printLocationInfo();
    }

    /**
     * Go back to the previous room. Also counts as a "move" for the player.
     */
    private void goBack() {
        player.goBack();
        printLocationInfo();
    }

    /**
     * Try to take an item from the current room. If the item is there,
     * pick it up, if not print an error message.
     */
    private void take(Command command) {
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
    private void drop(Command command) {
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
            System.out.println(player.getInventoryDescription());
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Look around in the current room and print some info.
     */
    private void look() {
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /**
     * Check if the player carries an item that the Character requests.
     * If it is the case, player receives a reward item from the character.
     * Print out dialogue of character depending on situation.
     */
    private void talk() {
        String itemToFind = player.getCurrentRoom().getItemRequested();
        if(player.getItem(itemToFind) != null && player.getCurrentRoom().getCharacter().hasBeenVisited()) {
            //Below method call returns the String of the Character Item, and also stores the Item into the
            //items collection of the room.
            String rewardItem = player.getCurrentRoom().getCharacterItem();
            player.pickUpItem(rewardItem);
        } else {
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
    private void eat(Command command) {
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
        }
    }

    private void cook() {
        player.cook();
    }

    /**
     * Stores the current location in the lamp.
     */
    private void charge() {
        player.chargeLamp();
        System.out.println("Lamp has been charged.");
    }

    /**
     * Travel to the location stored in the lamp, if there is one.
     */
    private void fire() {
        if(player.fireLamp()) {
            System.out.println("Lamp fired.");
            System.out.println(player.getCurrentRoom().getLongDescription());
        }
        else {
            System.out.println("Lamp not charged.");
        }
    }

    /**
     * Main method which creates a new Game object and triggers the play method.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}


