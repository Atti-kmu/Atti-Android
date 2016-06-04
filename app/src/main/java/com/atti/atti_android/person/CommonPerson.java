package com.atti.atti_android.person;

/**
 * Created by LG on 2016-03-28.
 */
public class CommonPerson {
    private String name;
    private String profileImg;
    private String id;

    public CommonPerson() {

    }

    public CommonPerson(String name) {
        this.name = name;
    }

    public CommonPerson(String id, String name, String profileImg) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
