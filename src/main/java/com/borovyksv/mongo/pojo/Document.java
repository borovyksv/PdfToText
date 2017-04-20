package com.borovyksv.mongo.pojo;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        if (!name.equals(document.name)) return false;
        return pages.equals(document.pages);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + pages.hashCode();
        return result;
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

