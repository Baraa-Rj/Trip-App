package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {
    private Button buttonViewTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initializeButtons();
        initializeListeners();
    }

    private void initializeButtons() {
        buttonViewTrips = findViewById(R.id.buttonViewTrips);
    }
    public void initializeListeners() {
        buttonViewTrips.setOnClickListener(v -> {

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}