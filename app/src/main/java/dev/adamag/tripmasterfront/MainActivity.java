package dev.adamag.tripmasterfront;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import dev.adamag.tripmasterfront.Activity.AuthActivity.LoginActivity;
import dev.adamag.tripmasterfront.Activity.AuthActivity.SignupActivity;

public class MainActivity extends AppCompatActivity {

    private MaterialButton getStartedButton;
    private TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        getStartedButton = findViewById(R.id.main_get_started_BTN);
        loginButton = findViewById(R.id.main_login_BTN);
//        ChatGptApi.chatGPT("Hello, how are you?", new ChatGptCallback() {
//            @Override
//            public void onSuccess(String response) {
//                Log.d("ChatGPT Response", response);
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.d("ChatGPT Response", error);
//            }
//        });

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SignupActivity
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
