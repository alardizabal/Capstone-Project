package com.albertlardizabal.packoverflow.ui;

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
import android.widget.TextView;

import com.albertlardizabal.packoverflow.R;
import com.albertlardizabal.packoverflow.dialogs.EditItemDialogFragment;
import com.albertlardizabal.packoverflow.models.PackingList;
import com.albertlardizabal.packoverflow.models.PackingListItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 2/25/17.
 */

public class PackingListFragment extends Fragment {

    private static final String LOG_TAG = PackingListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    public static PackingListAdapter adapter;

    public static ArrayList<PackingList> packingLists = new ArrayList<>();

    public static PackingList currentPackingList = new PackingList();
    public static ArrayList<PackingListItem> currentListItems = new ArrayList<>();

    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final DatabaseReference rootReference = firebaseDatabase.getReference();
    public static final DatabaseReference savedListsReference = rootReference.child("saved_lists");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_packing_list, container, false);
        view.setBackgroundColor(Color.WHITE);

        recyclerView = (RecyclerView) view.findViewById(R.id.packing_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new PackingListAdapter(currentListItems);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public static void updateFirebase() {
        savedListsReference.setValue(packingLists);
    }

    @Override
    public void onStart() {
        super.onStart();
        packingLists.clear();

        savedListsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "onDataChanged");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, "onCancelled");
            }
        });

        savedListsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PackingList packingList = dataSnapshot.getValue(PackingList.class);
                packingLists.add(packingList);

                if (packingList.isActive() == true) {
                    currentPackingList = packingList;
                    currentListItems = packingList.getItems();
                    MainActivity.toolbar.setTitle(currentPackingList.getTitle());
                }
                adapter.notifyDataSetChanged();

                Log.d(LOG_TAG, "onChildAdded");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                PackingList packingList = dataSnapshot.getValue(PackingList.class);
                for (int i = 0; i < packingLists.size(); i++) {
                    PackingList matchList = packingLists.get(i);
                    if (matchList.getTitle() == packingList.getTitle()) {
                        packingLists.set(i, packingList);
                    }
                }
                adapter.notifyDataSetChanged();
                Log.d(LOG_TAG, "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                PackingList removedList = dataSnapshot.getValue(PackingList.class);
//                for (int i = 0; i < packingLists.size(); i++) {
//                    PackingList list = packingLists.get(i);
//                    if (list.getTitle().equals(removedList.getTitle())) {
//
//                    }
//                }
                adapter.notifyDataSetChanged();
                SavedListsFragment.adapter.notifyDataSetChanged();
                Log.d(LOG_TAG, "onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
                Log.d(LOG_TAG, "onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, "onCancelled");
            }
        });
    }

    public class PackingListHolder extends RecyclerView.ViewHolder {

        private PackingListItem listItem;

        private CheckBox checkBox;
        private TextView title;
        private TextView subtitle;
        private TextView quantity;

        public PackingListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.packing_list_item, parent, false));

            checkBox = (CheckBox) itemView.findViewById(R.id.list_item_checkbox);
            title = (TextView) itemView.findViewById(R.id.list_item_title);
            subtitle = (TextView) itemView.findViewById(R.id.list_item_subtitle);
            quantity = (TextView) itemView.findViewById(R.id.list_item_quantity);
        }

        public void bind(PackingListItem packingListItem) {
            listItem = packingListItem;
        }
    }

    public class PackingListAdapter extends RecyclerView.Adapter<PackingListHolder> {

        public PackingListAdapter(ArrayList<PackingListItem> listItems) {
            PackingListFragment.currentListItems = listItems;
        }

        @Override
        public PackingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PackingListHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PackingListHolder holder, final int position) {

            final PackingListItem listItem = PackingListFragment.currentListItems.get(position);

            holder.title.setText(listItem.getTitle());
            holder.subtitle.setText(listItem.getSubtitle());
            holder.quantity.setText("x" + listItem.getQuantity());
            System.out.println("Checked " + position + " " + listItem.getIsChecked());
            holder.checkBox.setChecked(listItem.getIsChecked());

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < packingLists.size(); i++) {
                        if (packingLists.get(i).getTitle() == currentPackingList.getTitle()) {
                            PackingList list = packingLists.get(i);
                            PackingListItem item = list.getItems().get(position);
                            item.setIsChecked(!item.getIsChecked());
//                            updateFirebase();
                            return;
                        }
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Bundle itemBundle = new Bundle();
                    itemBundle.putParcelable("listItem", listItem);
                    DialogFragment dialogFragment = new EditItemDialogFragment();
                    dialogFragment.setArguments(itemBundle);
                    dialogFragment.show(getFragmentManager(), "editListItem");
                }
            });
        }

        @Override
        public int getItemCount() { return PackingListFragment.currentListItems.size(); }
    }
}
