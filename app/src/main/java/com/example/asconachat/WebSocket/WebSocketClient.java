package com.example.asconachat.WebSocket;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketClient extends WebSocketListener {
    private static WebSocketClient instance;
    private OkHttpClient client;
    WebSocket webSocket;
    public WebSocketClient() {
        client = new OkHttpClient();
    }
    private WebSocketCallback callback;

    public interface WebSocketCallback {
        void onMessageReceived(String message);
        void onConnected();
        void onDisconnected(String reason);
        void onError(String error);
    }
    public void setCallback(WebSocketCallback callback) {
        this.callback = callback;
    }

    public static WebSocketClient getInstance() {
        if (instance == null) {
            instance = new WebSocketClient();
        }
        return instance;
    }

    public void connect(String login, String password, String url){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("login", login)
                .addHeader("password", password)
                .build();
        webSocket = client.newWebSocket(request, this);
    }

    public void send(String mess){
        webSocket.send(mess);
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        Log.d("WebSocket", "Connected!");
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        Log.d("WebSocket", "Message: " + text);
        if (callback != null) {
            callback.onMessageReceived(text);
        }
    }

    @Override
    public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        Log.d("WebSocket", "Closed: " + reason);
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
        Log.e("WebSocket", "Error: " + t.getMessage());
    }
}
