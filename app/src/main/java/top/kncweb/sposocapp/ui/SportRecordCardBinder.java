package top.kncweb.sposocapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.databinding.SportRecordCardBinding;
import top.kncweb.sposocapp.enums.ActivityType;
import top.kncweb.sposocapp.local.entity.ActivityRecord;
import top.kncweb.sposocapp.util.ActivityCalculator;

public class SportRecordCardBinder {

    private final SportRecordCardBinding binding;
    private final Context context;

    public SportRecordCardBinder(@NonNull SportRecordCardBinding binding) {
        this.binding = binding;
        this.context = binding.getRoot().getContext();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void bind(@NonNull ActivityRecord record) {
        ActivityCalculator calculator = new ActivityCalculator(record);

        binding.tvSportType.setText(record.getRtype().toString());
        binding.tvDistance.setText(String.format("%.1f km", record.getDistance()));
        binding.tvDuration.setText(calculator.calculateTimeDifferenceMinSec());
        binding.tvCalories.setText(calculator.getCalories() + " kcal");

        // 设置运动图标
        int iconRes = getIconResource(record.getRtype());
        binding.ivSportIcon.setImageResource(iconRes);

        // TODO: 如果你有运动轨迹图，也可以绑定到 ivSportMap
        // binding.ivSportMap.setImageBitmap(record.getMapImage());

        binding.getRoot().setVisibility(View.VISIBLE);
    }

    private int getIconResource(ActivityType type) {
        switch (type) {
            case run:
                return R.drawable.ic_running;
            case cycling:
                return R.drawable.ic_cycling;
            case walk:
                return R.drawable.ic_walking;
            default:
                return R.drawable.ic_running;
        }
    }

//    public ImageView getMapView() {
//        return binding.ivSportMap;
//    }
}
