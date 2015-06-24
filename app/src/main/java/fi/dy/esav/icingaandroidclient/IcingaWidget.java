package fi.dy.esav.icingaandroidclient;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class IcingaWidget extends AppWidgetProvider {
    @Override
    public void onEnabled(Context context) { }

    @Override
    public void onDisabled(Context context) { }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for(int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent intent = new Intent(context, IcingaWidgetService.class);

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.icinga_widget);
        rv.setRemoteAdapter(R.id.icinga_widget_listview, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }
}

