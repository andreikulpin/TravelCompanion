package com.kulpin.project.travelcompanion.dto;


import android.os.Parcel;
import android.os.Parcelable;

public class Link implements Parcelable{

    private long id;
    private long eventId;
    private String title;
    private String address;

    public Link() {
    }

    public Link(Parcel parcel){
        id = parcel.readLong();
        eventId = parcel.readLong();
        title = parcel.readString();
        address = parcel.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(eventId);
        dest.writeString(title);
        dest.writeString(address);
    }

    public static final Parcelable.Creator<Link> CREATOR = new Creator<Link>() {
        @Override
        public Link createFromParcel(Parcel source) {
            return new Link(source);
        }

        @Override
        public Link[] newArray(int size) {
            return new Link[size];
        }
    };
}
