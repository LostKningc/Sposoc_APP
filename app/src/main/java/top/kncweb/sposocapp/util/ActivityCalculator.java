package top.kncweb.sposocapp.util;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import top.kncweb.sposocapp.enums.ActivityType;
import top.kncweb.sposocapp.local.entity.ActivityRecord;

public class ActivityCalculator {

    private final ActivityRecord activityRecord;

    public ActivityCalculator(@NonNull ActivityRecord activityRecord) {
        this.activityRecord = activityRecord;
    }

    public long calculateTimeDifferenceInSeconds() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(activityRecord.getRtime_start(), formatter);
        LocalDateTime end = LocalDateTime.parse(activityRecord.getRtime_end(), formatter);
        return Duration.between(start, end).getSeconds();
    }

    public double calculateDistanceInKm() {
        return activityRecord.getDistance() / 1000.0;
    }

    @SuppressLint("DefaultLocale")
    public String calculateTimeDifferenceMinSec() {
        long totalSeconds = calculateTimeDifferenceInSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

    public long getCalories() {
        double caloriesPerKm;
        switch (activityRecord.getRtype()) {
            case run:
                caloriesPerKm = 60;
                break;
            case cycling:
                caloriesPerKm = 40;
                break;
            case walk:
                caloriesPerKm = 30;
                break;
            default:
                caloriesPerKm = 50;
        }

        double distanceInKm = calculateDistanceInKm();

        double baseCalories = caloriesPerKm * distanceInKm;
        double intensityFactor = 1.0;
        if (distanceInKm > 0) {
            double speed = distanceInKm / (calculateTimeDifferenceInSeconds() / 3600.0);
            intensityFactor = speed / 6.0; // 6 km/h 作为基准
            intensityFactor = Math.max(0.5, Math.min(intensityFactor, 2.0));
        }

        return (long) (baseCalories * intensityFactor);
    }
}
