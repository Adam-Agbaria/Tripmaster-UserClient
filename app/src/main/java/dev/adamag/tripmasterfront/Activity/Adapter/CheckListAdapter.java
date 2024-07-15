package dev.adamag.tripmasterfront.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.CheckListItem;
import dev.adamag.tripmasterfront.Activity.CheckListActivity.DisplayCheckListActivity;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.CheckListViewHolder> {

    private List<CheckListItem> checkListItems;
    private Context context;

    public CheckListAdapter(List<CheckListItem> checkListItems, Context context) {
        this.checkListItems = checkListItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CheckListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checklist, parent, false);
        return new CheckListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListViewHolder holder, int position) {
        CheckListItem item = checkListItems.get(position);
        holder.itemName.setText(item.getName());
        holder.itemAmount.setText(String.valueOf(item.getAmount()));
        holder.itemCheckBox.setChecked(item.isChecked());

        holder.itemCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            ((DisplayCheckListActivity) context).saveCheckList(); // Save checklist whenever a checkbox is toggled
        });

        holder.removeButton.setOnClickListener(v -> {
            checkListItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, checkListItems.size());
            ((DisplayCheckListActivity) context).saveCheckList(); // Save checklist after removing an item
        });
    }

    @Override
    public int getItemCount() {
        return checkListItems.size();
    }

    public static class CheckListViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemAmount;
        CheckBox itemCheckBox;
        Button removeButton;

        public CheckListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemAmount = itemView.findViewById(R.id.item_amount);
            itemCheckBox = itemView.findViewById(R.id.item_checkbox);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }
}
