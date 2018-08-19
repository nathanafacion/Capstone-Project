package com.example.android.remedyme;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.remedyme.utils.Remedy;
import com.example.android.remedyme.utils.RemedyContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    public static RemedyAdapter adapter;
    public static Context mainContext;

    private static List<Remedy> remediesList = new ArrayList<Remedy>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadRemediesData(getApplicationContext());

        adapter = new RemedyAdapter(this, remediesList);

        rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RemedyCrudActivity.class);
                view.getContext().startActivity(intent);
            }
        });
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
        if (id == R.id.action_backup) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class RemedyAdapter extends RecyclerView.Adapter<RemedyViewHolder> {

        List<Remedy> remediesData;

        public RemedyAdapter(@NonNull Context context, @NonNull List<Remedy> objects) {
            remediesData = objects;
        }

        @NonNull
        @Override
        public RemedyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.remedy_list_item, parent, false);
            return new RemedyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RemedyViewHolder holder, int position) {
            Remedy remedy = remediesData.get(position);
            holder.titleView.setText(remedy.getRemedy_name());
            holder.tv_next_times.setText(remedy.getRemedy_name() + " " + remedy.getType_of_dose());

        }

        @Override
        public int getItemCount() {
            return remediesData.size();
        }

        public void setRemediesData(List<Remedy> remediesData) {
            this.remediesData = remediesData;
            notifyDataSetChanged();
        }
    }

    public static class RemedyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView tv_next_times;

        public RemedyViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.tv_title);
            tv_next_times = view.findViewById(R.id.tv_next_times);
            titleView.setTypeface(null, Typeface.BOLD);
        }
    }

    public static class FetchRemediesTask extends AsyncTask<Context, Void, List<Remedy>> {

        @Override
        protected List<Remedy> doInBackground(Context... contexts) {
            ContentResolver contentResolver = contexts[0].getContentResolver();
            Cursor c = contentResolver.query(
                    RemedyContract.RemedyEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

            List<Remedy> remedies = new ArrayList<Remedy>();
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                do {
                    String remedy_name = c.getString(1);
                    long start_date = c.getInt(2);
                    long end_date = c.getInt(3);
                    long time_of_first_dose = c.getLong(4);
                    String times = c.getString(5);
                    String type_of_dose = c.getString(6);
                    int quant_times = c.getInt(7);
                    int quant_type_of_dose = c.getInt(8);
                    boolean alarmOn = Boolean.TRUE.equals(c.getInt(9));

                    Remedy remedy = new Remedy(remedy_name, start_date, end_date, time_of_first_dose, times,
                            quant_times, type_of_dose, quant_type_of_dose, alarmOn);
                    remedies.add(remedy);
                } while (c.moveToNext());
            }
            return remedies;
        }

        protected void onPostExecute(List<Remedy> remediesData) {
            if (remediesData != null && remediesData.size()> 0) {
                adapter.setRemediesData(remediesData);
            }
        }
    }

}
