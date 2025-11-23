package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import adapter.ItemAdapter;
import entity.Item;
import entity.Trip;
import repo.TripRepository;

public class ViewItemsActivity extends AppCompatActivity {
    private ListView listViewItems;
    private TextView textViewTripName;
    private TextView textViewProgress;
    private TextView textViewEmpty;
    private ProgressBar progressBarPacking;
    private FloatingActionButton fabAddItem;
    private ImageButton buttonBack;

    private ItemAdapter itemAdapter;
    private Trip currentTrip;
    private int tripPosition;
    private TripRepository tripRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        tripRepository = TripRepository.getInstance();

        tripPosition = getIntent().getIntExtra("TRIP_POSITION", -1);
        if (tripPosition == -1) {
            Toast.makeText(this, "Error loading trip", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentTrip = tripRepository.getAllTrips().get(tripPosition);

        initializeViews();
        setupListView();
        updateProgress();
    }

    private void initializeViews() {
        buttonBack = findViewById(R.id.buttonBack);
        textViewTripName = findViewById(R.id.textViewTripName);
        textViewProgress = findViewById(R.id.textViewProgress);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        progressBarPacking = findViewById(R.id.progressBarPacking);
        listViewItems = findViewById(R.id.listViewItems);
        fabAddItem = findViewById(R.id.fabAddItem);

        textViewTripName.setText(currentTrip.getTripName());

        buttonBack.setOnClickListener(v -> finish());

        fabAddItem.setOnClickListener(v -> showAddItemDialog());
    }

    private void setupListView() {
        itemAdapter = new ItemAdapter(this, currentTrip.getItems());
        listViewItems.setAdapter(itemAdapter);

        itemAdapter.setOnItemCheckedListener(this::updateProgress);

        itemAdapter.setOnDeleteClickListener(this::showDeleteItemDialog);

        updateEmptyView();
    }

    private void showAddItemDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);

        TextInputEditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        Spinner spinnerQuantity = dialogView.findViewById(R.id.spinnerQuantity);
        TextInputEditText editTextItemDescription = dialogView.findViewById(R.id.editTextItemDescription);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        Button buttonSave = dialogView.findViewById(R.id.buttonSave);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(v -> {
            String itemName = editTextItemName.getText() != null ? editTextItemName.getText().toString().trim() : "";
            String category = spinnerCategory.getSelectedItem().toString();
            String quantityStr = spinnerQuantity.getSelectedItem().toString();
            String description = editTextItemDescription.getText() != null ? editTextItemDescription.getText().toString().trim() : "";

            if (itemName.isEmpty()) {
                editTextItemName.setError("Item name is required");
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            Item newItem = new Item(itemName, quantity, description, category);
            currentTrip.addItem(newItem);

            itemAdapter.notifyDataSetChanged();
            updateProgress();
            updateEmptyView();

            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDeleteItemDialog(Item item, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage(getString(R.string.delete_item_confirm))
                .setPositiveButton("Delete", (dialog, which) -> {
                    currentTrip.removeItem(item);
                    itemAdapter.notifyDataSetChanged();
                    updateProgress();
                    updateEmptyView();
                    Toast.makeText(this, getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void updateProgress() {
        int totalItems = currentTrip.getTotalItemsCount();
        int packedItems = currentTrip.getPackedItemsCount();

        textViewProgress.setText(getString(R.string.packing_progress, packedItems, totalItems));

        int progress = totalItems > 0 ? (packedItems * 100) / totalItems : 0;
        progressBarPacking.setProgress(progress);
    }

    private void updateEmptyView() {
        if (currentTrip.getItems().isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            listViewItems.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            listViewItems.setVisibility(View.VISIBLE);
        }
    }
}

