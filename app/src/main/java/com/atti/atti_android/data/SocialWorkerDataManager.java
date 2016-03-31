package com.atti.atti_android.data;

import com.atti.atti_android.person.SocialWorker;

import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-03-28.
 */
public class SocialWorkerDataManager {
    private ArrayList<SocialWorker> users;

    public SocialWorkerDataManager() {
        users = new ArrayList<SocialWorker>();
    }

    public ArrayList<SocialWorker> getUsers() {
        return users;
    }
}
