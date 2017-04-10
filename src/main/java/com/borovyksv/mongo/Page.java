package com.borovyksv.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Page {
    @Id
    public Integer id;
    @Indexed
    public String text;

    public Page(Integer id, String text) {

        this.id = id;
        this.text = text;
    }

    public Page() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
