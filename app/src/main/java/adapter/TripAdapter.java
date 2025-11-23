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
    private ArrayList<Trip> trips = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_layout,parent,false);
        TripViewHolder.OnItemClickListener viewHolderListener = position -> {
            if (listener != null && position >= 0 && position < trips.size()) {
                listener.onItemClick(trips.get(position));
            }
        };
        return new TripViewHolder(view, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.bindData(trip.getTripName(), trip.getTripDate(), trip.getTripDestination());
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Trip trip);
    }
}
