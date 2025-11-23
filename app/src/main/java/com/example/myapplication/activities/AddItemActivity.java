package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

import entity.Item;
import entity.Trip;
import repo.TripRepository;

public class AddItemActivity extends AppCompatActivity {

    private TextInputEditText editTextItemName;
    private TextInputEditText editTextItemDescription;
    private TextInputEditText editTextItemQuantity;
    private Button buttonSaveItem;
    private ImageButton buttonBack;
    private TripRepository tripRepository;

    private boolean isEditMode = false;
    private Item originalItem;
    private Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        tripRepository = TripRepository.getInstance();
        initializeViews();
        loadTripAndItemData();
        initializeListeners();
    }

    private void initializeViews() {
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextItemDescription = findViewById(R.id.editTextItemDescription);
        editTextItemQuantity = findViewById(R.id.editTextItemQuantity);
        buttonSaveItem = findViewById(R.id.buttonSaveItem);
        buttonBack = findViewById(R.id.buttonBack);
    }

    private void loadTripAndItemData() {
        String tripName = getIntent().getStringExtra("TRIP_NAME");
        String tripDestination = getIntent().getStringExtra("TRIP_DESTINATION");
        String tripDate = getIntent().getStringExtra("TRIP_DATE");

        currentTrip = tripRepository.findTrip(tripName, tripDestination, tripDate);

        if (currentTrip == null) {
            Toast.makeText(this, "Error: Trip not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        isEditMode = getIntent().getBooleanExtra("EDIT_MODE", false);

        if (isEditMode) {
            String itemName = getIntent().getStringExtra("ITEM_NAME");
            String itemDescription = getIntent().getStringExtra("ITEM_DESCRIPTION");
            int itemQuantity = getIntent().getIntExtra("ITEM_QUANTITY", 1);

            originalItem = new Item(itemName, itemQuantity, itemDescription);

            editTextItemName.setText(itemName);
            editTextItemDescription.setText(itemDescription);
            editTextItemQuantity.setText(String.valueOf(itemQuantity));

            buttonSaveItem.setText("Update Item");
        }
    }

    private void initializeListeners() {
        buttonBack.setOnClickListener(v -> finish());

        buttonSaveItem.setOnClickListener(v -> {
            String itemName = editTextItemName.getText().toString().trim();
            String itemDescription = editTextItemDescription.getText().toString().trim();
            String quantityStr = editTextItemQuantity.getText().toString().trim();

            if (itemName.isEmpty()) {
                editTextItemName.setError("Item name is required");
                return;
            }

            if (quantityStr.isEmpty()) {
                editTextItemQuantity.setError("Quantity is required");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    editTextItemQuantity.setError("Quantity must be positive");
                    return;
                }
            } catch (NumberFormatException e) {
                editTextItemQuantity.setError("Invalid quantity");
                return;
            }

            Item newItem = new Item(itemName, quantity, itemDescription);

            if (isEditMode && originalItem != null) {
                currentTrip.removeItem(originalItem);
                currentTrip.addItem(newItem);
                Toast.makeText(this, "Item Updated", Toast.LENGTH_SHORT).show();
            } else {
                currentTrip.addItem(newItem);
                Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }
}
