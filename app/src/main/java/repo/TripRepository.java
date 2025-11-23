package repo;

import java.util.ArrayList;
import java.util.List;

import entity.Trip;

public class TripRepository {
    public enum FilterType {
        TODAY,
        THIS_WEEK,
        THIS_MONTH,
        THIS_YEAR
    }

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

    public Trip findTrip(String tripName, String tripDestination, String tripDate) {
        for (Trip trip : tripList) {
            if (trip.getTripName().equals(tripName) &&
                trip.getTripDestination().equals(tripDestination) &&
                trip.getTripDate().equals(tripDate)) {
                return trip;
            }
        }
        return null;
    }

    public void updateTrip(Trip oldTrip, Trip newTrip) {
        int index = tripList.indexOf(oldTrip);
        if (index != -1) {
            // Preserve items from old trip
            newTrip.setItems(oldTrip.getItems());
            tripList.set(index, newTrip);
        }
    }

    public ArrayList<Trip> getFilteredTrips(FilterType filterType) {
        ArrayList<Trip> filteredTrips = new java.util.ArrayList<>();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        java.util.Calendar tripCalendar = java.util.Calendar.getInstance();

        for (Trip trip : tripList) {
            String tripDate = trip.getTripDate();

            String[] dateParts = tripDate.split("/");
            try {
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]) - 1;
                int year = Integer.parseInt(dateParts[2]);

                tripCalendar.set(year, month, day);

                boolean matches = false;

                switch (filterType) {
                    case TODAY:
                        matches = isSameDay(calendar, tripCalendar);
                        break;

                    case THIS_WEEK:
                        matches = isSameWeek(calendar, tripCalendar);
                        break;

                    case THIS_MONTH:
                        matches = isSameMonth(calendar, tripCalendar);
                        break;

                    case THIS_YEAR:
                        matches = isSameYear(calendar, tripCalendar);
                        break;
                }

                if (matches) {
                    filteredTrips.add(trip);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return filteredTrips;
    }

    private boolean isSameDay(java.util.Calendar cal1, java.util.Calendar cal2) {
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR);
    }

    private boolean isSameWeek(java.util.Calendar cal1, java.util.Calendar cal2) {
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.WEEK_OF_YEAR) == cal2.get(java.util.Calendar.WEEK_OF_YEAR);
    }

    private boolean isSameMonth(java.util.Calendar cal1, java.util.Calendar cal2) {
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH);
    }

    private boolean isSameYear(java.util.Calendar cal1, java.util.Calendar cal2) {
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR);
    }

}
