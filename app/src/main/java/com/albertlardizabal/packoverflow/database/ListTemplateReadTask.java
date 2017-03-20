package com.albertlardizabal.packoverflow.database;

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

public class ListTemplateReadTask extends AsyncTaskLoader<ArrayList<PackingList>> {

	private ListTemplateDbHelper dbHelper;

	public ListTemplateReadTask(Context context) {
		super(context);
	}

	@Override
	public ArrayList<PackingList> loadInBackground() {
		dbHelper = new ListTemplateDbHelper(getContext());
		SQLiteDatabase db = dbHelper.getReadableDatabase();

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
