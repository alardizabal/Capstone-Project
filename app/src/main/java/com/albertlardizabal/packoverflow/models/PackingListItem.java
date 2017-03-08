package com.albertlardizabal.packoverflow.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by albertlardizabal on 2/25/17.
 */

public class PackingListItem implements Parcelable {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("is_checked")
    @Expose
    private boolean isChecked;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("bag_type")
    @Expose
    private String bagType;
    @SerializedName("count")
    @Expose
    private int count;

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
     * The subtitle
     */
    public String getSubtitle() { return subtitle; }

    /**
     *
     * @param subtitle
     * The subtitle
     */
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    /**
     *
     * @return
     * The bagType
     */
    public String getBagType() { return bagType; }

    /**
     *
     * @param bagType
     * The bagType
     */
    public void setBagType(String bagType) { this.bagType = bagType; }

    /**
     *
     * @return
     * The count
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(bagType);
        dest.writeInt(count);
    }

    public static final Parcelable.Creator<PackingListItem> CREATOR = new Parcelable.Creator<PackingListItem>() {

        @Override
        public PackingListItem createFromParcel(Parcel source) { return null; }

        @Override
        public PackingListItem[] newArray(int size) { return new PackingListItem[size]; }
    };
}
