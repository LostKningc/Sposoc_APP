package top.kncweb.sposocapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.local.AppDatabase;
import top.kncweb.sposocapp.local.dao.MessageDao;
import top.kncweb.sposocapp.local.entity.Message;
import top.kncweb.sposocapp.remote.api.WebSocketClient;

public class ChatViewModel extends ViewModel {
    private final MutableLiveData<List<Message>> messages = new MutableLiveData<>(new ArrayList<>());
    private final Executor executor = Executors.newSingleThreadExecutor();

    private long uid;

    private final MessageDao messageDao;
    private final WebSocketClient webSocketClient;

    public ChatViewModel() {
        super();
        messageDao = AppDatabase.getInstance(SposocApplication.getContext()).messageDao();
        webSocketClient = WebSocketClient.getInstance();

        loadHistory();

        webSocketClient.setListener(message -> {
            if (message.getSenderUid() == uid || message.getReceiverUid() == uid) {
                addMessage(message);
            }
        });
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public void addMessage(Message msg) {
        List<Message> current = messages.getValue();
        current.add(msg);
        messages.postValue(current);
    }

    public void setAllMessages(List<Message> msgList) {
        messages.postValue(msgList);
    }

    public void sendMessage(Message msg) {
        webSocketClient.sendMessage(new Gson().toJson(msg));
    }

    private void loadHistory() {
        executor.execute(() -> {
            List<Message> all = messageDao.getMessagesByUserId(uid);
            messages.postValue(all);
        });
    }
}
