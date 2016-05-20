package com.atti.atti_android.data;

import com.atti.atti_android.person.CommonPerson;
import com.atti.atti_android.person.ElderlyPerson;
import com.atti.atti_android.person.Family;
import com.atti.atti_android.person.SocialWorker;

import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-03-28.
 */
public class UsersDataManager {
    private ArrayList<ElderlyPerson> elderly;
    private ArrayList<Family> families;
    private ArrayList<SocialWorker> socialWorkers;
    private static UsersDataManager users = new UsersDataManager();
    public static boolean connection = false;

    public UsersDataManager() {
        elderly = new ArrayList<ElderlyPerson>();
        families = new ArrayList<Family>();
        socialWorkers = new ArrayList<SocialWorker>();
    }

    public static UsersDataManager getUsersInstance() {
        if (users != null)
            return users;
        return new UsersDataManager();
    }

    public ArrayList<ElderlyPerson> getElderly() {
        return elderly;
    }

    public ArrayList<Family> getFamilies() {
        return families;
    }

    public ArrayList<SocialWorker> getSocialWorkers() {
        return socialWorkers;
    }

    public boolean addData(CommonPerson com) {
        if (com instanceof ElderlyPerson) {
            elderly.add((ElderlyPerson) com);
        } else if (com instanceof Family) {
            families.add((Family) com);
        } else if (com instanceof SocialWorker) {
            socialWorkers.add((SocialWorker) com);
        } else
            return false;

        return true;
    }
}
