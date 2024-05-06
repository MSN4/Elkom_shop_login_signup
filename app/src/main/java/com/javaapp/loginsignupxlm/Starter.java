package com.javaapp.loginsignupxlm;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
public class Starter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_starter);

        Button starter=(Button)findViewById(R.id.starterBtn);
        starter.setOnClickListener(view -> {

            Intent intent= new Intent(Starter.this , UserLogin.class);
            startActivity(intent);
        });

    }
}