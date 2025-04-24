package top.kncweb.sposocapp.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import top.kncweb.sposocapp.local.entity.Message;

@Dao
public interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Message message);

    @Query("SELECT * FROM message WHERE mid IN (" +
            "SELECT MAX(mid) FROM message WHERE senderUid = :userId OR receiverUid = :userId " +
            "GROUP BY CASE WHEN senderUid = :userId THEN receiverUid ELSE senderUid END)")
    List<Message> getLastMessagesPerUser(Long userId);

    @Query("SELECT * FROM message WHERE senderUid = :userId OR receiverUid = :userId")
    List<Message> getMessagesByUserId(Long userId);
}
