package com.example.android.remedyme.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RemedyDBHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "remedy.db";

        private static final int DATABASE_VERSION = 2;

        public RemedyDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            /*
             * This String will contain a simple SQL statement that will create a table that will
             * cache our remedy data.
             */
            final String SQL_CREATE_WEATHER_TABLE =

                    "CREATE TABLE " + RemedyContract.RemedyEntry.TABLE_NAME + " (" +

                            RemedyContract.RemedyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                            RemedyContract.RemedyEntry.COLUMN_REMEDY_NAME + " TEXT NOT NULL," +

                            RemedyContract.RemedyEntry.COLUMN_START_DATE + " INTEGER NOT NULL," +

                            RemedyContract.RemedyEntry.COLUMN_END_DATE + " INTEGER NOT NULL," +

                            RemedyContract.RemedyEntry.COLUMN_TIME_OF_FIRST_DOSE + " INTEGER NOT NULL," +

                            RemedyContract.RemedyEntry.COLUMN_TIMES + " TEXT NOT NULL," +

                            RemedyContract.RemedyEntry.COLUMN_TYPE_OF_DOSE + " TEXT NOT NULL," +

                            RemedyContract.RemedyEntry.COLUMN_QUANT_TIMES + " INT NOT NULL," +

                            RemedyContract.RemedyEntry.COLUMN_QUANT_TYPE_OF_DOSE + " INT NOT NULL," +

                            RemedyContract.RemedyEntry.COLUMN_ALARMON + " INT NOT NULL," +

                            RemedyContract.RemedyEntry.COLUMN_NEXT_NOTIFICATION + " INT NOT NULL);";

            sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RemedyContract.RemedyEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }