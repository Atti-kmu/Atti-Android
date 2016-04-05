package com.atti.atti_android.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.atti.atti_android.R;
import com.atti.atti_android.data.UsersDataManager;
import com.atti.atti_android.playrtc.PlayRTCDisplay;

/**
 * Created by 보운 on 2016-04-03.
 */
public class ElderlyList extends Activity {
    private ListView userList;
    private UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_layout);

        // Adapter 생성
        adapter = new UserListAdapter(getApplicationContext(), UsersDataManager.getUsersInstance().getElderly());
        // 리스트뷰 참조 및 Adapter달기
        userList = (ListView) findViewById(R.id.list_view_display);
        userList.setAdapter(adapter);

        userList.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(ElderlyList.this, "Click Item : " + position, Toast.LENGTH_SHORT).show();
            Intent playRTC = new Intent(ElderlyList.this, PlayRTCDisplay.class);
            startActivity(playRTC);
        }
    };
}
