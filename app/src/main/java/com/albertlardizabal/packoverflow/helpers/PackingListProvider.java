package com.albertlardizabal.packoverflow.helpers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by albertlardizabal on 3/19/17.
 */

public class PackingListProvider extends ContentProvider {

	private static final String PROVIDER_NAME = "com.albertlardizabal.packoverflow.helpers.PackingListProvider";
	private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/list");
	private static final int IMAGES = 1;

	private static final UriMatcher uriMatcher = getUriMatcher();
	private static UriMatcher getUriMatcher() {
		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "images", IMAGES);
		return uriMatcher;
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

		return null;
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
