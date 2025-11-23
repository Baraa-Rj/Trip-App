package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;

import adapter.TripAdapter;

public class ViewTripActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private TripAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        initializeViews();
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
    }

}
