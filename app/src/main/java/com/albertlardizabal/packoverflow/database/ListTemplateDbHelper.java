package com.albertlardizabal.packoverflow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by albertlardizabal on 3/9/17.
 */

public class ListTemplateDbHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "ListTemplate.db";

	private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + ListTemplateContract.ListTemplateEntry.TABLE_NAME + " (" +
					ListTemplateContract.ListTemplateEntry._ID + " INTEGER PRIMARY KEY," +
					ListTemplateContract.ListTemplateEntry.COLUMN_NAME_TITLE + " TEXT," +
					ListTemplateContract.ListTemplateEntry.COLUMN_NAME_ITEM_TITLE + " TEXT," +
					ListTemplateContract.ListTemplateEntry.COLUMN_NAME_ITEM_SUBTITLE + " TEXT," +
					ListTemplateContract.ListTemplateEntry.COLUMN_NAME_ITEM_QUANTITY + " TEXT)";

	private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + ListTemplateContract.ListTemplateEntry.TABLE_NAME;

	public ListTemplateDbHelper(Context context) {
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
