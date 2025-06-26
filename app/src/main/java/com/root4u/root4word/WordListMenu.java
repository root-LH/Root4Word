package com.root4u.root4word;

import androidx.activity.OnBackPressedCallback;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WordListMenu extends AppCompatActivity {

    private final String TAG = "WordListMenu";
    private static final String WORDLIST_FILE = "wordList.csv";

    private void createFileNotExist(){
        File file = new File(getFilesDir(), WORDLIST_FILE);
        if (!file.exists()){
            try (FileOutputStream fos = openFileOutput(WORDLIST_FILE, MODE_PRIVATE)){
                String header = "listName,count\n";
                fos.write(header.getBytes());
            } catch (IOException e){
                Log.e(TAG, "WordList 파일 초기화 실패", e);
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
            Log.e(TAG, "wordList 읽기 실패", e);
        }
        return wordList;
    }

    // WordList, adapter 선언
    List<WordListItem> wordList;
    WordListAdapter adapter;
    
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
        wordList = loadWordListFromCsv();
        adapter = new WordListAdapter(wordList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton addWordListBtn = findViewById(R.id.addWordList);
        addWordListBtn.setOnClickListener(view -> showAddWordListDialog());
    }

    private void showAddWordListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("단어장 추가");

        final EditText input = new EditText(this);
        input.setHint("단어장 이름");
        builder.setView(input);

        builder.setPositiveButton("추가", ((dialog, which) -> {
            String listName = input.getText().toString().trim();
            if (!listName.isEmpty()){
                addNewWordList(listName);
            }
        }));

        builder.setNegativeButton("취소", ((dialog, which) -> dialog.cancel()));

        builder.show();
    }

    private void addNewWordList(String listName) {
        File file = new File(getFilesDir(), WORDLIST_FILE);
        
        // 중복 체크를 위해 기존 단어장 목록을 불러옴
        List<String[]> existingLists = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))){
            String[] line;
            while ((line = reader.readNext()) != null){
                existingLists.add(line);
            }
        } catch (IOException e){
            Log.e(TAG, "단어장 읽기 오류", e);
        } catch (CsvValidationException e) {
            Log.e(TAG, "CSV 판정 오류", e);
        }
        
        // 헤더 제외하고 중복 검사
        for (int i=1;i<existingLists.size();i++){
            if (existingLists.get(i)[0].equals(listName)){
                Toast.makeText(this,"이미 존재하는 단어장입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 중복 검사 통과시 새 항목 추가
        try (CSVWriter writer = new CSVWriter(new FileWriter(file, true))){
            String[] newEntry = {listName, "0"};
            writer.writeNext(newEntry);
        } catch (IOException e){
            Log.e(TAG, "새 항목 생성 에러", e);
        }
        
        // 파일 생성
        File newWordFile = new File(getFilesDir(), listName + ".csv");
        if (!newWordFile.exists()){
            try (FileOutputStream fos = openFileOutput(listName + ".csv", MODE_PRIVATE)){
                String header = "word,meaning\n";
                fos.write(header.getBytes());
            } catch (IOException e){
                Log.e(TAG, "파일 생성 에러", e);
            }
        }

        wordList.add(new WordListItem(listName, 0));
        adapter.notifyItemInserted(wordList.size()-1);

    }


}