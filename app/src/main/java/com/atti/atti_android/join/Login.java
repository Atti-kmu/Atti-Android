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
public class Login extends Activity {
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        aq = new AQuery(this);

        aq.id(R.id.login_submit).clicked(loginSubmit);
    }

    public boolean loginChecked() {
        String id = aq.id(R.id.join_id_edit).getText().toString();
        String password = aq.id(R.id.join_password_edit).getText().toString();

        if (id.equals("") || password.equals(""))
            return false;

        return true;
    }

    Button.OnClickListener loginSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.join_submit:
                    if (loginChecked()) {
                        String id = aq.id(R.id.join_id_edit).getText().toString();
                        String password = aq.id(R.id.join_password_edit).getText().toString();

                        ArrayList<BasicNameValuePair> loginPair = new ArrayList<BasicNameValuePair>();
                        loginPair.add(new BasicNameValuePair("login", "login"));
                        loginPair.add(new BasicNameValuePair("id", id));
                        loginPair.add(new BasicNameValuePair("password", password));
                        new DataPostThread().execute(loginPair);
                    } else {
                        Toast.makeText(getApplicationContext(), "정보를 제대로 입력하세요!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.join_reject:
                    break;
                default:
                    break;
            }

            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    };
}
