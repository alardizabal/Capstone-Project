package com.albertlardizabal.packoverflow.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.albertlardizabal.packoverflow.R;
import com.albertlardizabal.packoverflow.models.PackingList;
import com.albertlardizabal.packoverflow.ui.PackingListFragment;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 3/8/17.
 */

public class EditListDialogFragment extends DialogFragment {

	private static final String LOG_TAG = EditListDialogFragment.class.getSimpleName();

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final EditText title;

		final PackingList list;

		if (getArguments() != null) {
			list = getArguments().getParcelable("packingList");
		} else {
			list = null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.fragment_edit_list_dialog, null);

		title = (EditText) v.findViewById(R.id.dialog_list_title);

		if (list != null) {
			title.setText(list.getTitle());
		}

		builder.setView(v)
				.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						ArrayList<PackingList> lists = PackingListFragment.packingLists;
						PackingList tempList = null;
						if (list == null) {
							PackingList newList = new PackingList();
							if (title.getText().length() > 0) {
								newList.setTitle(title.getText().toString());
							}
							for (PackingList aList : lists) {
								aList.setActive(false);
							}
							newList.setActive(true);
							lists.add(newList);
						} else {
							for (int i = 0; i < lists.size(); i++) {
								tempList = lists.get(i);
								if (tempList.getTitle().equals(list.getTitle())) {
									tempList.setTitle(title.getText().toString());
									break;
								}
							}
						}
						if (list != null) {
							PackingListFragment.updateFirebaseWithList(list, tempList);
						} else {
							PackingListFragment.updateFirebase();
						}
					}
				})
				.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						EditListDialogFragment.this.getDialog().cancel();
					}
				});
		return builder.create();
	}
}
