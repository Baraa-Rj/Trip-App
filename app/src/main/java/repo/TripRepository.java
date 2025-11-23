package repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import entity.Trip;

public class TripRepository {
    private static final String TAG = "TripRepository";
    private static final String PREFS_NAME = "TripAppPreferences";
    private static final String KEY_TRIPS = "trips_list";

    public enum FilterType {
        TODAY,
        THIS_WEEK,
        THIS_MONTH,
        THIS_YEAR
    }

    private static TripRepository instance;
    private final List<Trip> tripList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private TripRepository() {
        tripList = new ArrayList<>();
        gson = new Gson();
    }

    public static synchronized TripRepository getInstance() {
        if (instance == null) {
            instance = new TripRepository();
        }
        return instance;
    }

    public void initialize(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            loadTripsFromPreferences();
            Log.d(TAG, "Repository initialized");
        }
    }

    private void loadTripsFromPreferences() {
        String json = sharedPreferences.getString(KEY_TRIPS, null);
        if (json != null && !json.isEmpty()) {
            try {
                Type type = new TypeToken<ArrayList<Trip>>(){}.getType();
                ArrayList<Trip> loadedTrips = gson.fromJson(json, type);
                if (loadedTrips != null) {
                    tripList.clear();
                    tripList.addAll(loadedTrips);
                    Log.d(TAG, "Loaded " + tripList.size() + " trips from SharedPreferences");
                } else {
                    Log.w(TAG, "Loaded trips list is null");
                }
            } catch (com.google.gson.JsonSyntaxException e) {
                Log.e(TAG, "Error parsing JSON, clearing corrupted data", e);
                sharedPreferences.edit().remove(KEY_TRIPS).apply();
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error loading trips", e);
            }
        } else {
            Log.d(TAG, "No saved trips found");
        }
    }

    private void saveTripsToPreferences() {
        if (sharedPreferences != null) {
            String json = gson.toJson(tripList);
            sharedPreferences.edit().putString(KEY_TRIPS, json).apply();
            Log.d(TAG, "Saved " + tripList.size() + " trips to SharedPreferences");
        }
    }

    public void addTrip(Trip trip) {
        if (trip == null) {
            Log.e(TAG, "Cannot add null trip");
            return;
        }
        if (trip.getTripName() == null || trip.getTripName().trim().isEmpty()) {
            Log.e(TAG, "Cannot add trip with empty name");
            return;
        }
        tripList.add(trip);
        saveTripsToPreferences();
        Log.d(TAG, "Added trip: " + trip.getTripName());
    }

    public List<Trip> getAllTrips() {
        return new ArrayList<>(tripList);
    }

    public void clearTrips() {
        tripList.clear();
        saveTripsToPreferences();
        Log.d(TAG, "Cleared all trips");
    }

    public void removeTrip(Trip trip) {
        tripList.remove(trip);
        saveTripsToPreferences();
        Log.d(TAG, "Removed trip: " + trip.getTripName());
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
                Log.e(TAG, "Error parsing date: " + tripDate, e);
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
