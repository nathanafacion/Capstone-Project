package com.example.android.remedyme.utils;

import java.text.DateFormat;
import java.util.Date;

public class Remedy {

    private String remedy_name;
    private Date start_date;
    private DateFormat time_of_first_dose;
    private String times;
    private int quant_times;
    private String type_of_dose;
    private int quant_type_of_dose;
    private boolean alarmOn;

    public Remedy(String remedy_name, Date start_date, DateFormat time_of_first_dose, String times, int quant_times, String type_of_dose, int quant_type_of_dose, boolean alarmOn) {
        this.remedy_name = remedy_name;
        this.start_date = start_date;
        this.time_of_first_dose = time_of_first_dose;
        this.times = times;
        this.quant_times = quant_times;
        this.type_of_dose = type_of_dose;
        this.quant_type_of_dose = quant_type_of_dose;
        this.alarmOn = alarmOn;
    }

    public String getRemedy_name() {
        return remedy_name;
    }

    public void setRemedy_name(String remedy_name) {
        this.remedy_name = remedy_name;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public DateFormat getTime_of_first_dose() {
        return time_of_first_dose;
    }

    public void setTime_of_first_dose(DateFormat time_of_first_dose) {
        this.time_of_first_dose = time_of_first_dose;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getQuant_times() {
        return quant_times;
    }

    public void setQuant_times(int quant_times) {
        this.quant_times = quant_times;
    }

    public String getType_of_dose() {
        return type_of_dose;
    }

    public void setType_of_dose(String type_of_dose) {
        this.type_of_dose = type_of_dose;
    }

    public int getQuant_type_of_dose() {
        return quant_type_of_dose;
    }

    public void setQuant_type_of_dose(int quant_type_of_dose) {
        this.quant_type_of_dose = quant_type_of_dose;
    }

    public boolean isAlarmOn() {
        return alarmOn;
    }

    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }

}
