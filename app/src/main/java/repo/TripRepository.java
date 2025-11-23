package repo;

import java.util.List;

import entity.Trip;

public class TripRepository {
    private static TripRepository instance;
    private final List<Trip> tripList;

    private TripRepository() {
        tripList = new java.util.ArrayList<>();
    }

    public static synchronized TripRepository getInstance() {
        if (instance == null) {
            instance = new TripRepository();
        }
        return instance;
    }

    public void addTrip(Trip trip) {
        tripList.add(trip);
    }

    public List<Trip> getAllTrips() {
        return new java.util.ArrayList<>(tripList);
    }

    public void clearTrips() {
        tripList.clear();
    }

    public void removeTrip(Trip trip) {
        tripList.remove(trip);
    }
}
