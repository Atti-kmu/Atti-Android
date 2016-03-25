package com.atti.atti_android.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atti.atti_android.R;
import com.atti.atti_android.person.User;

import java.util.ArrayList;

/**
 * Created by 보운 on 2016-03-25.
 */
public class UserListAdapter extends BaseAdapter {
    private User u;
    private Context context;
    private ImageView img;
    private TextView text;
    private ArrayList<User> user;

    public UserListAdapter(Context context) {
        this.context = context;
        user = new ArrayList<User>();
    }

    @Override
    public int getCount() {
        return user.size();
    }

    @Override
    public Object getItem(int position) {
        return user.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null)
            v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_view_item, null);

        u = (User) getItem(position);
        img = (ImageView) v.findViewById(R.id.profile_img);
        text = (TextView) v.findViewById(R.id.name);
        text.setText(u.getName());

        return v;
    }

    public void add(User user){
        this.user.add(user);
    }
}
