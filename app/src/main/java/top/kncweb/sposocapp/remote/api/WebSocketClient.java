package top.kncweb.sposocapp.remote.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.local.AppDatabase;
import top.kncweb.sposocapp.local.entity.Message;

public class WebSocketClient {
    private static WebSocketClient instance;
    private WebSocket webSocket;
    private OkHttpClient client;
    private Listener listener;
    private final String baseUrl = "ws://172.18.0.101:8080/ws/chat";
    private boolean isConnected = false;
    private String jwtToken;

    public interface Listener {
        void onMessageReceived(Message message);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static WebSocketClient getInstance() {
        if (instance == null) {
            instance = new WebSocketClient();
        }
        return instance;
    }

    public void connect(String jwtToken) {
        if (jwtToken.equals(this.jwtToken) && webSocket != null && isConnected) return;

        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(baseUrl + "?token=" + jwtToken)
                .build();

        this.jwtToken = jwtToken;

        if (webSocket != null) {
            webSocket.close(1000, "重连中");
            webSocket = null;
        }

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                Log.d("WebSocket", "连接成功");
                isConnected = true;
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d("WebSocket", "收到消息: " + text);
                Message message = null;
                try{
                    message = new Gson().fromJson(text, Message.class);
                }
                catch (Exception e) {
                    Log.e("WebSocket", "解析消息失败", e);
                }
                if (message == null) {
                    Log.e("WebSocket", "消息为空");
                    return;
                }
                AppDatabase.getInstance(SposocApplication.getContext())
                        .messageDao()
                        .insert(message);

                if (listener != null) {
                    listener.onMessageReceived(message);
                }

            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
                if (t instanceof java.io.EOFException) {
                    Log.e("WebSocket", "连接意外关闭 (EOFException)", t);
                } else {
                    Log.e("WebSocket", "连接失败", t);
                }
                isConnected = false;
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    connect(jwtToken);
                }, 5000); // 5秒后重连
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d("WebSocket", "连接关闭: " + reason);
                isConnected = false;
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    connect(jwtToken);
                }, 5000);
            }
        });
    }

    public boolean sendMessage(String json) {
        if (webSocket != null && isConnected) {
            return webSocket.send(json);
        }
        else {
            Log.e("WebSocket", "WebSocket未连接或已关闭");
            return false;
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "主动关闭");
            webSocket = null;
        }
    }
}
