package com.albertlardizabal.packoverflow.widget;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by albertlardizabal on 3/9/17.
 */

public class WidgetRemoteViewsService extends RemoteViewsService {

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new RemoteViewsFactory() {

			@Override
			public void onCreate() {

			}

			@Override
			public void onDataSetChanged() {

			}

			@Override
			public void onDestroy() {

			}

			@Override
			public int getCount() {
				return 0;
			}

			@Override
			public RemoteViews getViewAt(int position) {
				return null;
			}

			@Override
			public RemoteViews getLoadingView() {
				return null;
			}

			@Override
			public int getViewTypeCount() {
				return 0;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public boolean hasStableIds() {
				return false;
			}
		};
	}


}
