package adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

import entity.Item;

public class ItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Item> items;
    private OnItemCheckedListener onItemCheckedListener;
    private OnDeleteClickListener onDeleteClickListener;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
            holder = new ViewHolder();
            holder.checkBoxPacked = convertView.findViewById(R.id.checkBoxPacked);
            holder.textViewItemName = convertView.findViewById(R.id.textViewItemName);
            holder.textViewCategory = convertView.findViewById(R.id.textViewCategory);
            holder.textViewQuantity = convertView.findViewById(R.id.textViewQuantity);
            holder.textViewDescription = convertView.findViewById(R.id.textViewDescription);
            holder.buttonDelete = convertView.findViewById(R.id.buttonDeleteItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = items.get(position);

        holder.checkBoxPacked.setChecked(item.isPacked());
        holder.textViewItemName.setText(item.getItemName());
        holder.textViewCategory.setText(item.getCategory());
        holder.textViewQuantity.setText("Qty: " + item.getItemQuantity());
        holder.textViewDescription.setText(item.getItemDescription());

        // Apply strikethrough if packed
        if (item.isPacked()) {
            holder.textViewItemName.setPaintFlags(holder.textViewItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textViewItemName.setAlpha(0.6f);
        } else {
            holder.textViewItemName.setPaintFlags(holder.textViewItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.textViewItemName.setAlpha(1.0f);
        }

        // Set category background color
        setCategoryColor(holder.textViewCategory, item.getCategory());

        // CheckBox listener
        holder.checkBoxPacked.setOnCheckedChangeListener(null); // Clear previous listener
        holder.checkBoxPacked.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setPacked(isChecked);
            if (onItemCheckedListener != null) {
                onItemCheckedListener.onItemChecked();
            }
            notifyDataSetChanged();
        });

        // Delete button listener
        holder.buttonDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(item, position);
            }
        });

        return convertView;
    }

    private void setCategoryColor(TextView textView, String category) {
        int color;
        switch (category) {
            case "Clothing":
                color = 0xFF2196F3; // Blue
                break;
            case "Toiletries":
                color = 0xFF4CAF50; // Green
                break;
            case "Electronics":
                color = 0xFFFF9800; // Orange
                break;
            case "Documents":
                color = 0xFFF44336; // Red
                break;
            case "Medicines":
                color = 0xFF9C27B0; // Purple
                break;
            case "Food & Snacks":
                color = 0xFFFFEB3B; // Yellow
                break;
            case "Sports & Recreation":
                color = 0xFF00BCD4; // Cyan
                break;
            default:
                color = 0xFF9E9E9E; // Gray
                break;
        }
        textView.setBackgroundColor(color);
    }

    public void updateData(ArrayList<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public interface OnItemCheckedListener {
        void onItemChecked();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Item item, int position);
    }

    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        this.onItemCheckedListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    static class ViewHolder {
        CheckBox checkBoxPacked;
        TextView textViewItemName;
        TextView textViewCategory;
        TextView textViewQuantity;
        TextView textViewDescription;
        ImageButton buttonDelete;
    }
}

