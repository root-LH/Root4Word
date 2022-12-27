package com.root4u.root4word;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button moveToWordMenu = findViewById(R.id.mainButton);
        moveToWordMenu.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, WordListMenu.class);
                startActivity(intent);
        });
    }
}