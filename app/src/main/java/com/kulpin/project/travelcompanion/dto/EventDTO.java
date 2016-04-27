package com.kulpin.project.travelcompanion.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Andrei on 07.04.2016.
 */
public class EventDTO implements Parcelable{

    private long id;
    private String title;
    private String place;
    private Date eventDate;
    private float distance;


    public EventDTO(){}

    public EventDTO(String title, String place, Date eventDate, float distance) {
        this.title = title;
        this.place = place;
        this.eventDate = eventDate;
        this.distance = distance;
    }

    private EventDTO (Parcel parcel){
        id = parcel.readLong();
        title = parcel.readString();
        place = parcel.readString();
        eventDate = new Date(parcel.readLong());
        distance = parcel.readFloat();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(place);
        dest.writeLong(eventDate.getTime());
        dest.writeFloat(distance);
    }

    public static final Parcelable.Creator<EventDTO> CREATOR = new Parcelable.Creator<EventDTO>(){

        @Override
        public EventDTO createFromParcel(Parcel source) {
            return new EventDTO(source);
        }

        @Override
        public EventDTO[] newArray(int size) {
            return new EventDTO[size];
        }
    };
}
