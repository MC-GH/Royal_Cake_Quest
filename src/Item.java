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

    public Item uncover() {
        this.isCovered = false;
        return coveredItem;
    }

    public Item getCoveredItem() {
        return this.coveredItem;
    }
}
