package com.rpatel.weighttracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.View.TEXT_ALIGNMENT_CENTER;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Bundle extras = getIntent().getExtras();
        Bundle args = extras.getBundle("args");
        List<String> lines = args.getStringArrayList("lines");

        TableLayout resultsTable = findViewById(R.id.Results_TableLayout);
        TableRow headerRow = findViewById(R.id.Results_Header);
        TextView headerTextView = findViewById(R.id.Results_DateHeaderTextView);

        if (lines.size() > 0) {
            int counter = 0;
            for (String line : lines) {
                counter++;

                System.out.println("Line: " + line);
                String[] lineContent = line.split(",");
                String date = lineContent[0].trim();
                String weight = lineContent[1].trim();
                String fat = lineContent[2].trim();
                String water = lineContent[3].trim();
                String muscle = lineContent[4].trim();
                String bone = lineContent[5].trim();

                TableRow resultRow = new TableRow(ResultsActivity.this);
                resultRow.setId(counter);
                resultRow.setLayoutParams(headerRow.getLayoutParams());

                TextView dateView = new TextView(ResultsActivity.this);
                dateView.setLayoutParams(headerTextView.getLayoutParams());
                dateView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                dateView.setText(date);
                resultRow.addView(dateView);

                TextView weightView = new TextView(ResultsActivity.this);
                weightView.setLayoutParams(headerTextView.getLayoutParams());
                weightView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                weightView.setText(weight);
                resultRow.addView(weightView);

                TextView fatView = new TextView(ResultsActivity.this);
                fatView.setLayoutParams(headerTextView.getLayoutParams());
                fatView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                fatView.setText(fat);
                resultRow.addView(fatView);

                TextView waterView = new TextView(ResultsActivity.this);
                waterView.setLayoutParams(headerTextView.getLayoutParams());
                waterView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                waterView.setText(water);
                resultRow.addView(waterView);

                TextView muscleView = new TextView(ResultsActivity.this);
                muscleView.setLayoutParams(headerTextView.getLayoutParams());
                muscleView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                muscleView.setText(muscle);
                resultRow.addView(muscleView);

                TextView boneView = new TextView(ResultsActivity.this);
                boneView.setLayoutParams(headerTextView.getLayoutParams());
                boneView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                boneView.setText(bone);
                resultRow.addView(boneView);



                resultsTable.addView(resultRow);
            }


        }
        else {
            resultsTable.removeAllViews();
            TableRow result1Row = new TableRow(ResultsActivity.this);
            result1Row.setLayoutParams(headerRow.getLayoutParams());

            TextView view = new TextView(ResultsActivity.this);
            view.setLayoutParams(headerTextView.getLayoutParams());
            view.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            view.setText("No Results Found!");

            result1Row.addView(view);
            resultsTable.addView(result1Row);
        }

    }
}
