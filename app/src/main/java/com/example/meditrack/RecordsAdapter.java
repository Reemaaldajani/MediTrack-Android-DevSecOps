package com.example.meditrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordViewHolder> {

    private Context context;
    private List<MedicalRecord> recordList;
    private OnRecordClickListener listener;

    public interface OnRecordClickListener {
        void onDeleteClick(int position);
        void onStatusToggle(int position, boolean isCompleted);
        void onItemClick(int position);
    }

    public RecordsAdapter(Context context, List<MedicalRecord> recordList, OnRecordClickListener listener) {
        this.context = context;
        this.recordList = recordList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        MedicalRecord record = recordList.get(position);
        holder.tvName.setText(record.getName());
        holder.tvType.setText(record.getType());
        holder.tvDateTime.setText(record.getDateTime());
        holder.cbCompleted.setChecked(record.isCompleted());

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(position));
        
        holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onStatusToggle(position, isChecked);
        });

        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public void updateList(List<MedicalRecord> newList) {
        this.recordList = newList;
        notifyDataSetChanged();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvDateTime;
        CheckBox cbCompleted;
        ImageButton btnDelete;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            cbCompleted = itemView.findViewById(R.id.cbCompleted);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}