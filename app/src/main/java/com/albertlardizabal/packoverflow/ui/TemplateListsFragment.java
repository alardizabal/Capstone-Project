package com.albertlardizabal.packoverflow.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albertlardizabal.packoverflow.R;
import com.albertlardizabal.packoverflow.database.ListTemplateReadTask;
import com.albertlardizabal.packoverflow.database.ListTemplateWriteTask;
import com.albertlardizabal.packoverflow.models.PackingList;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 3/9/17.
 */

public class TemplateListsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<PackingList>> {

	private static final String LOG_TAG = TemplateListsFragment.class.getSimpleName();

	private ArrayList<PackingList> lists = new ArrayList<>();

	private TemplateListsAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_template_lists, container, false);
		view.setBackgroundColor(Color.WHITE);

		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.template_lists_recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		getLoaderManager().initLoader(0, null, this).forceLoad();

		adapter = new TemplateListsAdapter(lists);
		recyclerView.setAdapter(adapter);

		return view;
	}

	@Override
	public Loader<ArrayList<PackingList>> onCreateLoader(int id, Bundle args) {
		SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
		Boolean isFirstLoad = sharedPreferences.getBoolean(getString(R.string.preferences_is_first_load_database), true);

		if (!isFirstLoad) {
			return new ListTemplateReadTask(getContext());
		} else {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(getString(R.string.preferences_is_first_load_database), false);
			editor.commit();

			return new ListTemplateWriteTask(getContext());
		}
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<PackingList>> loader, ArrayList<PackingList> data) {
		if (data != null) {
			lists = data;
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<PackingList>> loader) {

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
