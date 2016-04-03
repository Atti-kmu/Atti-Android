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

    public UsersDataManager() {
        elderly = new ArrayList<ElderlyPerson>();
        families = new ArrayList<Family>();
        socialWorkers = new ArrayList<SocialWorker>();
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

    public boolean removeData(CommonPerson com, int index) {
        if (com instanceof ElderlyPerson) {
            elderly.remove(index);
        } else if (com instanceof Family) {
            families.remove(index);
        } else if (com instanceof SocialWorker) {
            socialWorkers.remove(index);
        } else
            return false;

        return true;
    }
}
