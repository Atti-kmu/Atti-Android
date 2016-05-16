package com.atti.atti_android.join;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.atti.atti_android.R;
import com.atti.atti_android.data.DataPostThread;
import com.atti.atti_android.mainactivity.MainActivity;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-05-16.
 */
public class Join extends Activity {
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_layout);
        aq = new AQuery(this);

        aq.id(R.id.join_submit).clicked(joinSubmit);
        aq.id(R.id.join_reject).clicked(joinSubmit);
    }

    public boolean joinChecked() {
        String id = aq.id(R.id.join_id_edit).getText().toString();
        String password = aq.id(R.id.join_password_edit).getText().toString();
        String nickname = aq.id(R.id.join_nickname_edit).getText().toString();

        if (id.equals("") || password.equals("") || nickname.equals(""))
            return false;

        return true;
    }

    Button.OnClickListener joinSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.join_submit:
                    if (joinChecked()) {
                        String id = aq.id(R.id.join_id_edit).getText().toString();
                        String password = aq.id(R.id.join_password_edit).getText().toString();
                        String nickname = aq.id(R.id.join_nickname_edit).getText().toString();

                        ArrayList<BasicNameValuePair> joinPair = new ArrayList<BasicNameValuePair>();
                        joinPair.add(new BasicNameValuePair("join", "join"));
                        joinPair.add(new BasicNameValuePair("id", id));
                        joinPair.add(new BasicNameValuePair("password", password));
                        joinPair.add(new BasicNameValuePair("nickname", nickname));
                        new DataPostThread().execute(joinPair);
                    } else {
                        Toast.makeText(getApplicationContext(), "정보를 제대로 입력하세요!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.join_reject:
                    break;
                default:
                    break;
            }

            startActivity(new Intent(Join.this, MainActivity.class));
            finish();
        }
    };
}
