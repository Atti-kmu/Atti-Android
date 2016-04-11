package com.atti.atti_android.person;

/**
 * Created by LG on 2016-03-28.
 */
public class ElderlyPerson extends CommonPerson {
    private String nickName;

    public ElderlyPerson() {

    }

    public ElderlyPerson(String name, String phoneNumber, String nickName) {
        super(name, phoneNumber);
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
