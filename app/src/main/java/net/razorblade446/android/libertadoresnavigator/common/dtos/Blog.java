package net.razorblade446.android.libertadoresnavigator.common.dtos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Blog {
    private String title;
    private String description;
    private String link;
    private Date pubDate;

    public Blog (String title, String description, String link, String pubDate) {
        SimpleDateFormat parser = new SimpleDateFormat("EE, dd MM yyyy HH:mm:ss Z", Locale.US);

        this.title = title;
        this.description = description;
        this.link = link;
        try {
            this.pubDate = parser.parse(pubDate);
        }catch (Exception ex) {
            this.pubDate = null;
        }
    }

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

    public String getPubDate () {
        if (pubDate == null) {
            return "Sin Fecha";
        } else {
            return pubDate.toString();
        }
    }

    public void setPubDate (String date) {
        SimpleDateFormat parser = new SimpleDateFormat("EE, dd MM yyyy HH:mm:ss Z", Locale.US);

        try {
            this.pubDate = parser.parse(date);
        }catch (Exception ex) {
            this.pubDate = null;
        }
    }
}
