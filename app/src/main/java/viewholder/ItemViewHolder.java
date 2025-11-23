package viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    private TextView itemNameTextView;
    private TextView itemDescriptionTextView;
    private TextView itemQuantityTextView;
    private Button buttonEditItem;
    private Button buttonDeleteItem;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
    }

    public void bindData(String name, String description, int quantity) {
        itemNameTextView.setText(name);
        itemDescriptionTextView.setText(description);
        itemQuantityTextView.setText("Quantity: " + quantity);
    }

    public Button getButtonEdit() {
        return buttonEditItem;
    }

    public Button getButtonDelete() {
        return buttonDeleteItem;
    }

    private void initializeViews() {
        itemNameTextView = itemView.findViewById(R.id.textViewItemName);
        itemDescriptionTextView = itemView.findViewById(R.id.textViewItemDescription);
        itemQuantityTextView = itemView.findViewById(R.id.textViewItemQuantity);
        buttonEditItem = itemView.findViewById(R.id.buttonEditItem);
        buttonDeleteItem = itemView.findViewById(R.id.buttonDeleteItem);
    }
}
