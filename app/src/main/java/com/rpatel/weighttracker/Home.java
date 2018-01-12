package com.rpatel.weighttracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String fileName = "results.csv";
    private static TextView dateTimeTextView = null;
    private static final int FILE_SELECT_CODE = 0;
    static Date date = Calendar.getInstance().getTime();

    private void setDateTimeTextView(View view){
        dateTimeTextView = (TextView) view;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setDateTimeTextView(findViewById(R.id.dateTimeTextView));
        setTimeText(date);

        final EditText weight = findViewById(R.id.weightInput);
        final EditText fat = findViewById(R.id.fatInput);
        final EditText water = findViewById(R.id.waterInput);
        final EditText muscle = findViewById(R.id.massInput);
        final EditText bone = findViewById(R.id.boneInput);

        weight.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                weight.getBackground().clearColorFilter();
                return false;
            }
        });


        // Saving fields
        FloatingActionButton save_fab = findViewById(R.id.save_fab);
        save_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click Save!");
                if(validateTextFields(Arrays.asList(weight))) {
                    writeFile(fileName);
                    for(String line : readFile(fileName)){
                        System.out.println("line: " + line);
                    }
                    weight.setText("");
                    fat.setText("");
                    water.setText("");
                    muscle.setText("");
                    bone.setText("");
                    weight.requestFocus();
                }
                else{
                    displayToast("Please enter weight.");
                }

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                closeKeyboard(Home.this);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(Home.this);
    }

    private void closeKeyboard(Activity activity){
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void setTimeText(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        setTimeText(cal.getTime());
    }

    public static void setTimeText(int hour, int min){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        setTimeText(cal.getTime());

    }

    public static void setTimeText(Date givenDate){
        if (dateTimeTextView != null) {
            date = givenDate;
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy @ hh:mma");
            dateTimeTextView.setText(sdf.format(date));
        }
    }
    public boolean validateTextFields(List<EditText> fieldList){
        boolean returnVal = true;
        for(EditText field : fieldList){
            String value = field.getText().toString();
            if(value.equals("")){
                field.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.ADD);
                returnVal = false;
            }
        }
        return returnVal;
    }

    public void displayToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void writeFile(String fileName){
        FileOutputStream saveFile = null;
        EditText weight = findViewById(R.id.weightInput);
        EditText fat = findViewById(R.id.fatInput);
        EditText water = findViewById(R.id.waterInput);
        EditText muscle = findViewById(R.id.massInput);
        EditText bone = findViewById(R.id.boneInput);

        try {
            saveFile = openFileOutput(fileName, MODE_APPEND);

            saveFile.write(String.format("%s, %s, %s, %s, %s, %s;",
                    new SimpleDateFormat("mm/dd/yyyy - hh:mma").format(date),
                    weight.getText().toString().equals("") ? "0" : weight.getText().toString(),
                    fat.getText().toString().equals("") ? "0" : fat.getText().toString(),
                    water.getText().toString().equals("") ? "0" : water.getText().toString(),
                    muscle.getText().toString().equals("") ? "0" : muscle.getText().toString(),
                    bone.getText().toString().equals("") ? "0" : bone.getText().toString()
            ).getBytes(Charset.forName("UTF-8")));
        }
        catch (FileNotFoundException e) {
            displayToast(String.format("Given file %s is not present", fileName));
            e.printStackTrace();
        }
        catch (IOException e) {
            displayToast(String.format("Something went wrong with writing the file: %s", fileName));
            e.printStackTrace();
        }
        displayToast("Saved!");
    }

    public List<String> readFile(String fileName){
        List<String> fileLines = new ArrayList<>();
        FileInputStream fileToRead = null;

        try {
            fileToRead = openFileInput(fileName);

            int content;
            StringBuilder fileContent = new StringBuilder();

            while ((content = fileToRead.read()) != -1) {
                // convert to char and display it
                fileContent.append((char) content);
            }

            fileLines = Arrays.asList(fileContent.toString().split(";"));

        }
        catch (FileNotFoundException e) {
            displayToast(String.format("Given file %s is not present", fileName));
            e.printStackTrace();
        }
        catch (IOException e) {
            displayToast(String.format("Something went wrong with reading the file: %s", fileName));
            e.printStackTrace();
        }
        finally {
            try {
                if (fileToRead != null)
                    fileToRead.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return fileLines;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        closeKeyboard(Home.this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Settings Selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_delete) {
            new AlertDialog.Builder(Home.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete File")
                    .setMessage("Are you sure you want to delete the results file?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("Click Delete!");
                            deleteFile(fileName);
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.out.println("No Delete!");
                            Toast.makeText(getApplicationContext(), "No Delete file", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_results) {
            Intent resultsIntent = new Intent(Home.this, ResultsActivity.class);
            Bundle args = new Bundle();
            args.putStringArrayList("lines", new ArrayList(readFile(fileName)));
            resultsIntent.putExtra("args", args);
            startActivity(resultsIntent);
        }

        if (id == R.id.nav_export) {


            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
            intent.putExtra("android.content.extra.FANCY", true);
            intent.putExtra("android.content.extra.SHOW_FILESIZE", true);
//            intent.putExtra(Intent.EXTRA_TITLE, "test.csv");
//            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_TITLE, "test.txt");
            intent.setType("text/plain");


            try {
                startActivityForResult(intent, FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException ex) {
                // Potentially direct the user to the Market with a Dialog
                displayToast("Please install a File Manager.");
            }


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                displayToast("Result_ok");
                System.out.println(data.getDataString());
                System.out.println("YES!");
                exportResultsFile(data.getData());
                // Do something with the contact here (bigger example below)
            }
            else {
                displayToast("Please select a location!");
            }
        }
    }

    public void exportResultsFile(Uri uri){
        ContentResolver cr = getApplicationContext().getContentResolver();
        try {
            OutputStream export_file = cr.openOutputStream(uri);
            for(String line : readFile(fileName)) {
                line += "\n";
                export_file.write(line.getBytes());

            }
            export_file.flush();
            export_file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showTimePickerDialog(View view) {
        DialogFragment timeFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("hour", Integer.parseInt(new SimpleDateFormat("HH").format(date)));
        args.putInt("minute", Integer.parseInt(new SimpleDateFormat("mm").format(date)));
        timeFragment.setArguments(args);
        timeFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view) {
        DialogFragment dateFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("year", Integer.parseInt(new SimpleDateFormat("yyyy").format(date)));
        args.putInt("month", Integer.parseInt(new SimpleDateFormat("M").format(date)));
        args.putInt("day", Integer.parseInt(new SimpleDateFormat("d").format(date)));
        dateFragment.setArguments(args);
        dateFragment.show(getFragmentManager(), "datePicker");
    }
}
