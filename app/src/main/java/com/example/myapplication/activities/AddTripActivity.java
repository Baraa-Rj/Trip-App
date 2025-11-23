package com.example.myapplication.activities;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import entity.Trip;
import repo.TripRepository;

public class AddTripActivity extends AppCompatActivity {

    private Button buttonSaveTrip;
    private DatePicker datePicker;
    private EditText editTextDescription;
    private EditText editTextTitle;
    private ImageButton buttonBack;
    private TripRepository tripRepository;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.myapplication.R.layout.activity_add_trip);
        tripRepository = TripRepository.getInstance();
        initializeViews();
        initializeListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initializeViews() {
        buttonSaveTrip = findViewById(com.example.myapplication.R.id.buttonSaveTrip);
        datePicker = findViewById(com.example.myapplication.R.id.datePicker);
        editTextDescription = findViewById(com.example.myapplication.R.id.editTextDescription);
        editTextTitle = findViewById(com.example.myapplication.R.id.editTextTitle);
        buttonBack = findViewById(com.example.myapplication.R.id.buttonBack);
    }


    private void initializeListeners() {
        buttonBack.setOnClickListener(v -> finish());
        buttonSaveTrip.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            String date = day + "/" + month + "/" + year;
            tripRepository.addTrip(new Trip(title, date, description));
            Toast.makeText(this, "Trip Saved", Toast.LENGTH_SHORT).show();
        });
    }
}
