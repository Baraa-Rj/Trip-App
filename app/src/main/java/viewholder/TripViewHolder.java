package viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class TripViewHolder extends RecyclerView.ViewHolder {
    private TextView tripNameTextView;
    private TextView tripDateTextView;
    private TextView tripDescriptionTextView;
    private Button buttonEdit;
    private Button buttonDelete;

    public TripViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
    }

    public void bindData(String name, String date, String description) {
        tripNameTextView.setText(name);
        tripDateTextView.setText(date);
        tripDescriptionTextView.setText(description);
    }

    public Button getButtonEdit() {
        return buttonEdit;
    }

    public Button getButtonDelete() {
        return buttonDelete;
    }

    private void initializeViews() {
        tripNameTextView = itemView.findViewById(R.id.textViewTripName);
        tripDescriptionTextView = itemView.findViewById(R.id.textViewDescription);
        tripDateTextView = itemView.findViewById(R.id.textViewDate);
        buttonEdit = itemView.findViewById(R.id.buttonEdit);
        buttonDelete = itemView.findViewById(R.id.buttonDelete);
    }
}
