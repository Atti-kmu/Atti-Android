package com.atti.atti_android.list;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.atti.atti_android.R;
import com.atti.atti_android.data.UsersDataManager;
import com.atti.atti_android.playrtc.PlayRTCDisplay;

/**
 * Created by 보운 on 2016-04-03.
 */
public class FamilyList extends Fragment {
    private ListView userList;
    private UserListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.list_view_layout, container, false);
        Log.i("ListSize", String.valueOf(UsersDataManager.getUsersInstance().getFamilies().size()));
        adapter = new UserListAdapter(getActivity(), UsersDataManager.getUsersInstance().getFamilies());
        userList = (ListView) root.findViewById(R.id.list_view_display);
        userList.setAdapter(adapter);

        userList.setOnItemClickListener(itemClickListener);
        return root;
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent playRTC = new Intent(getActivity(), PlayRTCDisplay.class);
            playRTC.putExtra("connect", 0);
            playRTC.putExtra("connectID", UsersDataManager.getUsersInstance().getFamilies().get(position).getId());
            startActivity(playRTC);
        }
    };
}
