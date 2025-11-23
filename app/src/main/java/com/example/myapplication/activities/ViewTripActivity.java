package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;

public class ViewTripActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        initializeViews();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewTrips);
        radioGroup = findViewById(R.id.radioGroupTrips);
    }

}
