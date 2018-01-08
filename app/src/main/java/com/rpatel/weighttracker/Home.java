package com.rpatel.weighttracker;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String fileName = "results.csv";
    static private TextView dateTimeTextView = null;
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

        final EditText weightInout = findViewById(R.id.weightInput);

        weightInout.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                weightInout.getBackground().clearColorFilter();
                return false;
            }
        });


        // Saving fields
        FloatingActionButton save_fab = findViewById(R.id.save_fab);
        save_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click Save!");
                if(validateTextFields(Arrays.asList(weightInout))) {
                    writeFile(fileName);
                    for(String line : readFile(fileName)){
                        System.out.println("line: " + line);
                    }
                }
                else{
                    displayToast("Please enter weight.");
                }

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        Date date = Calendar.getInstance().getTime();
        EditText weight = findViewById(R.id.weightInput);
        EditText fat = findViewById(R.id.fatInput);
        EditText water = findViewById(R.id.waterInput);
        EditText muscle = findViewById(R.id.massInput);
        EditText bone = findViewById(R.id.boneInput);


        displayToast(weight.getText().toString());
        try {
            saveFile = openFileOutput(fileName, MODE_APPEND);

            saveFile.write(String.format("Test - %s, %s, %s, %s, %s, %s;",
                    date,
                    weight.getText().toString(),
                    fat.getText().toString(),
                    water.getText().toString(),
                    muscle.getText().toString(),
                    bone.getText().toString()).getBytes(Charset.forName("UTF-8")));
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
        List<String> fileLines = null;
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(getApplicationContext(), "nav_camera", Toast.LENGTH_SHORT).show();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(), "nav_gallery", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(getApplicationContext(), "nav_slideshow", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {
            Toast.makeText(getApplicationContext(), "nav_manage", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), "nav_share", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(getApplicationContext(), "nav_send", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showTimePickerDialog(View view) {
        DialogFragment timeFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("hour", Integer.parseInt(new SimpleDateFormat("HH").format(date)));
        args.putInt("minute", Integer.parseInt(new SimpleDateFormat("mm").format(date)));
        timeFragment.setArguments(args);
        timeFragment.show(getFragmentManager(), "timePicker");
//        args.putInt("hour", Integer.parseInt(new SimpleDateFormat("EEE, d MMM yyyy @ hh:mma").format(date)));
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
