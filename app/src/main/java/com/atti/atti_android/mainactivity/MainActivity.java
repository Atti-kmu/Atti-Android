package com.atti.atti_android.mainactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.atti.atti_android.R;
import com.atti.atti_android.data.UsersDataManager;
import com.atti.atti_android.list.ElderlyList;
import com.atti.atti_android.list.FamilyList;
import com.atti.atti_android.list.SocialWorkerList;
import com.atti.atti_android.person.ElderlyPerson;
import com.atti.atti_android.person.Family;
import com.atti.atti_android.person.SocialWorker;
import com.atti.atti_android.playrtc.PlayRTCDisplay;

public class MainActivity extends Activity {
    private UsersDataManager users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init() {
        findViewById(R.id.btn_family).setOnClickListener(listener);
        findViewById(R.id.btn_friend).setOnClickListener(listener);
        findViewById(R.id.btn_social_worker).setOnClickListener(listener);

        users = UsersDataManager.getUsersInstance();

        users.addData(new ElderlyPerson("김씨", "010-1234-5678", "김씨"));
        users.addData(new ElderlyPerson("이씨", "010-1234-5678", "이씨"));
        users.addData(new ElderlyPerson("박씨", "010-1234-5678", "박씨"));

        users.addData(new Family("최씨", "010-4321-8765", "최씨"));
        users.addData(new Family("강씨", "010-4321-8765", "강씨"));
        users.addData(new Family("조씨", "010-4321-8765", "조씨"));

        users.addData(new SocialWorker("송씨", "010-8765-4321"));
        users.addData(new SocialWorker("남씨", "010-8765-4321"));
        users.addData(new SocialWorker("진씨", "010-8765-4321"));
    }

    Button.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_family:
                    Intent familyList = new Intent(MainActivity.this, FamilyList.class);
                    startActivity(familyList);
                    break;
                case R.id.btn_friend:
                    Intent friendList = new Intent(MainActivity.this, ElderlyList.class);
                    startActivity(friendList);
                    break;
                case R.id.btn_social_worker:
                    Intent socialWorkerList = new Intent(MainActivity.this, SocialWorkerList.class);
                    startActivity(socialWorkerList);
                    break;
                default:
                    break;
            }
        }
    };
}
