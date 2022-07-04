package com.hero.seoultechteams.database.todo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringDef;

public class Event implements Parcelable {

    public static final String EVENT_CREATE = "create";
    public static final String EVENT_SUBMIT = "submit";
    public static final String EVENT_DISMISS = "dismiss";
    public static final String EVENT_CONFIRM = "confirm";

    @StringDef({EVENT_CREATE, EVENT_SUBMIT, EVENT_DISMISS, EVENT_CONFIRM})
    public @interface TodoEvent {};

    @TodoEvent
    private String event;
    private long time;

    public Event() {
    }

    protected Event(Parcel in) {
        event = in.readString();
        time = in.readLong();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @TodoEvent
    public String getEvent() {
        return event;
    }

    public void setEvent(@TodoEvent String event) {
        this.event = event;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(event);
        dest.writeLong(time);
    }
}