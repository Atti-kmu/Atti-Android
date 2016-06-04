package com.atti.atti_android.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.atti.atti_android.R;
import com.atti.atti_android.person.CommonPerson;

import java.util.ArrayList;

/**
 * Created by 보운 on 2016-03-25.
 */
public class UserListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<? extends CommonPerson> user;
    private AQuery aq;

    public UserListAdapter(Context context, ArrayList<? extends CommonPerson> com) {
        this.context = context;
        this.user = com;
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

        aq = new AQuery(v);
        aq.id(R.id.name).text(user.get(position).getName());
        aq.id(R.id.profile_img).image(user.get(position).getProfileImg());
        return v;
    }
}
