package top.kncweb.sposocapp.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import top.kncweb.sposocapp.local.entity.ActivityRecord;

@Dao
public interface ActivityRecordDao {
    @Insert
    void insert(ActivityRecord record);

    @Query("SELECT * FROM activity_record ORDER BY id DESC")
    List<ActivityRecord> getAllRecords();

    @Query("SELECT * FROM activity_record WHERE id = :recordId")
    ActivityRecord getRecordById(long recordId);

    @Query("UPDATE activity_record SET onCloud = :onCloud WHERE id = :recordId")
    void updateOnCloudStatus(long recordId, boolean onCloud);
}
