package com.albertlardizabal.packoverflow.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * Created by albertlardizabal on 3/9/17.
 */

public class WidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = WidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        for (int widgetId : appWidgetIds) {
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
//                    R.layout.widget_listview);
//
//            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
//            setRemoteAdapter(context, remoteViews);
//
//            Intent clickIntent = new Intent(context, MyStocksActivity.class);
//            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
//                    .addNextIntentWithParentStack(clickIntent)
//                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);
//            appWidgetManager.updateAppWidget(widgetId, remoteViews);
//        }
    }
}
