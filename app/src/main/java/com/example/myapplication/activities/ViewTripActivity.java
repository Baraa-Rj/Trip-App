package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
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

        radioGroup = findViewById(R.id.radioGroupTrips);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            refreshTripList();
        });

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

    private void refreshTripList() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        ArrayList<Trip> filteredTrips;

        if (selectedId == R.id.radioToday) {
            filteredTrips = tripRepository.getFilteredTrips(TripRepository.FilterType.TODAY);
        } else if (selectedId == R.id.radioThisWeek) {
            filteredTrips = tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_WEEK);
        } else if (selectedId == R.id.radioThisMonth) {
            filteredTrips = tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_MONTH);
        } else if (selectedId == R.id.radioThisYear) {
            filteredTrips = tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_YEAR);
        } else {
            filteredTrips = new ArrayList<>(tripRepository.getAllTrips());
        }
        adapter.updateData(filteredTrips);
    }
}
