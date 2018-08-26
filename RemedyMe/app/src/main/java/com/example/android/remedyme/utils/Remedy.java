package com.example.android.remedyme.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class Remedy implements Parcelable {

    private int _ID;
    private String remedy_name;
    private long start_date;
    private long end_date;
    private long time_of_first_dose;
    private String times;
    private int quant_times;
    private String type_of_dose;
    private int quant_type_of_dose;
    private boolean alarmOn;
    private int nextNotification;

    public Remedy(int id, String remedy_name, long start_date, long end_date, long time_of_first_dose, String times,
                  int quant_times, String type_of_dose, int quant_type_of_dose, boolean alarmOn) {
        this._ID = id;
        this.remedy_name = remedy_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.time_of_first_dose = time_of_first_dose;
        this.times = times;
        this.quant_times = quant_times;
        this.type_of_dose = type_of_dose;
        this.quant_type_of_dose = quant_type_of_dose;
        this.alarmOn = alarmOn;
    }

    protected Remedy(Parcel in) {
        _ID = in.readInt();
        remedy_name = in.readString();
        start_date = in.readLong();
        end_date = in.readLong();
        time_of_first_dose = in.readLong();
        times = in.readString();
        quant_times = in.readInt();
        type_of_dose = in.readString();
        quant_type_of_dose = in.readInt();
        alarmOn = in.readInt() == 1 ? Boolean.TRUE : Boolean.FALSE;
    }

    public int getId() {
        return _ID;
    }

    public void setTime_of_first_dose(int time_of_first_dose) {
        this.time_of_first_dose = time_of_first_dose;
    }

    public long getTime_of_first_dose() {
        return time_of_first_dose;
    }

    public int getNextNotification() {
        return nextNotification;
    }

    public void setNextNotification(int nextNotification) {
        this.nextNotification = nextNotification;
    }

    public String getRemedy_name() {
        return remedy_name;
    }

    public void setRemedy_name(String remedy_name) {
        this.remedy_name = remedy_name;
    }

    public long getStart_date() {
        return start_date;
    }

    public void setStart_date(int start_date) {
        this.start_date = start_date;
    }

    public long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(int end_date) {
        this.end_date = end_date;
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

    public String dateToString(Date date) {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));
        return sdf.format(date.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Integer hourToInteger() {
        String myFormat = "HH"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));
        return Integer.valueOf(sdf.format(getNextNotification()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_ID);
        dest.writeString(remedy_name);
        dest.writeLong(start_date);
        dest.writeLong(end_date);
        dest.writeLong(time_of_first_dose);
        dest.writeString(times);
        dest.writeInt(quant_times);
        dest.writeString(type_of_dose);
        dest.writeInt(quant_type_of_dose);
        dest.writeInt(alarmOn ? 1:0);
    }

    public Integer minuteToInteger() {
        String myFormat = "mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        return Integer.valueOf(sdf.format(getNextNotification()));
    }

    public static final Creator<Remedy> CREATOR = new Creator<Remedy>() {
        public Remedy createFromParcel(Parcel in) {
            return new Remedy(in);
        }

        public Remedy[] newArray(int size) {
            return new Remedy[size];
        }
    };

    public Vector<Integer> nextDose() {
        int quantDose = 3;
        Vector<Integer> hours = new Vector(quantDose);
        Integer firstDose = hourToInteger();
        // interval vai ser usado apenas caso seja para tomar quantas vezes ao dia
        int interval = 24 % getQuant_times();
        for (int i = 0; i < quantDose; i++) {
            if (getTimes().equals("Time Interval hour")) {
                hours.add((firstDose + getQuant_times()) % 24);
            } else {
                hours.add((firstDose + interval) % 24);
            }
        }
        return hours;
    }
}
