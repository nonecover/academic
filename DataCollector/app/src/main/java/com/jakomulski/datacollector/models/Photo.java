package com.jakomulski.datacollector.models;

/**
 * Created by Adam on 16.02.2017.
 */

public class Photo {
    private long id;
    private long user_id;
    private String uri;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Photo: {" +
                "id=" + id +
                '}';
    }
}
