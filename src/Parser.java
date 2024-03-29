import java.util.Scanner;

/**
 * This parser reads user input and tries to interpret it as a command.
 * Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 */

public class Parser {
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * @return The next command from the user.
     */
    public Command getCommand() {
        String inputLine;   // will hold the full input line
        String word1 = null;
        String word2 = null;
        System.out.print("> ");     // print prompt
        inputLine = reader.nextLine();
        // Find up to two words on the line. Store in lowercase, so if the player types in capital letters, it still gets recognized.
        //Create new Scanner object to check the inputLine for seperate words
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            word1 = tokenizer.next().toLowerCase();
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next().toLowerCase();
                // note: we just ignore the rest of the input line.
            }
        }

        if(commands.isCommand(word1)) {
            return new Command(commands.getCommandWord(word1), word2);
        }
        else {
            return new Command(CommandWord.UNKNOWN, word2);
        }
    }

    public void getCommandList() {
        commands.showAll();
    }
}

