package com.example.myapplication.activities;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import adapter.ItemAdapter;
import entity.Item;
import entity.Trip;
import repo.TripRepository;

public class AddTripActivity extends AppCompatActivity {
    private static final String TAG = "AddTripActivity";

    private Button buttonSaveTrip;
    private Button buttonAddItem;
    private DatePicker datePicker;
    private EditText editTextDescription;
    private EditText editTextTitle;
    private ImageButton buttonBack;
    private ListView listViewPackingItems;
    private TextView textViewItemsEmpty;
    private TripRepository tripRepository;

    private boolean isEditMode = false;
    private Trip originalTrip;
    private ArrayList<Item> packingItems;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(com.example.myapplication.R.layout.activity_add_trip);
        tripRepository = TripRepository.getInstance();
        tripRepository.initialize(this);
        packingItems = new ArrayList<>();
        initializeViews();
        loadEditData();
        initializeListeners();
        setupPackingList();
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

    private void initializeViews() {
        buttonSaveTrip = findViewById(com.example.myapplication.R.id.buttonSaveTrip);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        datePicker = findViewById(com.example.myapplication.R.id.datePicker);
        editTextDescription = findViewById(com.example.myapplication.R.id.editTextDescription);
        editTextTitle = findViewById(com.example.myapplication.R.id.editTextTitle);
        buttonBack = findViewById(com.example.myapplication.R.id.buttonBack);
        listViewPackingItems = findViewById(R.id.listViewPackingItems);
        textViewItemsEmpty = findViewById(R.id.textViewItemsEmpty);
    }

    private void loadEditData() {
        isEditMode = getIntent().getBooleanExtra("EDIT_MODE", false);

        if (isEditMode) {
            String tripName = getIntent().getStringExtra("TRIP_NAME");
            String tripDestination = getIntent().getStringExtra("TRIP_DESTINATION");
            String tripDate = getIntent().getStringExtra("TRIP_DATE");
            int tripPosition = getIntent().getIntExtra("TRIP_POSITION", -1);

            if (tripPosition != -1) {
                originalTrip = tripRepository.getAllTrips().get(tripPosition);
                packingItems.addAll(originalTrip.getItems());
            } else {
                originalTrip = new Trip(tripName, tripDestination, tripDate);
            }

            editTextTitle.setText(tripName);
            editTextDescription.setText(tripDestination);

            if (tripDate != null && !tripDate.isEmpty()) {
                String[] dateParts = tripDate.split("/");
                if (dateParts.length == 3) {
                    try {
                        int day = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]) - 1;
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

        buttonAddItem.setOnClickListener(v -> showAddItemDialog());

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

            for (Item item : packingItems) {
                trip.addItem(item);
            }

            if (isEditMode && originalTrip != null) {
                tripRepository.removeTrip(originalTrip);
                tripRepository.addTrip(trip);
                Toast.makeText(this, "Trip Updated", Toast.LENGTH_SHORT).show();
            } else {
                tripRepository.addTrip(trip);
                Toast.makeText(this, "Trip Saved", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }

    private void setupPackingList() {
        itemAdapter = new ItemAdapter(this, packingItems);
        listViewPackingItems.setAdapter(itemAdapter);

        itemAdapter.setOnItemCheckedListener(() -> {
        });

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
            packingItems.add(newItem);

            itemAdapter.notifyDataSetChanged();
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
                    packingItems.remove(item);
                    itemAdapter.notifyDataSetChanged();
                    updateEmptyView();
                    Toast.makeText(this, getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void updateEmptyView() {
        if (packingItems.isEmpty()) {
            textViewItemsEmpty.setVisibility(View.VISIBLE);
            listViewPackingItems.setVisibility(View.GONE);
        } else {
            textViewItemsEmpty.setVisibility(View.GONE);
            listViewPackingItems.setVisibility(View.VISIBLE);
        }
    }
}
