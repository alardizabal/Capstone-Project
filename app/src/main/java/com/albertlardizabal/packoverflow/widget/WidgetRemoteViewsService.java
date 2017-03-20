package com.albertlardizabal.packoverflow.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.support.v4.content.CursorLoader;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.albertlardizabal.packoverflow.R;

/**
 * Created by albertlardizabal on 3/9/17.
 */

public class WidgetRemoteViewsService extends RemoteViewsService {

	private static final String LOG_TAG = WidgetRemoteViewsService.class.getSimpleName();
	private static final String PROVIDER_NAME = "com.albertlardizabal.packoverflow.helpers.PackingListProvider";
	private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/list");

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new RemoteViewsFactory() {

			private Cursor data = null;
			@Override
			public void onCreate() {

				CursorLoader cursorLoader = new CursorLoader(
						getBaseContext(),
						CONTENT_URI,
						null,
						null,
						null,
						null
				);
				data = cursorLoader.loadInBackground();
			}

			@Override
			public void onDataSetChanged() {
				final long identityToken = Binder.clearCallingIdentity();
			}

			@Override
			public void onDestroy() {

			}

			@Override
			public int getCount() {
				return 8;
			}

			@Override
			public RemoteViews getViewAt(int position) {

				RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_listview_item);
				views.setTextViewText(R.id.widget_list_item_title, "Hello");

				return views;
			}

			@Override
			public RemoteViews getLoadingView() {
				return null;
			}

			@Override
			public int getViewTypeCount() {
				return 1;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public boolean hasStableIds() {
				return true;
			}
		};
	}


}
