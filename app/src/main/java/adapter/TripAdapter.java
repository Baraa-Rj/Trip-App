package adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

import entity.Trip;
import viewholder.TripViewHolder;

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {
    private final ArrayList<Trip> trips = new ArrayList<>();
    private OnItemClickListener listener;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnDoubleClickListener doubleClickListener;

    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // milliseconds
    private long lastClickTime = 0;

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_layout, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = trips.get(position);

        holder.bindData(trip.getTripName(), trip.getTripDate(), trip.getTripDestination());

        // Double-click listener on the entire item view
        holder.itemView.setOnClickListener(v -> {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                // Double click detected
                if (doubleClickListener != null) {
                    doubleClickListener.onDoubleClick(trip, position);
                }
                lastClickTime = 0; // Reset
            } else {
                // Single click
                if (listener != null) {
                    listener.onItemClick(trip);
                }
            }
            lastClickTime = clickTime;
        });

        holder.getButtonEdit().setOnClickListener(v -> {
            if (editClickListener != null) {
                editClickListener.onEditClick(trip, position);
            }
        });

        holder.getButtonDelete().setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(trip, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ArrayList<Trip> newTrips) {
        trips.clear();
        trips.addAll(newTrips);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Trip trip);
    }

    public interface OnEditClickListener {
        void onEditClick(Trip trip, int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Trip trip, int position);
    }

    public interface OnDoubleClickListener {
        void onDoubleClick(Trip trip, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener editClickListener) { // Fixed parameter type
        this.editClickListener = editClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener deleteClickListener) { // Fixed parameter type
        this.deleteClickListener = deleteClickListener;
    }

    public void setOnDoubleClickListener(OnDoubleClickListener doubleClickListener) {
        this.doubleClickListener = doubleClickListener;
    }
}
