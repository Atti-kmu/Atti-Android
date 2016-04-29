package com.atti.atti_android.person;

/**
 * Created by LG on 2016-03-28.
 */
public class CommonPerson {
    private String name;
    private String profileImg;

    public CommonPerson() {

    }

    public CommonPerson(String name) {
        this.name = name;
    }

    public CommonPerson(String name, String profileImg) {
        this.name = name;
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
