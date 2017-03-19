package com.albertlardizabal.packoverflow.widget;

import android.content.Intent;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.albertlardizabal.packoverflow.R;

/**
 * Created by albertlardizabal on 3/9/17.
 */

public class WidgetRemoteViewsService extends RemoteViewsService {

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new RemoteViewsFactory() {

//			private Cursor data = null;
			@Override
			public void onCreate() {

			}

			@Override
			public void onDataSetChanged() {

//				if (data != null)
//					data.close();

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

//				if (position == AdapterView.INVALID_POSITION || data == null || !data.moveToPosition(position))
//					return null;

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
