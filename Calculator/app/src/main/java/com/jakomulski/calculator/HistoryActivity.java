package com.jakomulski.calculator;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<String> history = new ArrayList<>();
    ListView historyView;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, history);
        historyView = (ListView)findViewById(R.id.list_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.history_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener((e)->{onBackPressed();});
        findViewById(R.id.button_clear).setOnClickListener((e)->clearHistory());
    }

    private void clearHistory(){
        history.clear();
        historyView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<String> extraHistory = getIntent().getExtras().getStringArrayList(MainActivity.EXTRA_HISTORY);
        history.addAll(extraHistory);
        historyView.setAdapter(adapter);
    }
}
