package entity;

import java.util.ArrayList;

public class Trip {
    private String tripName;
    private String tripDestination;
    private String tripDate;
    private ArrayList<Item> items;


    public Trip(String tripName, String tripDestination, String tripDate) {
        setTripName(tripName);
        setTripDestination(tripDestination);
        setTripDate(tripDate);
        this.items = new ArrayList<>();
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
    }

    public int getPackedItemsCount() {
        int count = 0;
        for (Item item : items) {
            if (item.isPacked()) {
                count++;
            }
        }
        return count;
    }

    public int getTotalItemsCount() {
        return items.size();
    }

}
