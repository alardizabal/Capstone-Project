package com.albertlardizabal.packoverflow.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 3/5/17.
 */

public class PackingList implements Parcelable {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("is_checked")
    @Expose
    private boolean isChecked;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("items")
    @Expose
    private ArrayList<PackingListItem> items;
    @SerializedName("is_active")
    @Expose
    private boolean isActive;

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
     * The is item checked flag
     */
    public boolean getIsChecked() {
        return isChecked;
    }

    /**
     *
     * @param isChecked
     * The is item checked flag
     */
    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
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
    public ArrayList<PackingListItem> getItems() {
        return items;
    }

    /**
     *
     * @param items
     * The items
     */
    public void setItems(ArrayList<PackingListItem> items) {
        this.items = items;
    }

    /**
     *
     * @return
     * The flag for the active list
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     * The flag for the active list
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(title);
        dest.writeList(items);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }

    public static final Parcelable.Creator<PackingListItem> CREATOR = new Parcelable.Creator<PackingListItem>() {

        @Override
        public PackingListItem createFromParcel(Parcel source) { return null; }

        @Override
        public PackingListItem[] newArray(int size) { return new PackingListItem[size]; }
    };
}
