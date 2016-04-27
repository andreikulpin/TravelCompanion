package com.kulpin.project.travelcompanion.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class JourneyDTO implements Parcelable{

    private long id;
    private String title;
    private Date startDate;
    private Date endDate;
    private List<EventDTO> eventList;

    public JourneyDTO() {}

    public JourneyDTO(String title, Date startDate, Date endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private JourneyDTO(Parcel parcel){
        id = parcel.readLong();
        title = parcel.readString();
        startDate = new Date(parcel.readLong());
        endDate = new Date(parcel.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeLong(startDate.getTime());
        dest.writeLong(endDate.getTime());
    }

    public static final Parcelable.Creator<JourneyDTO> CREATOR = new Parcelable.Creator<JourneyDTO>(){

        @Override
        public JourneyDTO createFromParcel(Parcel source) {
            return new JourneyDTO(source);
        }

        @Override
        public JourneyDTO[] newArray(int size) {
            return new JourneyDTO[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<EventDTO> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventDTO> eventList) {
        this.eventList = eventList;
    }
}
