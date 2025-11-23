package entity;

public class Item {
    private String itemName;
    private int itemQuantity;
    private String itemDescription;

    public Item(String itemName, int itemQuantity, String itemDescription) {
        setItemName(itemName);
        setItemQuantity(itemQuantity);
        setItemDescription(itemDescription);
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

}
