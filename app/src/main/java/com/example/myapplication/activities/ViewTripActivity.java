package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private TripAdapter adapter;
    private ImageButton buttonBack;
    TripRepository tripRepository;

    // RadioButtons for manual selection handling
    private RadioButton radioAll, radioToday, radioThisWeek, radioThisMonth, radioThisYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        tripRepository = TripRepository.getInstance();
        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTripList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            Toast.makeText(this, "Clicked: " + trip.getTripName(), Toast.LENGTH_SHORT).show();
        });

        adapter.setOnEditClickListener(this::handleEditTrip);

        adapter.setOnDeleteClickListener(this::showDeleteConfirmationDialog);

        adapter.setOnDoubleClickListener(this::handleDoubleClick);

        radioGroup = findViewById(R.id.radioGroupTrips);

        // Initialize all RadioButtons
        radioAll = findViewById(R.id.radioAll);
        radioToday = findViewById(R.id.radioToday);
        radioThisWeek = findViewById(R.id.radioThisWeek);
        radioThisMonth = findViewById(R.id.radioThisMonth);
        radioThisYear = findViewById(R.id.radioThisYear);

        // Set up custom click listeners for mutual exclusivity
        setupRadioButtonListeners();

        refreshTripList();
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

    private void handleDoubleClick(Trip trip, int position) {
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
        adapter.updateData(filteredTrips);
    }
}
