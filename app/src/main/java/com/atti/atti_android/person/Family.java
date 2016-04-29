package com.atti.atti_android.person;

import android.graphics.Bitmap;

/**
 * Created by LG on 2016-03-28.
 */
public class Family extends CommonPerson {
    private String nickName;

    public Family() {

    }

    public Family(String name, String nickName) {
        super(name);
        this.nickName = nickName;
    }

    public Family(String name, String nickName, String profileImg) {
        super(name, profileImg);
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
