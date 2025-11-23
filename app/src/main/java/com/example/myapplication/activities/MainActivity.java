package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import repo.TripRepository;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button buttonViewTrips;
    private Button buttonAddTrip;
    private TripRepository tripRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tripRepository = TripRepository.getInstance();
        tripRepository.initialize(this);

        initializeButtons();
        initializeListeners();
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

    private void initializeButtons() {
        buttonViewTrips = findViewById(R.id.buttonViewTrips);
        buttonAddTrip = findViewById(R.id.buttonAddTrip);
    }

    public void initializeListeners() {
        buttonViewTrips.setOnClickListener(v -> {
            Log.d(TAG, "View Trips button clicked");
            startActivity(new Intent(this, ViewTripActivity.class));
        });
        buttonAddTrip.setOnClickListener(v -> {
            Log.d(TAG, "Add Trip button clicked");
            startActivity(new Intent(this, AddTripActivity.class));
        });
    }
}