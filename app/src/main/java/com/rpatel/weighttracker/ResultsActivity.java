package com.rpatel.weighttracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Bundle extras = getIntent().getExtras();
        Bundle args = extras.getBundle("args");
        List<String> lines = args.getStringArrayList("lines");
        String id = "Results_%s_%s";

        TableLayout resultsTable = findViewById(R.id.Results_TableLayout);
        LayoutInflater inflator = ResultsActivity.this.getLayoutInflater();

        // Generate Header Row
        inflator.inflate(R.layout.results_row_layout, resultsTable, true);
        TableRow headerRow = findViewById(R.id.Results_row);
        headerRow.setId(String.format(id, "header", "row").hashCode());
        headerRow.setBackgroundColor(getResources().getColor(R.color.black_alpha_12));

        TextView headerDateView = findViewById(R.id.Results_row_DateTextView);
        headerDateView.setId(String.format(id, "header", "DateTextView").hashCode());

        TextView headerTimeView = findViewById(R.id.Results_row_TimeTextView);
        headerTimeView.setId(String.format(id, "header", "TimeTextView").hashCode());

        TextView headerWeightView = findViewById(R.id.Results_row_WeightTextView);
        headerWeightView.setId(String.format(id, "header", "WeightTextView").hashCode());

        TextView headerFatView = findViewById(R.id.Results_row_FatTextVIew);
        headerFatView.setId(String.format(id, "header", "FatTextView").hashCode());

        TextView headerWaterView = findViewById(R.id.Results_row_BodyWaterTextView);
        headerWaterView.setId(String.format(id, "header", "BodyWaterTextView").hashCode());

        TextView headerMuscleView = findViewById(R.id.Results_row_BodyMusclesTextView);
        headerMuscleView.setId(String.format(id, "header", "BodyMusclesTextView").hashCode());

        TextView headerBoneView = findViewById(R.id.Results_row_BoneMassTextView);
        headerBoneView.setId(String.format(id, "header", "BoneMassTextView").hashCode());


        if (lines.size() > 0) {
            int counter = 0;
            for (String line : lines) {
                counter ++;
                System.out.println("Line: " + line);
                String[] lineContent = line.split(",");
                String[] dateTime = lineContent[0].trim().split("-");
                String date = dateTime[0].trim();
                String time = dateTime[1].trim();
                String weight = lineContent[1].trim();
                String fat = lineContent[2].trim();
                String water = lineContent[3].trim();
                String muscle = lineContent[4].trim();
                String bone = lineContent[5].trim();

                // Generate Result Row
                inflator.inflate(R.layout.results_row_layout, resultsTable, true);

                TableRow resultRow = findViewById(R.id.Results_row);
                resultRow.setId(String.format(id, counter, "row").hashCode());

                TextView dateView = findViewById(R.id.Results_row_DateTextView);
                dateView.setId(String.format(id, counter, "DateTextView").hashCode());
                dateView.setText(date);

                TextView timeView = findViewById(R.id.Results_row_TimeTextView);
                timeView.setId(String.format(id, counter, "TimeTextView").hashCode());
                timeView.setText(time);

                TextView weightView = findViewById(R.id.Results_row_WeightTextView);
                weightView.setId(String.format(id, counter, "WeightTextView").hashCode());
                weightView.setText(weight);

                TextView fatView = findViewById(R.id.Results_row_FatTextVIew);
                fatView.setId(String.format(id, counter, "FatTextView").hashCode());
                fatView.setText(fat);

                TextView waterView = findViewById(R.id.Results_row_BodyWaterTextView);
                waterView.setId(String.format(id, counter, "BodyWaterTextView").hashCode());
                waterView.setText(water);

                TextView muscleView = findViewById(R.id.Results_row_BodyMusclesTextView);
                muscleView.setId(String.format(id, counter, "BodyMusclesTextView").hashCode());
                muscleView.setText(muscle);

                TextView boneView = findViewById(R.id.Results_row_BoneMassTextView);
                boneView.setId(String.format(id, counter, "BoneMassTextView").hashCode());
                boneView.setText(bone);
            }
        }
        else {
            resultsTable.removeAllViews();
            TableRow errorRow = new TableRow(ResultsActivity.this);

            TextView view = new TextView(ResultsActivity.this);
            view.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            view.setText("No Results Found!");

            errorRow.addView(view);
            resultsTable.addView(errorRow);
        }

    }
}
