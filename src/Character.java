public class Character {
    private String name;
    private boolean alreadyVisited;
    private Item item;
    private String introductionString;
    private String followUpString;
    private String finalString;
    private String itemRequested;

    public Character(String name, Item item) {
        this.name = name;
        this.item = item;
        this.alreadyVisited = false;
    }
    //Constructor to use for when the character requests a specific item from the player.
    public Character(String name, Item item, String requestedItem) {
        this.name = name;
        this.item = item;
        this.alreadyVisited = false;
        this.itemRequested = requestedItem;
    }

    public String speak() {
        if(!hasBeenVisited()) {
            alreadyVisited = true;
            return introductionString;
        }
        return followUpString;
    }
    //Set the dialogue for when the player first interacts with the character
    public void setIntroductionString(String introduction) {
        this.introductionString = introduction;
    }
    //Set the dialogue for when the player has already spoken to the character
    public void setFollowUpString(String followUp) {
        this.followUpString = followUp;
    }

    //Set the final dialogue that is used by the character (if needed)
    public void setFinalString(String finalString) {
        this.finalString = finalString;
    }

    public String getName() {
        return this.name;
    }

    //Return the Item the character is holding, while also clearing the variable
    //This method can only be called once, because once it is called, the item is set to null
    public Item getItem() {
        Item returnItem = this.item;
        this.item = null;
        //Print the final message to display when the Character hands over the item to the Player
        System.out.println(finalString);
        return returnItem;
    }

    public String getItemRequested() {
        return itemRequested;
    }

    public boolean hasBeenVisited() {
        return alreadyVisited;
    }
}
