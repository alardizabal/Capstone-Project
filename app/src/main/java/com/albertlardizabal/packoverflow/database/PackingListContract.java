package com.albertlardizabal.packoverflow.database;

import android.provider.BaseColumns;

/**
 * Created by albertlardizabal on 3/20/17.
 */

public final class PackingListContract {

	private PackingListContract() {
	}

	public static class PackingListEntry implements BaseColumns {
		public static final String TABLE_NAME = "template";
		public static final String COLUMN_NAME_ITEM_TITLE = "item_title";
	}
}
