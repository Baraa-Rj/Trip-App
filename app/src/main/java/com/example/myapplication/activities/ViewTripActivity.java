package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;

import java.util.ArrayList;

import adapter.TripAdapter;
import entity.Trip;

public class ViewTripActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private TripAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        initializeViews();
        loadData();
    }

    private void initializeViews() {
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
                loadFilteredData("today");
            } else if (checkedId == R.id.radioThisWeek) {
                loadFilteredData("week");
            } else if (checkedId == R.id.radioThisMonth) {
                loadFilteredData("month");
            } else if (checkedId == R.id.radioThisYear) {
                loadFilteredData("year");
            }
        });
    }
    private void loadData(){
        // Load with default filter (today)
        loadFilteredData("today");
    }

    private void loadFilteredData(String filter) {
        ArrayList<Trip> trips = new ArrayList<>();

        // Sample data - in a real app, you would filter based on actual dates
        // For demonstration, showing different trips based on filter
        switch (filter) {
            case "today":
                trips.add(new Trip("Beach Vacation", "Hawaii", "2023-12-20"));
                break;
            case "week":
                trips.add(new Trip("Beach Vacation", "Hawaii", "2023-12-20"));
                trips.add(new Trip("Mountain Hiking", "Colorado", "2024-01-15"));
                break;
            case "month":
                trips.add(new Trip("Beach Vacation", "Hawaii", "2023-12-20"));
                trips.add(new Trip("Mountain Hiking", "Colorado", "2024-01-15"));
                trips.add(new Trip("City Tour", "New York", "2025-11-10"));
                break;
            case "year":
                trips.add(new Trip("Beach Vacation", "Hawaii", "2023-12-20"));
                trips.add(new Trip("Mountain Hiking", "Colorado", "2024-01-15"));
                trips.add(new Trip("City Tour", "New York", "2025-11-10"));
                trips.add(new Trip("Safari Adventure", "Kenya", "2025-08-05"));
                break;
        }

        adapter.updateData(trips);

    }

}
