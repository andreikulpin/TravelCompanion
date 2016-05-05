package com.kulpin.project.travelcompanion.dto;


import android.os.Parcel;
import android.os.Parcelable;

public class Document implements Parcelable{

    private long id;
    private long eventId;
    private long journeyId;
    private String title;
    private String filePath;

    public Document() {
    }

    public Document(long eventId, String title, String filePath) {
        this.eventId = eventId;
        this.title = title;
        this.filePath = filePath;
    }

    public Document(Parcel parcel){
        this.id = parcel.readLong();
        this.eventId = parcel.readLong();
        this.title = parcel.readString();
        this.filePath = parcel.readString();
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
        dest.writeString(filePath);
    }

    public static final Parcelable.Creator<Document> CREATOR = new Creator<Document>() {
        @Override
        public Document createFromParcel(Parcel source) {
            return new Document(source);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };
}
