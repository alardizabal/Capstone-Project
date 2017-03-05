package com.albertlardizabal.packoverflow;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.albertlardizabal.packoverflow.models.PackingListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albertlardizabal on 2/25/17.
 */

public class PackingListFragment extends Fragment {

    private RecyclerView recyclerView;
    private PackingListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_packing_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.packing_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        List<PackingListItem> listItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            PackingListItem item = new PackingListItem();
            item.setTitle("Title");
            item.setSubtitle("Subtitle");
            item.setCount(7);
            listItems.add(item);
        }
        adapter = new PackingListAdapter(listItems);
        recyclerView.setAdapter(adapter);
    }

    public class PackingListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PackingListItem listItem;

        private CheckBox checkBox;
        private TextView title;
        private TextView subtitle;
        private TextView count;

        public PackingListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.packing_list_item, parent, false));

            checkBox = (CheckBox) itemView.findViewById(R.id.list_item_checkbox);
            title = (TextView) itemView.findViewById(R.id.list_item_title);
            subtitle = (TextView) itemView.findViewById(R.id.list_item_subtitle);
            count = (TextView) itemView.findViewById(R.id.list_item_count);

            itemView.setOnClickListener(this);
        }

        public void bind(PackingListItem packingListItem) {
            listItem = packingListItem;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class PackingListAdapter extends RecyclerView.Adapter<PackingListHolder> {

        private List<PackingListItem> listItems;

        public PackingListAdapter(List<PackingListItem> listItems) { this.listItems = listItems; }

        @Override
        public PackingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PackingListHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PackingListHolder holder, int position) {
            PackingListItem listItem = listItems.get(position);
            holder.bind(listItem);
        }

        @Override
        public int getItemCount() { return listItems.size(); }
    }
}
