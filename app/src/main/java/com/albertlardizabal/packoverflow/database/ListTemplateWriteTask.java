package com.albertlardizabal.packoverflow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;

import com.albertlardizabal.packoverflow.models.PackingList;
import com.albertlardizabal.packoverflow.models.PackingListItem;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 3/19/17.
 */

public class ListTemplateWriteTask extends AsyncTaskLoader<ArrayList<PackingList>>  {

	private ListTemplateDbHelper dbHelper;

	public ListTemplateWriteTask(Context context) {
		super(context);
	}

	@Override
	public ArrayList<PackingList> loadInBackground() {
		// Gets the data repository in write mode
		dbHelper = new ListTemplateDbHelper(getContext());
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_TITLE, "Scuba Diving Expedition");
		values.put(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_ITEM_TITLE, "Buoyancy Compensator");

		ContentValues values2 = new ContentValues();
		values2.put(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_TITLE, "Annual Ski Trip");
		values2.put(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_ITEM_TITLE, "Skis");

		ContentValues values3 = new ContentValues();
		values3.put(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_TITLE, "Indoor Rock Climbing");
		values3.put(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_ITEM_TITLE, "Harness");

		ContentValues values4 = new ContentValues();
		values4.put(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_TITLE, "Skydiving");
		values4.put(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_ITEM_TITLE, "Parachute");

		// Insert the new row, returning the primary key value of the new row
		db.insert(ListTemplateContract.ListTemplateEntry.TABLE_NAME, null, values);
		db.insert(ListTemplateContract.ListTemplateEntry.TABLE_NAME, null, values2);
		db.insert(ListTemplateContract.ListTemplateEntry.TABLE_NAME, null, values3);
		db.insert(ListTemplateContract.ListTemplateEntry.TABLE_NAME, null, values4);

		db = dbHelper.getReadableDatabase();

		String[] projection = {
				ListTemplateContract.ListTemplateEntry.COLUMN_NAME_TITLE,
				ListTemplateContract.ListTemplateEntry.COLUMN_NAME_ITEM_TITLE,
		};

		Cursor cursor = db.query(
				ListTemplateContract.ListTemplateEntry.TABLE_NAME,      // Table to query
				projection,                                             // Columns to return
				null,                                                   // Columns for WHERE clause
				null,                                                   // Values for WHERE clause
				null,                                                   // Don't group rows
				null,                                                   // Don't filter by row groups
				null                                                    // The sort order
		);

		ArrayList<PackingList> packingLists = new ArrayList<>();
		while (cursor.moveToNext()) {
			String packingListTitle = cursor.getString(
					cursor.getColumnIndexOrThrow(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_TITLE));
			String packingListItemTitle = cursor.getString(
					cursor.getColumnIndexOrThrow(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_ITEM_TITLE));

			PackingListItem packingListItem = new PackingListItem();
			packingListItem.setTitle(packingListItemTitle);

			ArrayList<PackingListItem> items = new ArrayList<>();
			items.add(packingListItem);

			PackingList packingList = new PackingList();
			packingList.setTitle(packingListTitle);
			packingList.setItems(items);
			packingLists.add(packingList);
		}
		cursor.close();
		return packingLists;
	}
}
