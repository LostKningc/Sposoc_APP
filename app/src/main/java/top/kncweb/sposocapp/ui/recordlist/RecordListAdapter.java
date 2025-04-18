package top.kncweb.sposocapp.ui.recordlist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.local.entity.ActivityRecord;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private List<ActivityRecord> recordList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ActivityRecord> list) {
        this.recordList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityRecord record = recordList.get(position);
        holder.textView.setText("Time: " + record.getRtime_start() +
                "\nType: " + record.getRtype() +
                "\nDistance: " + record.getDistance() + " m");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RecordDetailActivity.class);
            intent.putExtra("record_id", record.getId());
            Log.d("RecordListAdapter", "Clicked record ID: " + record.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textTime);
        }
    }
}
