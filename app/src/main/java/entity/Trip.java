package entity;

public class Trip {
    private String tripName;
    private String tripDestination;
    private String tripDate;

    public Trip(String tripName, String tripDestination, String tripDate) {
        setTripName(tripName);
        setTripDestination(tripDestination);
        setTripDate(tripDate);
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

}
