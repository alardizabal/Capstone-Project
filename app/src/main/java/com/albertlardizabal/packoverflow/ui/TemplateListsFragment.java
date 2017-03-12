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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    private RecyclerView recyclerView;
    private TemplateListsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_template_lists, container, false);
        view.setBackgroundColor(Color.CYAN);

        recyclerView = (RecyclerView) view.findViewById(R.id.template_lists_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    private void updateUI() {
        ArrayList<PackingList> listItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            PackingList item = new PackingList();
            item.setTitle("Title");
            listItems.add(item);
        }
        adapter = new TemplateListsAdapter(listItems);
        recyclerView.setAdapter(adapter);
    }

    private class ReadDatabase extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
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
            return cursor;
        }
    }

    private class WriteDatabase extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            // Gets the data repository in write mode
            dbHelper = new ListTemplateDbHelper(getContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_TITLE, "Scuba Diving Expedition");
            values.put(ListTemplateContract.ListTemplateEntry.COLUMN_NAME_ITEM_TITLE, "Buoyancy Compensator");

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(ListTemplateContract.ListTemplateEntry.TABLE_NAME, null, values);
            return null;
        }
    }

    public class TemplateListsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PackingList listItem;

        private CheckBox checkBox;
        private TextView title;

        public TemplateListsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.saved_list_item, parent, false));

            checkBox = (CheckBox) itemView.findViewById(R.id.list_item_checkbox);
            title = (TextView) itemView.findViewById(R.id.saved_list_item_title);

            itemView.setOnClickListener(this);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(LOG_TAG, "tapped");
                }
            });
        }

        public void bind(PackingList packingList) {
            listItem = packingList;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class TemplateListsAdapter extends RecyclerView.Adapter<TemplateListsHolder> {

        private ArrayList<PackingList> listItems;

        public TemplateListsAdapter(ArrayList<PackingList> listItems) { this.listItems = listItems; }

        @Override
        public TemplateListsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TemplateListsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TemplateListsHolder holder, int position) {
            PackingList listItem = listItems.get(position);
            holder.bind(listItem);
        }

        @Override
        public int getItemCount() { return listItems.size(); }
    }
}
