package com.albertlardizabal.packoverflow.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.albertlardizabal.packoverflow.dialogs.EditListDialogFragment;
import com.albertlardizabal.packoverflow.models.PackingList;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 2/26/17.
 */

public class SavedListsFragment extends Fragment {

	private static final String LOG_TAG = SavedListsFragment.class.getSimpleName();

	public interface OnSavedListsFragmentListener {
		public void onPackingListSelected(PackingList packingList);
	}

	private OnSavedListsFragmentListener listener;
	public static SavedListsAdapter adapter;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listener = (OnSavedListsFragmentListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(getActivity().toString() + " must implement OnSavedListsFragmentListener");
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_saved_lists, container, false);
		view.setBackgroundColor(Color.WHITE);

		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.saved_lists_recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		adapter = new SavedListsAdapter(PackingListFragment.packingLists);
		recyclerView.setAdapter(adapter);

		return view;
	}

	private class SavedListsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private PackingList listItem;

		private CheckBox checkBox;
		private TextView title;

		private SavedListsHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.cell_saved_list_item, parent, false));

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

	public class SavedListsAdapter extends RecyclerView.Adapter<SavedListsHolder> {

		private ArrayList<PackingList> lists;

		public SavedListsAdapter(ArrayList<PackingList> lists) {
			this.lists = lists;
		}

		@Override
		public SavedListsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			return new SavedListsHolder(layoutInflater, parent);
		}

		@Override
		public void onBindViewHolder(SavedListsHolder holder, final int position) {

			final PackingList list = lists.get(position);

			holder.title.setText(list.getTitle());
			holder.checkBox.setChecked(list.getIsChecked());

			holder.checkBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					for (int i = 0; i < lists.size(); i++) {
						if (lists.get(i).getTitle().equals(list.getTitle())) {
							list.setIsChecked(!list.getIsChecked());
							PackingListFragment.updateFirebase();
							return;
						}
					}
				}
			});

			holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					Bundle itemBundle = new Bundle();
					itemBundle.putParcelable("packingList", list);
					DialogFragment dialogFragment = new EditListDialogFragment();
					dialogFragment.setArguments(itemBundle);
					dialogFragment.show(getFragmentManager(), "editList");
					Log.d(LOG_TAG, "Long press");
					return false;
				}
			});

			holder.itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					PackingList packingList = PackingListFragment.packingLists.get(position);
					listener.onPackingListSelected(packingList);
				}
			});
		}

		@Override
		public int getItemCount() {
			return lists.size();
		}
	}
}
