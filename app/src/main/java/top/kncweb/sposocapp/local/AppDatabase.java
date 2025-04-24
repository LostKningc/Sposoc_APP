package top.kncweb.sposocapp.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import top.kncweb.sposocapp.local.dao.ActivityRecordDao;
import top.kncweb.sposocapp.local.dao.MessageDao;
import top.kncweb.sposocapp.local.entity.ActivityRecord;
import top.kncweb.sposocapp.local.entity.Message;

@Database(entities = {ActivityRecord.class, Message.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract ActivityRecordDao activityRecordDao();
    public abstract MessageDao messageDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "sports_app_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}