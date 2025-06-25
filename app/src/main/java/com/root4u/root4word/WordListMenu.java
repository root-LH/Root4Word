package com.root4u.root4word;

import androidx.activity.OnBackPressedCallback;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WordListMenu extends AppCompatActivity {

    private static final String WORDLIST_FILE = "wordList.csv";

    private void createFileNotExist(){
        File file = new File(getFilesDir(), WORDLIST_FILE);
        if (!file.exists()){
            try (FileOutputStream fos = openFileOutput(WORDLIST_FILE, MODE_PRIVATE)){
                String header = "listName,count\n";
                String test = "엄준식,1\n";
                fos.write(header.getBytes());
                fos.write(test.getBytes());
            } catch (IOException e){
                Log.e("WordListMenu", "WordList 파일 초기화 실패", e);
            }
        }
    }

    public class WordListItem {
        private String name;
        private int count;

        public WordListItem(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public String getName() { return name; }
        public int getCount() { return count; }
    }

    private List<WordListItem> loadWordListFromCsv(){
        List<WordListItem> wordList = new ArrayList<>();
        File file = new File(getFilesDir(), WORDLIST_FILE);
        if (!file.exists()) return wordList;

        try (CSVReader reader = new CSVReader(new FileReader(file))){
            String[] line;
            reader.readNext(); // Header 부분 건너뜀
            while ((line = reader.readNext()) != null){
                String name = line[0];
                int count = Integer.parseInt(line[1]);
                wordList.add(new WordListItem(name, count));
            }
        } catch (Exception e){
            Log.e("WordListMenu", "wordList 읽기 실패", e);
        }
        return wordList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list_menu);

        createFileNotExist();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 뒤로가기 버튼 목적 변경
                Intent intent = new Intent(WordListMenu.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home){
                    Intent intent = new Intent(WordListMenu.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }
                else if (id == R.id.nav_word_list) {
                    return true;
                }
                else if (id == R.id.nav_account){
                    return true;
                }
                else return false;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.wordListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<WordListItem> wordList = loadWordListFromCsv();
        WordListAdapter adapter = new WordListAdapter(wordList);
        recyclerView.setAdapter(adapter);
    }


}