package com.albertlardizabal.packoverflow.helpers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.albertlardizabal.packoverflow.database.PackingListContract;
import com.albertlardizabal.packoverflow.database.PackingListDbHelper;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 3/19/17.
 */

public class PackingListProvider extends ContentProvider {

	private PackingListDbHelper dbHelper;
	private ArrayList<String> itemNames = new ArrayList<>();

	private static final String PROVIDER_NAME = "com.albertlardizabal.packoverflow.helpers.PackingListProvider";
	private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/list");

	private static final UriMatcher uriMatcher = getUriMatcher();
	private static UriMatcher getUriMatcher() {
		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//		uriMatcher.addURI(PROVIDER_NAME, "items", IMAGES);
		return uriMatcher;
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		dbHelper = new PackingListDbHelper(getContext());
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String[] listProjection = {
				PackingListContract.PackingListEntry.COLUMN_NAME_ITEM_TITLE,
		};

		Cursor cursor = db.query(
				PackingListContract.PackingListEntry.TABLE_NAME,        // Table to query
				listProjection,                                         // Columns to return
				null,                                                   // Columns for WHERE clause
				null,                                                   // Values for WHERE clause
				null,                                                   // Don't group rows
				null,                                                   // Don't filter by row groups
				null                                                    // The sort order
		);

		itemNames.clear();
		while (cursor.moveToNext()) {
			String packingListItemTitle = cursor.getString(
					cursor.getColumnIndexOrThrow(PackingListContract.PackingListEntry.COLUMN_NAME_ITEM_TITLE));

			itemNames.add(packingListItemTitle);
		}
		cursor.close();
		return cursor;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		return null;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
		return null;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
		return 0;
	}
}
