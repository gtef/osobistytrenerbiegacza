package com.example.jakub.osobistytrenerbiegacza.model;

import java.io.Serializable;

/**
 * Created by Jakub on 2015-10-22.
 */
public class Advice implements Serializable {
    private int id;

    private int Category;

    private String text;

    public Advice(int id, int category, String text) {
        this.id = id;
        Category = category;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Advice{" +
                "id=" + id +
                ", Category=" + Category +
                ", text='" + text + '\'' +
                '}';
    }
}
