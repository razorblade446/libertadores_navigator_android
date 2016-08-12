package net.razorblade446.android.libertadoresnavigator.common.dtos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fredericpena on 11/08/16.
 */
public class Schedule implements Parcelable {
    public String course;
    public String group;
    public String credits;
    public String classroom;
    public String time;
    public String day;
    public String location;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(course);
        parcel.writeString(group);
        parcel.writeString(credits);
        parcel.writeString(classroom);
        parcel.writeString(time);
        parcel.writeString(day);
        parcel.writeString(location);
    }

    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {

        @Override
        public Schedule createFromParcel(Parcel parcel) {
            return new Schedule(parcel);
        }

        @Override
        public Schedule[] newArray(int i) {
            return new Schedule[0];
        }
    };

    public Schedule() {
    }

    private Schedule(Parcel parcel) {
        course = parcel.readString();
        group = parcel.readString();
        credits = parcel.readString();
        classroom = parcel.readString();
        time = parcel.readString();
        day = parcel.readString();
        location = parcel.readString();
    }
}
