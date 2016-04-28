package com.atti.atti_android.data;

import android.util.Log;

/**
 * Created by BoWoon on 2016-04-28.
 */
public class HttpConnect {
    public void getData() {
        String result = null;
        DataGetThread data = new DataGetThread();
        data.execute();

        if (result != null) {
            Log.i("Data Get", "Success");
        }
        else {
            Log.i("Data Get", "Fail");
        }
    }
}
