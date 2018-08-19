package com.example.android.remedyme.utils;


import android.net.Uri;
import android.provider.BaseColumns;

public class RemedyContract {

    public static final class RemedyEntry implements BaseColumns {

        public static final String CONTENT_AUTHORITY = "com.example.android.remedyme";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_REMEDY = "remedy";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REMEDY)
                .build();

        /* Used internally as the name of our weather table. */
        public static final String TABLE_NAME = "remedies";

        public static final String COLUMN_REMEDY_NAME ="remedy_name";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_TIME_OF_FIRST_DOSE = "time_of_first_dose";
        public static final String COLUMN_TIMES= "times";
        public static final String COLUMN_QUANT_TIMES = "quant_times";
        public static final String COLUMN_TYPE_OF_DOSE= "type_of_dose";
        public static final String COLUMN_QUANT_TYPE_OF_DOSE = "quant_type_of_dose";
        public static final String COLUMN_ALARMON = "alarmOn";
        public static final String COLUMN_NEXT_NOTIFICATION = "next_notification";

        public static Uri buildWeatherUriWithDate(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }

    }
}

