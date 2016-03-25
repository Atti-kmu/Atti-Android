package com.atti.atti_android.person;

/**
 * Created by 보운 on 2016-03-25.
 */
public class User {
    private String name;

    public User() {
        name = "BoWoon Kim";
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
