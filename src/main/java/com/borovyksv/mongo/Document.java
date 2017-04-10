package com.borovyksv.mongo;

import org.springframework.data.annotation.Id;

import java.util.List;


public class Document {

    @Id
    public String id;

    public String name;
    public List<Page> pages;

    public Document(List<Page> pages, String name) {
        this.pages = pages;
        this.name = name;
    }

    public Document() {

    }

    @Override
    public String toString() {
        return "Document{" +
                "name= '" + name + '\'' +
                ", pages: " + pages.size() +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }


}

