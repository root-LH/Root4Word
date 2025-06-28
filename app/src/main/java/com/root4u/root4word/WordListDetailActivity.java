package com.root4u.root4word;

import static com.root4u.root4word.WordListMenu.WORDLIST_FILE;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordListDetailActivity extends AppCompatActivity {
    private final String TAG = "WordListDetail";
    private String fileName;

    public class WordItem {
        private String word;
        private String meaning;

        public WordItem(String word, String meaning) {
            this.word = word;
            this.meaning = meaning;
        }

        public String getWord() { return word; }
        public String getMeaning() { return meaning; }
    }

    private List<WordItem> loadWordFromCsv(){
        List<WordItem> wordList = new ArrayList<>();
        File file = new File(getFilesDir(), fileName);
        if (!file.exists()) return wordList;

        try (CSVReader reader = new CSVReader(new FileReader(file))){
            String[] line;
            reader.readNext(); // Header 부분 건너뜀
            while ((line = reader.readNext()) != null){
                String word = line[0];
                String meaning = line[1];
                wordList.add(new WordItem(word, meaning));
            }
        } catch (Exception e){
            Log.e(TAG, "wordList 읽기 실패", e);
        }
        return wordList;
    }

    // WordList, adapter 선언
    List<WordItem> wordList;
    WordAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list_detail);

        fileName = getIntent().getStringExtra("fileName");

        // 단어 리스트 불러오기
        wordList = loadWordFromCsv();
        adapter = new WordAdapter(wordList);

        // RecyclerView 초기화
        RecyclerView recyclerView = findViewById(R.id.wordDetailRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 단어장 추가 버튼 기능 할당
        FloatingActionButton addWordListBtn = findViewById(R.id.addWordButton);
        addWordListBtn.setOnClickListener(view -> showAddWordDialog());
    }

    private void showAddWordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("단어 추가");

        // 수직으로 입력창 2개 배치
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10); // 여백

        final EditText word = new EditText(this);
        word.setHint("단어");
        layout.addView(word);

        final EditText meaning = new EditText(this);
        meaning.setHint("의미");
        layout.addView(meaning);

        builder.setView(layout);

        builder.setPositiveButton("추가", ((dialog, which) -> {
            String word_text = word.getText().toString().trim();
            String meaning_text = meaning.getText().toString().trim();
            if (!word_text.isEmpty() && !meaning_text.isEmpty()){
                addNewWord(word_text, meaning_text);
            }
        }));

        builder.setNegativeButton("취소", ((dialog, which) -> dialog.cancel()));

        builder.show();
    }

    private void addNewWord(String word, String meaning) {
        File file = new File(getFilesDir(), fileName);

        // 중복 체크를 위해 기존 단어장을 불러옴
        List<String[]> existingWords = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                existingWords.add(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "단어장 읽기 오류", e);
        } catch (CsvValidationException e) {
            Log.e(TAG, "CSV 판정 오류", e);
        }

        // 헤더 제외하고 중복 검사
        for (int i = 1; i < existingWords.size(); i++) {
            if (existingWords.get(i)[0].equals(word)) {
                Toast.makeText(this, "이미 존재하는 단어입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // 중복 검사 통과시 새 항목 추가
        try (CSVWriter writer = new CSVWriter(new FileWriter(file, true))) {
            String[] newEntry = {word, meaning};
            writer.writeNext(newEntry);
        } catch (IOException e) {
            Log.e(TAG, "새 항목 생성 에러", e);
        }

        wordList.add(new WordItem(word, meaning));
        adapter.notifyItemInserted(wordList.size() - 1);
        int dotIndex = fileName.lastIndexOf(".csv");
        updateWordCount(fileName.substring(0, dotIndex), wordList.size());
    }

    private void updateWordCount(String targetWordList, int value) {
        File file = new File(getFilesDir(), "wordList.csv");

        List<String[]> allRows = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                // 목적 리스트라면 단어 수 갱신
                if (line[0].equals(targetWordList)) {
                    int original = Integer.parseInt(line[1]);
                    line[1] = String.valueOf(value); // 수정
                }

                allRows.add(line);
            }
        } catch (Exception e) {
            Log.e("CSV", "파일 읽기 오류", e);
            return;
        }

        // 수정된 내용 저장
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            writer.writeAll(allRows);
        } catch (IOException e) {
            Log.e("CSV", "파일 쓰기 오류", e);
        }
    }
}
