package com.jakomulski.datacollector.models;


/**
 * Created by Adam on 16.02.2017.
 */

public class User {
    private long id;
    private String name;
    private String lastname;
    private String birthDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }


    public String toLongString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return name + '\t' + lastname + '\t' +birthDate;
    }
}
