package com.albertlardizabal.packoverflow.ui;

import android.graphics.Color;
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
import com.albertlardizabal.packoverflow.models.PackingList;
import com.albertlardizabal.packoverflow.models.PackingListItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albertlardizabal on 2/26/17.
 */

public class SavedListsFragment extends Fragment {

    private static final String LOG_TAG = SavedListsFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private SavedListsAdapter adapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference rootReference = firebaseDatabase.getReference();
    private DatabaseReference savedListsReference = rootReference.child("saved_lists");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_packing_list, container, false);
        view.setBackgroundColor(Color.GREEN);

        recyclerView = (RecyclerView) view.findViewById(R.id.packing_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        stageData();

        return view;
    }

    private void stageData() {
        PackingListItem packingListItem = new PackingListItem();
        packingListItem.setTitle("Shirt");
        packingListItem.setSubtitle("Apples");
        packingListItem.setQuantity(1);

        PackingListItem packingListItem2 = new PackingListItem();
        packingListItem.setTitle("Pants");
        packingListItem.setSubtitle("Bananas");
        packingListItem.setQuantity(2);

        PackingListItem packingListItem3 = new PackingListItem();
        packingListItem.setTitle("Shoes");
        packingListItem.setSubtitle("Coconuts");
        packingListItem.setQuantity(3);

//        PackingList packingList = new PackingList();
//        packingList.setTitle("Overnight");
//        packingList.addItem(packingListItem);
//        packingList.addItem(packingListItem2);
//        packingList.addItem(packingListItem3);

        savedListsReference.push().setValue(packingListItem);
    }

    @Override
    public void onStart() {
        super.onStart();
        savedListsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                List<PackingList> packingListItems = (List) dataSnapshot.getChildren();
                System.out.print("Hello");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI() {
        List<PackingList> listItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            PackingList item = new PackingList();
            item.setTitle("Title");
            listItems.add(item);
        }
        adapter = new SavedListsAdapter(listItems);
        recyclerView.setAdapter(adapter);
    }

    public class SavedListsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PackingList listItem;
        private TextView title;

        public SavedListsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.saved_list_item, parent, false));

            title = (TextView) itemView.findViewById(R.id.saved_list_item_title);

            itemView.setOnClickListener(this);
        }

        public void bind(PackingList packingList) {
            listItem = packingList;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class SavedListsAdapter extends RecyclerView.Adapter<SavedListsHolder> {

        private List<PackingList> listItems;

        public SavedListsAdapter(List<PackingList> listItems) { this.listItems = listItems; }

        @Override
        public SavedListsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SavedListsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SavedListsHolder holder, int position) {
            PackingList listItem = listItems.get(position);
            holder.bind(listItem);
        }

        @Override
        public int getItemCount() { return listItems.size(); }
    }
}
