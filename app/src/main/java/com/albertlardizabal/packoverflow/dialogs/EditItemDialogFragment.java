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
import com.albertlardizabal.packoverflow.models.PackingListItem;
import com.albertlardizabal.packoverflow.ui.PackingListFragment;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 3/8/17.
 */

public class EditItemDialogFragment extends DialogFragment {

    private static final String LOG_TAG = EditItemDialogFragment.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final EditText title;
        final EditText subtitle;
        final EditText quantity;

        final PackingListItem item;

        if (getArguments() != null) {
            item = getArguments().getParcelable("listItem");
        } else {
            item = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_edit_item_dialog, null);

        title = (EditText) v.findViewById(R.id.dialog_item_title);
        subtitle = (EditText) v.findViewById(R.id.dialog_item_subtitle);
        quantity = (EditText) v.findViewById(R.id.dialog_item_quantity);

        if (item != null) {
            title.setText(item.getTitle());
            subtitle.setText(item.getSubtitle());
            quantity.setText(item.getQuantity());
        }

        builder.setView(v)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String currentListTitle = PackingListFragment.currentPackingList.getTitle();
                        ArrayList<PackingList> lists = PackingListFragment.packingLists;
                        for (int i = 0; i < lists.size(); i++) {
                            PackingList list = lists.get(i);
                            if (list.getTitle().equals(currentListTitle)) {
                                ArrayList<PackingListItem> items = list.getItems();
                                if (item != null) {
                                    for (int j = 0; j < items.size(); j++) {
                                        PackingListItem editItem = items.get(j);
                                        if (editItem.getTitle().equals(item.getTitle())) {
                                            if (title.getText().length() > 0) {
                                                editItem.setTitle(title.getText().toString());
                                            }
                                            if (subtitle.getText().length() > 0) {
                                                editItem.setSubtitle(subtitle.getText().toString());
                                            }
                                            if (quantity.getText().length() > 0) {
                                                editItem.setSubtitle(quantity.getText().toString());
                                            }
                                            PackingListFragment.updateFirebase();
                                            return;
                                        }
                                    }
                                } else {
                                    PackingListItem newItem = new PackingListItem();
                                    if (title.getText().length() > 0) {
                                        newItem.setTitle(title.getText().toString());
                                    }
                                    if (subtitle.getText().length() > 0) {
                                        newItem.setSubtitle(subtitle.getText().toString());
                                    }
                                    if (quantity.getText().length() > 0) {
                                        newItem.setQuantity(quantity.getText().toString());
                                    } else {
                                        newItem.setQuantity("1");
                                    }
                                    items.add(newItem);
                                    PackingListFragment.updateFirebase();
                                    return;
                                }
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditItemDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
