package com.albertlardizabal.packoverflow.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albertlardizabal.packoverflow.R;
import com.albertlardizabal.packoverflow.database.ListTemplateContract;
import com.albertlardizabal.packoverflow.database.ListTemplateDbHelper;
import com.albertlardizabal.packoverflow.models.PackingList;
import com.albertlardizabal.packoverflow.models.PackingListItem;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 3/9/17.
 */

public class TemplateListsFragment extends Fragment {

	private static final String LOG_TAG = TemplateListsFragment.class.getSimpleName();

	private ListTemplateDbHelper dbHelper;

	private ArrayList<PackingList> lists = new ArrayList<>();

	private TemplateListsAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_template_lists, container, false);
		view.setBackgroundColor(Color.WHITE);

		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.template_lists_recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		adapter = new TemplateListsAdapter(lists);
		recyclerView.setAdapter(adapter);

		new ReadDatabaseTask().execute();

		return view;
	}

	@Override
	public void onDestroy() {
		dbHelper.close();
		super.onDestroy();
	}

	private class ReadDatabaseTask extends AsyncTask<Void, Void, Cursor> {

		@Override
		protected Cursor doInBackground(Void... params) {
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
			lists = packingLists;
			cursor.close();
			return cursor;
		}

		@Override
		protected void onPostExecute(Cursor cursor) {
			if (lists.size() == 0) {
				new WriteDatabaseTask().execute();
			} else {
				adapter.notifyDataSetChanged();
			}
		}
	}

	private class WriteDatabaseTask extends AsyncTask<Void, Void, Cursor> {

		@Override
		protected Cursor doInBackground(Void... params) {
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
			return null;
		}

		@Override
		protected void onPostExecute(Cursor cursor) {
			new ReadDatabaseTask().execute();
		}
	}

	public class TemplateListsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private PackingList listItem;

		private TextView title;

		public TemplateListsHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.cell_template_list_item, parent, false));

			title = (TextView) itemView.findViewById(R.id.template_list_item_title);

			itemView.setOnClickListener(this);
		}

		public void bind(PackingList packingList) {
			listItem = packingList;
		}

		@Override
		public void onClick(View v) {

		}
	}

	public class TemplateListsAdapter extends RecyclerView.Adapter<TemplateListsHolder> {

		public TemplateListsAdapter(ArrayList<PackingList> listItems) {
			lists = listItems;
		}

		@Override
		public TemplateListsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			return new TemplateListsHolder(layoutInflater, parent);
		}

		@Override
		public void onBindViewHolder(TemplateListsHolder holder, int position) {
			PackingList listItem = lists.get(position);
			holder.title.setText(listItem.getTitle());
		}

		@Override
		public int getItemCount() {
			return lists.size();
		}
	}
}
