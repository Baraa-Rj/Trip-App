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

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(trip);
            }
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }
}
