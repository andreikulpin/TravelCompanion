package com.kulpin.project.travelcompanion.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class EventDTO implements Parcelable{

    private long id;
    private long journeyId;
    private int type;
    private String title;
    private String place;
    private String departurePlace;
    private String destinationPlace;
    //private Date eventDate;
    private Date startDate;
    private Date startTime;
    private Date endDate;
    private Date endTime;


    public EventDTO(){}



    private EventDTO (Parcel parcel){
        id = parcel.readLong();
        type = parcel.readInt();
        title = parcel.readString();
        place = parcel.readString();
        departurePlace = parcel.readString();
        destinationPlace = parcel.readString();
        startDate = new Date(parcel.readLong());
        startTime = new Date(parcel.readLong());
        endDate = new Date(parcel.readLong());
        endTime = new Date(parcel.readLong());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getDeparturePlace() {
        return departurePlace;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public String getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(String destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(type);
        dest.writeString(title);
        dest.writeString(place);
        dest.writeString(departurePlace);
        dest.writeString(destinationPlace);
        dest.writeLong(startDate.getTime());
        dest.writeLong(startTime.getTime());
        dest.writeLong(endDate.getTime());
        dest.writeLong(endTime.getTime());
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
