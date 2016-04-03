package com.atti.atti_android.person;

/**
 * Created by LG on 2016-03-28.
 */
public class CommonPerson {
    private String name;
    private String phoneNumber;
    private String nickName;
    //photo

    public CommonPerson() {

    }

    public CommonPerson(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public CommonPerson(String name, String phoneNumber, String nickName) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
