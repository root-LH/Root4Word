package com.root4u.root4word;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home){
                return true;
            }
            else if (id == R.id.nav_word_list) {
                startActivity(new Intent(MainActivity.this, WordListMenuActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_account){
                    return true;
            }
            else return false;
        });
    }


}
