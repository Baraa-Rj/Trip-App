package com.example.myapplication.activities;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.R;

import entity.Trip;
import repo.TripRepository;

public class AddTripActivity extends AppCompatActivity {

    private Button buttonSaveTrip;
    private DatePicker datePicker;
    private EditText editTextDescription;
    private EditText editTextTitle;
    private ImageButton buttonBack;
    private TripRepository tripRepository;

    private boolean isEditMode = false;
    private Trip originalTrip;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.myapplication.R.layout.activity_add_trip);
        tripRepository = TripRepository.getInstance();
        initializeViews();
        loadEditData();
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

    private void loadEditData() {
        isEditMode = getIntent().getBooleanExtra("EDIT_MODE", false);

        if (isEditMode) {
            String tripName = getIntent().getStringExtra("TRIP_NAME");
            String tripDestination = getIntent().getStringExtra("TRIP_DESTINATION");
            String tripDate = getIntent().getStringExtra("TRIP_DATE");

            originalTrip = new Trip(tripName, tripDestination, tripDate);

            editTextTitle.setText(tripName);
            editTextDescription.setText(tripDestination);

            if (tripDate != null && !tripDate.isEmpty()) {
                String[] dateParts = tripDate.split("/");
                if (dateParts.length == 3) {
                    try {
                        int day = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based
                        int year = Integer.parseInt(dateParts[2]);
                        datePicker.updateDate(year, month, day);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            buttonSaveTrip.setText(R.string.update_trip);
        }
    }


    private void initializeListeners() {
        buttonBack.setOnClickListener(v -> finish());
        buttonSaveTrip.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();

            if (title.isEmpty()) {
                editTextTitle.setError("Title is required");
                return;
            }

            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            String date = day + "/" + month + "/" + year;

            Trip trip = new Trip(title, description, date);

            if (isEditMode && originalTrip != null) {
                tripRepository.updateTrip(originalTrip, trip);
                Toast.makeText(this, "Trip Updated", Toast.LENGTH_SHORT).show();
            } else {
                tripRepository.addTrip(trip);
                Toast.makeText(this, "Trip Saved", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }
}
