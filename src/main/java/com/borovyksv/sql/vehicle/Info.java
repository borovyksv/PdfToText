package com.borovyksv.sql.vehicle;

import org.springframework.data.mongodb.core.index.Indexed;

public class Info {
    @Indexed
    public String title;
    @Indexed
    public String[] options;

    public Info(String title, String[] options) {
        this.title = title;
        this.options = options;
    }

    public Info() {
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    @Override
    public String toString() {
        return "Info{" +
                "title='" + title + '\'' +
                ", options='" + options + '\'' +
                '}';
    }
}
