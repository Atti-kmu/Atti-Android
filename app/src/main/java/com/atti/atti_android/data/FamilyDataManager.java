package com.atti.atti_android.data;

import com.atti.atti_android.person.Family;

import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-03-28.
 */
public class FamilyDataManager {
    private ArrayList<Family> users;

    public FamilyDataManager() {
        users = new ArrayList<Family>();
    }

    public ArrayList<Family> getUsers() {
        return users;
    }
}
