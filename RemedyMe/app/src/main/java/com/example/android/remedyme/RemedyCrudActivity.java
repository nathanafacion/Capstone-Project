package com.example.android.remedyme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.android.remedyme.utils.RemedyContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RemedyCrudActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    Calendar myCalendar = Calendar.getInstance();
    int selectedViewId;
    long startDate;
    long endDate;
    long firstDoseTime;

    @BindView(R.id.et_l_remedy_name) EditText remedyName;
    @BindView(R.id.et_l_start_date) Button startDateButton;
    @BindView(R.id.et_l_end_date) Button endDateButton;
    @BindView(R.id.cb_first_dose_hour) Button firstDoseButton;
    @BindView(R.id.cb_option_time) Spinner optionTime;
    @BindView(R.id.cb_option_time_amount) EditText optionTimeAmount;
    @BindView(R.id.cb_option_dose_type) Spinner doseType;
    @BindView(R.id.cb_option_dose_type_amount) EditText doseTypeAmount;
    @BindView(R.id.cb_alarm) CheckBox alarmOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remedy_crud);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

//        //****Spinners pro "Time of first dose"
//        //Horas
//        Integer[] items = new Integer[12];
//        for (int i=0; i<12;i++) {
//            items[i]=i;
//        }
//        Spinner startHourSpinner = findViewById(R.id.cb_first_dose_hour);
//        ArrayAdapter<Integer> startHourAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
//        startHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        startHourSpinner.setAdapter(startHourAdapter);
//
//        //Minutos
//        items = new Integer[60];
//        for (int i=0; i<60;i++) {
//            items[i]=i;
//        }
//        Spinner startMinuteSpinner = findViewById(R.id.cb_first_dose_min);
//        ArrayAdapter<Integer> startMinutesAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
//        startMinutesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        startMinuteSpinner.setAdapter(startMinutesAdapter);
//        //****

        //****Spinners pro "Times"
        //Times per Day - Time Interval
        Spinner spinnerTimes = findViewById(R.id.cb_option_time);
        ArrayAdapter<CharSequence> timesAdapter = ArrayAdapter.createFromResource(this,
                R.array.times_array, android.R.layout.simple_spinner_item);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimes.setAdapter(timesAdapter);
        //Quantidade
        Integer[] items = new Integer[12];
        for (int i=0; i<12;i++) {
            items[i]=i;
        }
        //Spinner timesAmountSpinner = findViewById(R.id.cb_option_time_amount);
        //ArrayAdapter<Integer> timesAmountAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
        //timesAmountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //timesAmountSpinner.setAdapter(timesAmountAdapter);
        //*****

        //****Spinners pro "Type of dose"
        //Pills-ML
        Spinner spinnerTypeOfDose = findViewById(R.id.cb_option_dose_type);
        ArrayAdapter<CharSequence> typeOfDoseAdapter = ArrayAdapter.createFromResource(this,
                R.array.typeOfDose_array, android.R.layout.simple_spinner_item);
        typeOfDoseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeOfDose.setAdapter(typeOfDoseAdapter);
        //Quantidade
        //items = new Integer[12];
        //for (int i=0; i<12;i++) {
        //    items[i]=i;
        //}
        //Spinner spinnerTypeOfDoseType = findViewById(R.id.cb_option_dose_type_amount);
        //ArrayAdapter<Integer> typeOfDoseTypeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
        //typeOfDoseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinnerTypeOfDoseType.setAdapter(typeOfDoseTypeAdapter);
        //*****
        myCalendar = Calendar.getInstance();
        selectedViewId = R.id.et_l_start_date;
        updateCalendarLabel();
        selectedViewId = R.id.et_l_end_date;
        updateCalendarLabel();
        selectedViewId = R.id.cb_first_dose_hour;
        updateTimeLabel();
    }

    public void saveNewRemedy(View view) {

        ContentResolver contentResolver = view.getContext().getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put( RemedyContract.RemedyEntry.COLUMN_ALARMON, remedyName.getText().toString());
        cv.put( RemedyContract.RemedyEntry.COLUMN_START_DATE, startDate);
        cv.put( RemedyContract.RemedyEntry.COLUMN_END_DATE, endDate);
        cv.put( RemedyContract.RemedyEntry.COLUMN_TIME_OF_FIRST_DOSE, firstDoseTime);
        cv.put( RemedyContract.RemedyEntry.COLUMN_NEXT_NOTIFICATION, firstDoseTime);
        cv.put( RemedyContract.RemedyEntry.COLUMN_ALARMON, alarmOn.isEnabled());
        cv.put( RemedyContract.RemedyEntry.COLUMN_QUANT_TIMES, Integer.valueOf(optionTimeAmount.getText().toString()));
        cv.put( RemedyContract.RemedyEntry.COLUMN_TYPE_OF_DOSE, String.valueOf(doseType.getSelectedItem()));
        cv.put( RemedyContract.RemedyEntry.COLUMN_QUANT_TYPE_OF_DOSE, doseTypeAmount.getText().toString());
        cv.put( RemedyContract.RemedyEntry.COLUMN_TIMES, String.valueOf(optionTime.getSelectedItem()));
        cv.put( RemedyContract.RemedyEntry.COLUMN_QUANT_TIMES, optionTimeAmount.getText().toString());
        cv.put( RemedyContract.RemedyEntry.COLUMN_REMEDY_NAME, remedyName.getText().toString());

        contentResolver.insert(
                RemedyContract.RemedyEntry.CONTENT_URI, cv);

        Intent intent = new Intent(view.getContext(), MainActivity.class);
        view.getContext().startActivity(intent);
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
        if (id == R.id.action_backup) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openCalendarDialog(View v) {
        selectedViewId = v.getId();
        myCalendar = Calendar.getInstance();
        new DatePickerDialog(RemedyCrudActivity.this, this, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateCalendarLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        ((Button) findViewById(selectedViewId)).setText(sdf.format(myCalendar.getTime()));
        if (selectedViewId == R.id.et_l_start_date) {
            startDate = myCalendar.getTimeInMillis();
        } else {
            endDate = myCalendar.getTimeInMillis();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateCalendarLabel();
    }

    public void openTimePickerDialog(View v) {
        selectedViewId = v.getId();
        myCalendar = Calendar.getInstance();
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        new TimePickerDialog(this, this, hour, minute,
               true).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        myCalendar.set(Calendar.MINUTE, minute);
        updateTimeLabel();
    }

    private void updateTimeLabel() {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        ((Button) findViewById(selectedViewId)).setText(sdf.format(myCalendar.getTime()));
        firstDoseTime = myCalendar.getTimeInMillis();
    }
}
