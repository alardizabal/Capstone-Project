package com.albertlardizabal.packoverflow.ui;

import android.graphics.Color;
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
import com.albertlardizabal.packoverflow.helpers.Utils;
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
    private PackingListAdapter adapter;

    public static String currentPackingListTitle;
    public static ArrayList<PackingList> packingLists = new ArrayList<>();
    public static ArrayList<PackingListItem> currentListItems = new ArrayList<>();

    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final DatabaseReference rootReference = firebaseDatabase.getReference();
    private static final DatabaseReference savedListsReference = rootReference.child("saved_lists");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_packing_list, container, false);
        view.setBackgroundColor(Color.WHITE);

        recyclerView = (RecyclerView) view.findViewById(R.id.packing_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new PackingListAdapter(currentListItems);
        recyclerView.setAdapter(adapter);

        ArrayList<PackingList> savedLists = Utils.stageData();
        savedListsReference.setValue(savedLists);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
                    currentListItems = packingList.getItems();
                }

                adapter.notifyDataSetChanged();

                Log.d(LOG_TAG, "onChildAdded");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, "onCancelled");
            }
        });
    }

    public class PackingListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            itemView.setOnClickListener(this);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(LOG_TAG, "tapped");
                }
            });
        }

        public void bind(PackingListItem packingListItem) {
            listItem = packingListItem;
        }

        @Override
        public void onClick(View v) {

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
        public void onBindViewHolder(PackingListHolder holder, int position) {
            PackingListItem listItem = PackingListFragment.currentListItems.get(position);
            holder.title.setText(listItem.getTitle());
            holder.subtitle.setText(listItem.getSubtitle());
            holder.quantity.setText(listItem.getQuantity());
        }

        @Override
        public int getItemCount() { return PackingListFragment.currentListItems.size(); }
    }
}
