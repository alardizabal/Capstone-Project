package com.albertlardizabal.packoverflow.database;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

/**
 * Created by albertlardizabal on 3/20/17.
 */

public class PackingListReadTask extends AsyncTask<Void, Void, Cursor> {

	private PackingListDbHelper dbHelper;

	public PackingListReadTask(Context context) {
		super(context);
	}
	@Override
	protected Cursor doInBackground(Void... params) {
		return null;
	}

	@Override
	protected void onPostExecute(Object o) {
		super.onPostExecute(o);
	}
}
