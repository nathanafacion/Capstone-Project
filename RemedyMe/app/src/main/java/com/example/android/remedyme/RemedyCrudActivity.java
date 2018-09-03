package com.example.android.remedyme;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.remedyme.utils.Remedy;
import com.example.android.remedyme.utils.RemedyContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    boolean isEditing = false;
    Remedy remedy;
    @BindView(R.id.tv_l_dose_type) TextView tv_dose_type;
    @BindView(R.id.tv_l_times) TextView tv_times;
    @BindView(R.id.tv_l_end_date) TextView tv_end_date;
    @BindView(R.id.tv_l_remedy_name) TextView tv_remedyName;
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
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        //****Spinners pro "Times"
        //Times per Day - Time Interval
        ArrayAdapter<CharSequence> timesAdapter = ArrayAdapter.createFromResource(this,
                R.array.times_array, android.R.layout.simple_spinner_item);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionTime.setAdapter(timesAdapter);

        //Quantidade
        Integer[] items = new Integer[12];
        for (int i=0; i<12;i++) {
            items[i]=i;
        }
        //****Spinners pro "Type of dose"
        //Pills-ML
        ArrayAdapter<CharSequence> typeOfDoseAdapter = ArrayAdapter.createFromResource(this,
                R.array.typeOfDose_array, android.R.layout.simple_spinner_item);
        typeOfDoseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doseType.setAdapter(typeOfDoseAdapter);

        myCalendar = Calendar.getInstance();

        Intent remedyIntent = getIntent();
        if (remedyIntent != null && remedyIntent.getExtras() != null) {
            isEditing = true;
            remedy = remedyIntent.getExtras().getParcelable("remedy");
            remedyName.setText(remedy.getRemedy_name());
            startDateButton.setText(formatDate(new Date(remedy.getStart_date())));
            endDateButton.setText(formatDate(new Date(remedy.getEnd_date())));
            firstDoseButton.setText(formatTimeLabel(new Date(remedy.getTime_of_first_dose())));
            int typeOfDoseInt = typeOfDoseAdapter.getPosition(remedy.getType_of_dose());
            doseType.setSelection(typeOfDoseInt);
            doseTypeAmount.setText(String.valueOf(remedy.getQuant_type_of_dose()));
            int timesInt = timesAdapter.getPosition(remedy.getTimes());
            optionTime.setSelection(timesInt);
            optionTimeAmount.setText(String.valueOf(remedy.getQuant_times()));
            alarmOn.setChecked(remedy.isAlarmOn());
        } else {
            selectedViewId = R.id.et_l_start_date;
            updateCalendarLabel();
            selectedViewId = R.id.et_l_end_date;
            updateCalendarLabel();
            selectedViewId = R.id.cb_first_dose_hour;
            updateTimeLabel();
        }
    }

    public boolean validation(){
        boolean hasError = false;
        tv_remedyName.setTextColor(Color.parseColor("#000000"));
        tv_times.setTextColor(Color.parseColor("#000000"));
        tv_dose_type.setTextColor(Color.parseColor("#000000"));
        tv_end_date.setTextColor(Color.parseColor("#000000"));
        if(remedyName.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Remedy Name should not be empty", Toast.LENGTH_SHORT).show();
            tv_remedyName.setTextColor(Color.parseColor("#FF0000"));
            return false;
        }
        if(Integer.valueOf(optionTimeAmount.getText().toString()) > 24 || Integer.valueOf(optionTimeAmount.getText().toString()) < 1){
            Toast.makeText(getApplicationContext(), "Time quantity should be between 1 and 24", Toast.LENGTH_SHORT).show();
            tv_times.setTextColor(Color.parseColor("#FF0000"));
            return false;
        }
        if(Integer.valueOf(doseTypeAmount.getText().toString()) < 1){
            Toast.makeText(getApplicationContext(), "Time quantity should be less than 24", Toast.LENGTH_SHORT).show();
            tv_dose_type.setTextColor(Color.parseColor("#FF0000"));
            return false;
        }
        Date now =  new Date();
        if(startDate > endDate){
            Toast.makeText(getApplicationContext(), "Date Start should before than end date", Toast.LENGTH_SHORT).show();
            tv_end_date.setTextColor(Color.parseColor("#FF0000"));
            return false;
        }
        return true;
    }

    public void saveNewRemedy(View view) {

        if (validation()) {
            ContentResolver contentResolver = view.getContext().getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(RemedyContract.RemedyEntry.COLUMN_START_DATE, startDate);
            cv.put(RemedyContract.RemedyEntry.COLUMN_END_DATE, endDate);
            cv.put(RemedyContract.RemedyEntry.COLUMN_TIME_OF_FIRST_DOSE, firstDoseTime);
            cv.put(RemedyContract.RemedyEntry.COLUMN_NEXT_NOTIFICATION, firstDoseTime);
            cv.put(RemedyContract.RemedyEntry.COLUMN_ALARMON, alarmOn.isChecked());
            cv.put(RemedyContract.RemedyEntry.COLUMN_QUANT_TIMES, Integer.valueOf(optionTimeAmount.getText().toString()));
            cv.put(RemedyContract.RemedyEntry.COLUMN_TYPE_OF_DOSE, String.valueOf(doseType.getSelectedItem()));
            cv.put(RemedyContract.RemedyEntry.COLUMN_QUANT_TYPE_OF_DOSE, doseTypeAmount.getText().toString());
            cv.put(RemedyContract.RemedyEntry.COLUMN_TIMES, String.valueOf(optionTime.getSelectedItem()));
            cv.put(RemedyContract.RemedyEntry.COLUMN_QUANT_TIMES, optionTimeAmount.getText().toString());
            cv.put(RemedyContract.RemedyEntry.COLUMN_REMEDY_NAME, remedyName.getText().toString());

            if (isEditing) {
                contentResolver.update(
                        RemedyContract.RemedyEntry.CONTENT_URI,
                        cv,
                        RemedyContract.RemedyEntry._ID + " = ? ",
                        new String[]{String.valueOf(remedy.getId())});
            } else {
                contentResolver.insert(
                        RemedyContract.RemedyEntry.CONTENT_URI, cv);
            }

            if (alarmOn.isChecked()) {
                AlarmManager alarmManager;
                PendingIntent pendingIntent;

                Intent myIntent = new Intent(this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, 0);

                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(startDate + firstDoseTime);

                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 10000, pendingIntent);
            }

            Intent intent = new Intent(view.getContext(), MainActivity.class);
            view.getContext().startActivity(intent);
        }
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

    private String formatDate(Date date) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        return sdf.format(date);
    }

    private void updateCalendarLabel() {
        String dateString = formatDate(myCalendar.getTime());
        ((Button) findViewById(selectedViewId)).setText(dateString);
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

    private String formatTimeLabel(Date date) {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        return sdf.format(date);
    }

    private void updateTimeLabel() {
        ((Button) findViewById(selectedViewId)).setText(formatTimeLabel(myCalendar.getTime()));
        firstDoseTime = myCalendar.getTimeInMillis();
    }
}
