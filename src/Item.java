/**
 * Class which stores several attributes related to an Item.
 */
public class Item {
    private String name;
    private String description;
    private double weight;
    //Some items can cover another item. For example; picking up a strange rock can uncover something else
    private Item coveredItem;
    private boolean isCovered;

    public Item(String name, String description, double weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.isCovered = false;
    }

    /**
     * If we have an Item covering another Item, we can set the covered item with this method.
     * @param item The item that is covered by another item. (not visible)
     */
    public void setCoveredItem(Item item) {
        this.coveredItem = item;
        this.isCovered = true;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isCovered() {
        return this.isCovered;
    }

    public Item getCoveredItem() {
        return this.coveredItem;
    }
}
