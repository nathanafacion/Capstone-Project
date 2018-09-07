package com.example.android.remedyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.remedyme.utils.Remedy;
import com.example.android.remedyme.utils.RemedyContract;
import com.example.android.remedyme.widget.RemedyWidgetProvider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    public static RemedyAdapter adapter;
    public static Context context;
    private static List<Remedy> remediesList = new ArrayList<Remedy>();
    public static String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        loadRemediesData(getApplicationContext());

        context = getApplicationContext();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userEmail = extras.get("user").toString();
        }

        adapter = new RemedyAdapter(this, remediesList);

        rv = findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RemedyCrudActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        AdView mAdView = (AdView) findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        createNotificationChannel();
    }

    private void loadRemediesData(Context context) {
        new FetchRemediesTask().execute(context);
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
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = getResources().getString(R.string.description_remedy);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("REMEDY_ME", "Remedio", importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void updateWidget(List<Remedy> remediesData) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RemedyWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget_layout);
        //Now update all widgets
        RemedyWidgetProvider.updateRemediesWidget(this, appWidgetManager, remediesData, appWidgetIds);
    }

    private class RemedyAdapter extends RecyclerView.Adapter<RemedyViewHolder> {

        List<Remedy> remediesData;
        private Context context;
        private RemedyAdapter(@NonNull Context context, @NonNull List<Remedy> objects) {
            remediesData = objects;
            this.context = context;
        }

        @NonNull
        @Override
        public RemedyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.remedy_list_item, parent, false);
            return new RemedyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RemedyViewHolder holder, int position) {
            final Remedy remedy = remediesData.get(position);
            String text = remedy.dateToString(new Date(remedy.getNextNotification()))+ " " + remedy.getRemedy_name() +" ("+ remedy.getQuant_type_of_dose()+ remedy.getType_of_dose() +")";
            holder.titleView.setText(text);
            holder.titleView.setContentDescription(text);
            Vector<Integer> nextDose = remedy.nextDose();
            String minutes = String.valueOf(remedy.minuteToInteger());
            String formatMinutes;
            formatMinutes = minutes.length() < 2 ? "0" + minutes : "" + minutes;
            String time = "";
            String and = " | ";
            String formatHours;
            int quant_times = remedy.getQuant_times() > 3 ? 3 : remedy.getQuant_times();

            for(int i = 0; i < quant_times; i++){
                formatHours = nextDose.get(i) < 9 ? "0":"";
                time = time + String.valueOf(formatHours + nextDose.get(i)) + ":" + formatMinutes;
                if ( i + 1 < quant_times){
                    time = time + and;
                }
            }
            if (quant_times == 1) {
                time = getResources().getString(R.string.msg_tomorrow) + time;
            }
            String msgNextDoses = getResources().getString(R.string.msg_next_doses);
            msgNextDoses = msgNextDoses.replace("{?}", time);
            holder.tv_next_times.setText(msgNextDoses);
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), RemedyCrudActivity.class);
                    intent.putExtra("remedy", remedy);
                    view.getContext().startActivity(intent);
                }
            });
            final String deleteMessage = context.getResources().getString(R.string.msg_remove_remedy);
            deleteMessage.replace("{x}", remedy.getRemedy_name());
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    new AlertDialog.Builder(view.getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getResources().getString(R.string.notification_title_remove_remedy))
                            .setMessage(deleteMessage)
                            .setPositiveButton(getResources().getString(R.string.msg_yes), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ContentResolver contentResolver = view.getContext().getContentResolver();
                                    contentResolver.delete(RemedyContract.RemedyEntry.CONTENT_URI,
                                            RemedyContract.RemedyEntry._ID + " = ? ",
                                            new String[]{String.valueOf(remedy.getId())});
                                    remediesData.remove(remedy);
                                    notifyDataSetChanged();
                                }

                            })
                            .setNegativeButton(getResources().getString(R.string.msg_no), null)
                            .show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return remediesData.size();
        }

        private void setRemediesData(List<Remedy> remediesData) {
            this.remediesData = remediesData;
            updateWidget(remediesData);
            notifyDataSetChanged();
        }
    }

    public static class RemedyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView tv_next_times;
        public ImageButton editButton;
        public ImageButton deleteButton;

        public RemedyViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.tv_title);
            tv_next_times = view.findViewById(R.id.tv_next_times);
            titleView.setTypeface(null, Typeface.BOLD);
            editButton = view.findViewById(R.id.bt_edit);
            deleteButton = view.findViewById(R.id.bt_del);
        }
    }

    public static class FetchRemediesTask extends AsyncTask<Context, Void, List<Remedy>> {

        @Override
        protected List<Remedy> doInBackground(Context... contexts) {
            ContentResolver contentResolver = contexts[0].getContentResolver();
            try (Cursor c = contentResolver.query(
                    RemedyContract.RemedyEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    RemedyContract.RemedyEntry.COLUMN_NEXT_NOTIFICATION + " ASC")) {

                List<Remedy> remedies = new ArrayList<Remedy>();
                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        int remedy_id = c.getInt(0);
                        String remedy_name = c.getString(1);
                        long start_date = c.getLong(2);
                        long end_date = c.getLong(3);
                        long time_of_first_dose = c.getLong(4);
                        String times = c.getString(5);
                        String type_of_dose = c.getString(6);
                        int quant_times = c.getInt(7);
                        int quant_type_of_dose = c.getInt(8);
                        boolean alarmOn = Boolean.TRUE.equals(c.getInt(9));
                        long nextNotification = c.getLong(10);

                        Remedy remedy = new Remedy(remedy_id, remedy_name, start_date, end_date, time_of_first_dose, times,
                                quant_times, type_of_dose, quant_type_of_dose, alarmOn, nextNotification);
                        remedies.add(remedy);
                    } while (c.moveToNext());
                }
                return remedies;
            }
        }

        protected void onPostExecute(List<Remedy> remediesData) {
            if (remediesData != null && remediesData.size()> 0) {
                adapter.setRemediesData(remediesData);
            }
        }
    }
}
