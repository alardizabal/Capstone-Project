package com.albertlardizabal.packoverflow.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.albertlardizabal.packoverflow.R;
import com.albertlardizabal.packoverflow.ui.MainActivity;

/**
 * Created by albertlardizabal on 3/9/17.
 */

public class WidgetProvider extends AppWidgetProvider {

	private static final String LOG_TAG = WidgetProvider.class.getSimpleName();

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int widgetId : appWidgetIds) {

			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_listview);
			views.setOnClickPendingIntent(R.id.widget, pendingIntent);
			setRemoteAdapter(context, views);

			// TODO - Set empty state view
			appWidgetManager.updateAppWidget(widgetId, views);
		}
	}

	private void setRemoteAdapter(Context context, final RemoteViews views) {
		try {
			views.setRemoteAdapter(R.id.widget_list,
					new Intent(context, WidgetRemoteViewsService.class));
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			e.printStackTrace();
		}
	}
}
