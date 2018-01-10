package com.rpatel.weighttracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Bundle extras = getIntent().getExtras();
        Bundle args = extras.getBundle("args");
        List<String> lines = args.getStringArrayList("lines");
        TextView resultsTextView = findViewById(R.id.resultsTextView);
        StringBuilder fileContent = new StringBuilder();
        for(String line : lines){
            fileContent.append(line);
            fileContent.append("\n");
        }
        resultsTextView.setText(fileContent.toString());
    }
}
