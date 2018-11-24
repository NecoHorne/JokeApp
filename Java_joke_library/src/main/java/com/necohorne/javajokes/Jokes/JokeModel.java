package com.necohorne.javajokes.Jokes;

import java.util.ArrayList;

public class JokeModel {

    private String id;
    private String joke;
    private ArrayList<String> categories;

    public JokeModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
