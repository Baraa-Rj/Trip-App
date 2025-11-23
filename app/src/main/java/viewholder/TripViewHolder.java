package viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TripViewHolder extends RecyclerView.ViewHolder {
    private TextView tripNameTextView;
    private TextView tripDateTextView;
    private TextView tripDescriptionTextView;

    public TripViewHolder(@NonNull View itemView) {
        super(itemView);

    }
}
