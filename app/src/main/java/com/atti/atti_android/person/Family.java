package com.atti.atti_android.person;

/**
 * Created by LG on 2016-03-28.
 */
public class Family extends CommonPerson {
    private String nickName;
    public Family() {

    }

    public Family(String name, String phoneNumber, String nickName) {
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
