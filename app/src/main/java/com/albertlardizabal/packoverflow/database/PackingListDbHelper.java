package com.albertlardizabal.packoverflow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by albertlardizabal on 3/20/17.
 */

public class PackingListDbHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "PackingList.db";

	private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + PackingListContract.PackingListEntry.TABLE_NAME + " (" +
					PackingListContract.PackingListEntry._ID + " INTEGER PRIMARY KEY," +
					PackingListContract.PackingListEntry.COLUMN_NAME_ITEM_TITLE + " TEXT)";

	private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + PackingListContract.PackingListEntry.TABLE_NAME;

	public PackingListDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}
