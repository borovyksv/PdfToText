package com.borovyksv.mongo.pojo;

import org.springframework.data.annotation.Id;

import java.util.List;


public class DocumentWithTextPages {

    @Id
    public String id;

    public String name;
    public String vendor;
    public String type;
    public List<String> model;
    public List<Integer> year;
    public List<String> options;
    public List<Page> bookmarks;
    public List<Page> pages;
    public DocumentWithTextPages(List<Page> pages, String name) {
        this.pages = pages;
        this.name = name;
    }
    public DocumentWithTextPages() {

    }

    public DocumentWithTextPages(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public List<String> getModel() {
        return model;
    }

    public void setModel(List<String> model) {
        this.model = model;
    }

    public List<Integer> getYear() {
        return year;
    }

    public void setYear(List<Integer> year) {
        this.year = year;
    }

    public List<Page> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Page> bookmarks) {
        this.bookmarks = bookmarks;
    }

    @Override
    public String toString() {
        return "DocumentWithTextPages{" +
                "options=" + options +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", vendor='" + vendor + '\'' +
                ", type='" + type + '\'' +
                ", model=" + model +
                ", year=" + year +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentWithTextPages document = (DocumentWithTextPages) o;

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

