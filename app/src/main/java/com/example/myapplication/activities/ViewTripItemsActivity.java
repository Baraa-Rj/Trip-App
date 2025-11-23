package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import adapter.ItemAdapter;
import entity.Item;
import entity.Trip;
import repo.TripRepository;

public class ViewTripItemsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewItems;
    private ItemAdapter itemAdapter;
    private TextView textViewTripName;
    private TextView textViewTripInfo;
    private TextView textViewNoItems;
    private FloatingActionButton fabAddItem;
    private ImageButton buttonBack;
    private TripRepository tripRepository;
    private Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip_items);

        tripRepository = TripRepository.getInstance();
        loadTripData();
        initializeViews();
        initializeListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshItemList();
    }

    private void loadTripData() {
        String tripName = getIntent().getStringExtra("TRIP_NAME");
        String tripDestination = getIntent().getStringExtra("TRIP_DESTINATION");
        String tripDate = getIntent().getStringExtra("TRIP_DATE");

        currentTrip = tripRepository.findTrip(tripName, tripDestination, tripDate);

        if (currentTrip == null) {
            Toast.makeText(this, "Error: Trip not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        buttonBack = findViewById(R.id.buttonBack);
        textViewTripName = findViewById(R.id.textViewTripName);
        textViewTripInfo = findViewById(R.id.textViewTripInfo);
        textViewNoItems = findViewById(R.id.textViewNoItems);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        fabAddItem = findViewById(R.id.fabAddItem);

        textViewTripName.setText(currentTrip.getTripName());
        textViewTripInfo.setText(currentTrip.getTripDestination() + " • " + currentTrip.getTripDate());

        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setHasFixedSize(true);
        itemAdapter = new ItemAdapter();
        recyclerViewItems.setAdapter(itemAdapter);
    }

    private void initializeListeners() {
        buttonBack.setOnClickListener(v -> finish());

        fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddItemActivity.class);
            intent.putExtra("TRIP_NAME", currentTrip.getTripName());
            intent.putExtra("TRIP_DESTINATION", currentTrip.getTripDestination());
            intent.putExtra("TRIP_DATE", currentTrip.getTripDate());
            startActivity(intent);
        });

        itemAdapter.setOnEditClickListener(this::handleEditItem);
        itemAdapter.setOnDeleteClickListener(this::showDeleteConfirmationDialog);
    }

    private void handleEditItem(Item item, int position) {
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("EDIT_MODE", true);
        intent.putExtra("TRIP_NAME", currentTrip.getTripName());
        intent.putExtra("TRIP_DESTINATION", currentTrip.getTripDestination());
        intent.putExtra("TRIP_DATE", currentTrip.getTripDate());
        intent.putExtra("ITEM_NAME", item.getItemName());
        intent.putExtra("ITEM_DESCRIPTION", item.getItemDescription());
        intent.putExtra("ITEM_QUANTITY", item.getItemQuantity());
        startActivity(intent);
    }

    private void showDeleteConfirmationDialog(Item item, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete \"" + item.getItemName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    currentTrip.removeItem(item);
                    refreshItemList();
                    Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void refreshItemList() {
        if (currentTrip.getItems().isEmpty()) {
            textViewNoItems.setVisibility(View.VISIBLE);
            recyclerViewItems.setVisibility(View.GONE);
        } else {
            textViewNoItems.setVisibility(View.GONE);
            recyclerViewItems.setVisibility(View.VISIBLE);
            itemAdapter.updateData(currentTrip.getItems());
        }
    }
}
