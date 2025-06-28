package com.root4u.root4word;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class WordListDetailActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list_detail);

        fileName = getIntent().getStringExtra("fileName");
        // RecyclerView 등 초기화
    }
}
