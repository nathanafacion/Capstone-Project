package com.example.android.remedyme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class RemedyCrudActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remedy_crud);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //****Spinners pro "Time of first dose"
        //Horas
        Integer[] items = new Integer[12];
        for (int i=0; i<12;i++) {
            items[i]=i;
        }
        Spinner startHourSpinner = findViewById(R.id.cb_first_dose_hour);
        ArrayAdapter<Integer> startHourAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
        startHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startHourSpinner.setAdapter(startHourAdapter);

        //Minutos
        items = new Integer[60];
        for (int i=0; i<60;i++) {
            items[i]=i;
        }
        Spinner startMinuteSpinner = findViewById(R.id.cb_first_dose_min);
        ArrayAdapter<Integer> startMinutesAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
        startMinutesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startMinuteSpinner.setAdapter(startMinutesAdapter);
        //****

        //****Spinners pro "Times"
        //Times per Day - Time Interval
        Spinner spinnerTimes = findViewById(R.id.cb_option_time);
        ArrayAdapter<CharSequence> timesAdapter = ArrayAdapter.createFromResource(this,
                R.array.times_array, android.R.layout.simple_spinner_item);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimes.setAdapter(timesAdapter);
        //Quantidade
        items = new Integer[12];
        for (int i=0; i<12;i++) {
            items[i]=i;
        }
        Spinner timesAmountSpinner = findViewById(R.id.cb_option_time_amount);
        ArrayAdapter<Integer> timesAmountAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
        timesAmountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timesAmountSpinner.setAdapter(timesAmountAdapter);
        //*****

        //****Spinners pro "Type of dose"
        //Pills-ML
        Spinner spinnerTypeOfDose = findViewById(R.id.cb_option_dose_type);
        ArrayAdapter<CharSequence> typeOfDoseAdapter = ArrayAdapter.createFromResource(this,
                R.array.typeOfDose_array, android.R.layout.simple_spinner_item);
        typeOfDoseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeOfDose.setAdapter(typeOfDoseAdapter);
        //Quantidade
        items = new Integer[12];
        for (int i=0; i<12;i++) {
            items[i]=i;
        }
        Spinner spinnerTypeOfDoseType = findViewById(R.id.cb_option_dose_type_amount);
        ArrayAdapter<Integer> typeOfDoseTypeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
        typeOfDoseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeOfDoseType.setAdapter(typeOfDoseTypeAdapter);
        //*****

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
