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
import top.kncweb.sposocapp.util.ActivityCalculator;

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
                .inflate(R.layout.sport_record_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityRecord record = recordList.get(position);
        ActivityCalculator calculator = new ActivityCalculator(record);
        holder.sportType.setText(record.getRtype().toString());
        holder.distance.setText(String.format("%.2f km", calculator.calculateDistanceInKm()));
        holder.time.setText(calculator.calculateTimeDifferenceMinSec());
        holder.calories.setText(String.valueOf(calculator.getCalories()));

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
        TextView sportType;
        TextView distance;
        TextView time;
        TextView calories;

        public ViewHolder(View itemView) {
            super(itemView);
            sportType = itemView.findViewById(R.id.tv_sport_type);
            distance = itemView.findViewById(R.id.tv_distance);
            time = itemView.findViewById(R.id.tv_duration);
            calories =  itemView.findViewById(R.id.tv_calories);
        }
    }
}
