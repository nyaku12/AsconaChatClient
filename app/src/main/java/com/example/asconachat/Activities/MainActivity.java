package com.example.asconachat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asconachat.R;
import com.example.asconachat.WebSocket.WebSocketClient;
import com.example.asconachat.WebSocket.WebSocketService;

public class MainActivity extends AppCompatActivity {
    WebSocketClient webSocketClient = WebSocketClient.getInstance();
    private boolean logged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button signInButton = findViewById(R.id.sign_in_button);
        webSocketClient.setCallback(new WebSocketClient.WebSocketCallback() {
            @Override
            public void onMessageReceived(String message) {
                if(message.equals("succes")){
                    Log.d("WebSocket", "Succes first");
                    runOnUiThread(() -> {
                        Log.d("WebSocket", "Succes second");
                    Intent intent = new Intent (MainActivity.this, ChatlistActivity.class);
                    startActivity(intent);
                    finish();
                });
                }
            }

            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(String reason) {

            }

            @Override
            public void onError(String error) {
                Log.e("Websocket", error);
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ETlogin = findViewById(R.id.login_edittext);
                String login = ETlogin.getText().toString();
                EditText ETpassword = findViewById(R.id.password_edittext);
                String password = ETpassword.getText().toString();
                EditText ETserver = findViewById(R.id.server_edittext);
                String server = ETserver.getText().toString();
                if(server.equals("")) server = "ws://10.0.2.2:8080/ws";
                webSocketClient.connect(login, password, server);
            }
        });

    }
}