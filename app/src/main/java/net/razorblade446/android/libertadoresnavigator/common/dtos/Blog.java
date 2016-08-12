package net.razorblade446.android.libertadoresnavigator.common.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Blog implements Parcelable {
    private String title;
    private String description;
    private String link;
    private String pubDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public static final Parcelable.Creator<Blog> CREATOR = new Parcelable.Creator<Blog>() {

        @Override
        public Blog createFromParcel(Parcel parcel) {
            return new Blog(parcel);
        }

        @Override
        public Blog[] newArray(int i) {
            return new Blog[0];
        }
    };

    public Blog() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeString(pubDate);
    }

    private Blog(Parcel parcel) {
        this.title = parcel.readString();
        this.description = parcel.readString();
        this.link = parcel.readString();
        this.pubDate = parcel.readString();
    }
}
