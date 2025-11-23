package viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class TripViewHolder extends RecyclerView.ViewHolder {
    private TextView tripNameTextView;
    private TextView tripDateTextView;
    private TextView tripDescriptionTextView;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public TripViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        this.listener = listener;
        initializeViews();
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    public void bindData(String name, String date, String description) {
        tripNameTextView.setText(name);
        tripDateTextView.setText(date);
        tripDescriptionTextView.setText(description);
    }

    private void initializeViews() {
        tripDateTextView = itemView.findViewById(R.id.textViewDate);
        tripNameTextView = itemView.findViewById(R.id.textViewTitle);
        tripDescriptionTextView = itemView.findViewById(R.id.textViewDescription);
    }
}
