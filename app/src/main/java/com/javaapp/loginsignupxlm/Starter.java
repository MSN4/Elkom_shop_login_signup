package com.javaapp.loginsignupxlm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class Starter extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String FIRST_RUN_KEY = "firstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean(FIRST_RUN_KEY, true);

        if (isFirstRun) {
            // Show the starter activity
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_starter);

            Button starter = findViewById(R.id.starterBtn);
            starter.setOnClickListener(view -> {
                // Set the flag to false indicating the app has been started for the first time
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(FIRST_RUN_KEY, false);
                editor.apply();

                // Start the UserLogin activity
                Intent intent = new Intent(Starter.this, UserLogin.class);
                startActivity(intent);
                finish();
            });
        } else {
            // Bypass the starter activity and go directly to UserLogin activity
            Intent intent = new Intent(Starter.this, UserLogin.class);
            startActivity(intent);
            finish();
        }
    }
}
