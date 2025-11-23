package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;


import adapter.TripAdapter;

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
        radioGroup = findViewById(R.id.radioGroupTrips);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioToday) {
                adapter.updateData(tripRepository.getFilteredTrips(TripRepository.FilterType.TODAY));
            } else if (checkedId == R.id.radioThisWeek) {
                adapter.updateData(tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_WEEK));
            } else if (checkedId == R.id.radioThisMonth) {
                adapter.updateData(tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_MONTH));
            } else if (checkedId == R.id.radioThisYear) {
                adapter.updateData(tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_YEAR));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (radioGroup.getCheckedRadioButtonId() == R.id.radioToday) {
            adapter.updateData(tripRepository.getFilteredTrips(TripRepository.FilterType.TODAY));
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioThisWeek) {
            adapter.updateData(tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_WEEK));
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioThisMonth) {
            adapter.updateData(tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_MONTH));
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioThisYear) {
            adapter.updateData(tripRepository.getFilteredTrips(TripRepository.FilterType.THIS_YEAR));
        }
    }
}
