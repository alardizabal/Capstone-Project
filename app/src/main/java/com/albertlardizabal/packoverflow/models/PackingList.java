package com.albertlardizabal.packoverflow.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by albertlardizabal on 3/5/17.
 */

public class PackingList implements Parcelable {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("items")
    @Expose
    private List<PackingList> items;

    /**
     *
     * @return
     * The uid
     */
    public String getUid() {
        return uid;
    }

    /**
     *
     * @param uid
     * The uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The items
     */
    public List getItems() {
        return items;
    }

    /**
     *
     * @param items
     * The items
     */
    public void setItems(List<PackingList> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(title);
        dest.writeList(items);
    }

    public static final Parcelable.Creator<PackingListItem> CREATOR = new Parcelable.Creator<PackingListItem>() {

        @Override
        public PackingListItem createFromParcel(Parcel source) { return null; }

        @Override
        public PackingListItem[] newArray(int size) { return new PackingListItem[size]; }
    };
}
