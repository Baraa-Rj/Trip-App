package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

import adapter.TripAdapter;
import entity.Trip;
import repo.TripRepository;

public class ViewTripActivity extends AppCompatActivity {
    private static final String TAG = "ViewTripActivity";
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private TripAdapter adapter;
    private ImageButton buttonBack;
    private SearchView searchViewTrips;
    TripRepository tripRepository;

    private RadioButton radioAll, radioToday, radioThisWeek, radioThisMonth, radioThisYear;
    private String currentSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_view_trip);
        tripRepository = TripRepository.getInstance();
        tripRepository.initialize(this);
        initializeViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        refreshTripList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void initializeViews() {
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewTrips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new TripAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(trip -> {
            int position = tripRepository.getAllTrips().indexOf(trip);
            if (position != -1) {
                handleOpenItems(trip, position);
            }
        });

        adapter.setOnEditClickListener(this::handleEditTrip);

        adapter.setOnDeleteClickListener(this::showDeleteConfirmationDialog);


        radioGroup = findViewById(R.id.radioGroupTrips);
        searchViewTrips = findViewById(R.id.searchViewTrips);

        initializeRadioButtons();
        setupRadioButtonListeners();
        setupSearchView();

        refreshTripList();
    }

    private void initializeRadioButtons() {
        radioAll = findViewById(R.id.radioAll);
        radioToday = findViewById(R.id.radioToday);
        radioThisWeek = findViewById(R.id.radioThisWeek);
        radioThisMonth = findViewById(R.id.radioThisMonth);
        radioThisYear = findViewById(R.id.radioThisYear);
    }

    private void handleEditTrip(Trip trip, int position) {
        Intent intent = new Intent(this, AddTripActivity.class);
        intent.putExtra("EDIT_MODE", true);
        intent.putExtra("TRIP_POSITION", position);
        intent.putExtra("TRIP_NAME", trip.getTripName());
        intent.putExtra("TRIP_DESTINATION", trip.getTripDestination());
        intent.putExtra("TRIP_DATE", trip.getTripDate());
        startActivity(intent);
    }

    private void handleOpenItems(Trip trip, int position) {
        Intent intent = new Intent(this, ViewItemsActivity.class);
        intent.putExtra("TRIP_POSITION", position);
        startActivity(intent);
    }

    private void showDeleteConfirmationDialog(Trip trip, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Trip")
                .setMessage("Are you sure you want to delete \"" + trip.getTripName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    tripRepository.removeTrip(trip);
                    refreshTripList();
                    Toast.makeText(this, "Trip deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setupRadioButtonListeners() {
        radioAll.setOnClickListener(v -> {
            uncheckAllExcept(radioAll);
            refreshTripList();
        });

        radioToday.setOnClickListener(v -> {
            uncheckAllExcept(radioToday);
            refreshTripList();
        });

        radioThisWeek.setOnClickListener(v -> {
            uncheckAllExcept(radioThisWeek);
            refreshTripList();
        });

        radioThisMonth.setOnClickListener(v -> {
            uncheckAllExcept(radioThisMonth);
            refreshTripList();
        });

        radioThisYear.setOnClickListener(v -> {
            uncheckAllExcept(radioThisYear);
            refreshTripList();
        });
    }

    private void uncheckAllExcept(RadioButton selected) {
        if (radioAll != selected) radioAll.setChecked(false);
        if (radioToday != selected) radioToday.setChecked(false);
        if (radioThisWeek != selected) radioThisWeek.setChecked(false);
        if (radioThisMonth != selected) radioThisMonth.setChecked(false);
        if (radioThisYear != selected) radioThisYear.setChecked(false);
        selected.setChecked(true);
    }

    private void setupSearchView() {
        searchViewTrips.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                refreshTripList();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                refreshTripList();
                return true;
            }
        });
    }

    private void refreshTripList() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        ArrayList<Trip> filteredTrips;
        if (selectedId == R.id.radioAll || radioAll.isChecked()) {
            filteredTrips = new ArrayList<>(tripRepository.getAllTrips());
        } else if (selectedId == R.id.radioToday || radioToday.isChecked()) {
            filteredTrips = tripRepository.getFilteredTrips(TripRepository.FilterType.TODAY);
        } else if (selectedId == R.id.radioThisWeek || radioThisWeek.isChecked()) {
            filteredTrips = tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_WEEK);
        } else if (selectedId == R.id.radioThisMonth || radioThisMonth.isChecked()) {
            filteredTrips = tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_MONTH);
        } else if (selectedId == R.id.radioThisYear || radioThisYear.isChecked()) {
            filteredTrips = tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_YEAR);
        } else {
            filteredTrips = new ArrayList<>(tripRepository.getAllTrips());
        }

        if (!currentSearchQuery.isEmpty()) {
            filteredTrips = searchTrips(filteredTrips, currentSearchQuery);
        }

        adapter.updateData(filteredTrips);
    }

    private ArrayList<Trip> searchTrips(ArrayList<Trip> trips, String query) {
        ArrayList<Trip> searchResults = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (Trip trip : trips) {
            if (trip.getTripName().toLowerCase().contains(lowerCaseQuery) ||
                trip.getTripDestination().toLowerCase().contains(lowerCaseQuery) ||
                trip.getTripDate().contains(lowerCaseQuery)) {
                searchResults.add(trip);
            }
        }

        return searchResults;
    }
}
