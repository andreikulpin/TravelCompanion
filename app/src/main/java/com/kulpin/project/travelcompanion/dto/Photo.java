package com.kulpin.project.travelcompanion.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Andrei on 04.05.2016.
 */
public class Photo implements Parcelable {
    private long id;
    private long eventId;
    private long journeyId;
    private String title;
    private String path;


    public Photo() {
    }

    public Photo(long eventId, String title, String path) {
        this.eventId = eventId;
        this.title = title;
        this.path = path;
    }

    public Photo(Parcel parcel){
        id = parcel.readLong();
        eventId = parcel.readLong();
        title = parcel.readString();
        path = parcel.readString();

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        dest.writeString(path);
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {

        @Override
        public Photo createFromParcel(Parcel source) {
            return  new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}