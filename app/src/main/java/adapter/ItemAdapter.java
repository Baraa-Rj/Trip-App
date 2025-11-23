package adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

import entity.Item;
import viewholder.ItemViewHolder;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private final ArrayList<Item> items = new ArrayList<>();
    private OnItemClickListener listener;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);

        holder.bindData(item.getItemName(), item.getItemDescription(), item.getItemQuantity());

        holder.getButtonEdit().setOnClickListener(v -> {
            if (editClickListener != null) {
                editClickListener.onEditClick(item, position);
            }
        });

        holder.getButtonDelete().setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ArrayList<Item> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public interface OnEditClickListener {
        void onEditClick(Item item, int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Item item, int position);
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
