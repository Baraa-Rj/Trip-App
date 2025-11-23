package entity;

public class Item {
    private String itemName;
    private int itemQuantity;
    private String itemDescription;
    private boolean isPacked;
    private String category;

    public Item(String itemName, int itemQuantity, String itemDescription, String category) {
        setItemName(itemName);
        setItemQuantity(itemQuantity);
        setItemDescription(itemDescription);
        setCategory(category);
        this.isPacked = false;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemDescription() {
        return itemDescription;
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public boolean isPacked() {
        return isPacked;
    }

    public void setPacked(boolean packed) {
        isPacked = packed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
