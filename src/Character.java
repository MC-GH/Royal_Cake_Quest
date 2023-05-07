/**
 * Character Class
 * Holds attributes of the Character, such as name, if it has already been visited,
 * the Item it might request or the Item it holds, as well as some dialogue phrases.
 */
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

    /**
     * Second constructor for if the Character requests a specific Item.
      * @param name Name of the Character.
     * @param item The Item it holds on itself.
     * @param requestedItem the Item it might request from the player.
     */
    public Character(String name, Item item, String requestedItem) {
        this.name = name;
        this.item = item;
        this.alreadyVisited = false;
        this.itemRequested = requestedItem;
    }

    /**
     * Triggers the dialogue with the Character, depending on whether the Character
     * has already been visited or not.
     * @return the String of dialogue from the Character.
     */
    public String speak() {
        if(!hasBeenVisited()) {
            alreadyVisited = true;
            return introductionString;
        }
        return followUpString;
    }

    /**
     * Sets introduction dialogue.
      * @param introduction The string to display as introduction message.
     */
    public void setIntroductionString(String introduction) {
        this.introductionString = introduction;
    }

    /**
     * Sets a follow-up dialogue if the Character has already been visited.
      * @param followUp The string to display as follow-up dialogue.
     */
    public void setFollowUpString(String followUp) {
        this.followUpString = followUp;
    }

    /**
     * Sets the final dialogue from the Character before disappearing or handing over the Item.
     * @param finalString
     */
    public void setFinalString(String finalString) {
        this.finalString = finalString;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Returns the Item the Character is holding while also removing the Item from the Character.
     * @return the Item held by the Character.
     */
    public Item getItem() {
        Item returnItem = this.item;
        this.item = null;
        System.out.println(finalString);
        return returnItem;
    }

    /**
     * @return the name of the Item the Character requests from the player.
     */
    public String getItemRequested() {
        return itemRequested;
    }

    public boolean hasBeenVisited() {
        return alreadyVisited;
    }
}
