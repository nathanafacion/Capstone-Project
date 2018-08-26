package com.example.android.remedyme.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.android.remedyme.R;
import com.example.android.remedyme.utils.Remedy;

import java.util.Date;
import java.util.List;

/**
 * Created by Joaoe on 02/06/2018.
 * */

public class RemedyWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        List<Remedy> remedyList, int appWidgetId) {
        RemoteViews rv;
        rv = getIngredientListRemoteView(context, remedyList);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    public static void updateRemediesWidget(Context context, AppWidgetManager appWidgetManager,
                                            List<Remedy> remedyList, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, remedyList, appWidgetId);
        }
    }

    private static RemoteViews getIngredientListRemoteView(Context context, List<Remedy> remedyList) {
        RemoteViews remedyView = new RemoteViews(context.getPackageName(), R.layout.remedy_widget);
        StringBuilder remedyTimesList = new StringBuilder();
        int numMaxRemedy = 7;
        for (Remedy remedy : remedyList) {
            String asd = remedy.dateToString(new Date(remedy.getNextNotification()))+ " " + remedy.getRemedy_name() +" ("+ remedy.getQuant_type_of_dose()+ remedy.getType_of_dose() +")";
            remedyTimesList.append(asd).append("\n");
            numMaxRemedy--;
            if(numMaxRemedy == 0) break;
        }
        //remedyView.setTextViewText(R.id.remedy_widget_title, "Remedy Me");
        remedyView.setTextViewText(R.id.remedy_widget_content, remedyTimesList.toString());


        return remedyView;
    }
}
