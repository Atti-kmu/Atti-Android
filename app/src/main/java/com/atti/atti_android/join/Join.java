package com.atti.atti_android.join;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.atti.atti_android.R;
import com.atti.atti_android.data.DataPutThread;
import com.atti.atti_android.mainactivity.MainActivity;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by BoWoon on 2016-05-16.
 */
public class Join extends Activity {
    private AQuery aq;
    private int gender, kind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_layout);
        aq = new AQuery(this);

        gender = kind = -1;

        aq.id(R.id.join_submit).clicked(joinSubmit);
        aq.id(R.id.join_reject).clicked(joinSubmit);
        RadioGroup rgGender = (RadioGroup) findViewById(R.id.radio_group_gender);
        rgGender.setOnCheckedChangeListener(radioListener);
        RadioGroup rgKind = (RadioGroup) findViewById(R.id.radio_group_kind);
        rgKind.setOnCheckedChangeListener(radioListener);
    }

    public boolean joinChecked() {
        String id = aq.id(R.id.join_id_edit).getText().toString();
        String password = aq.id(R.id.join_password_edit).getText().toString();
        String name = aq.id(R.id.join_name_edit).getText().toString();

        if (id.equals("") || password.equals("") || name.equals("") || gender == -1 || kind == -1)
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
                        String name = aq.id(R.id.join_name_edit).getText().toString();

                        ArrayList<BasicNameValuePair> joinPair = new ArrayList<BasicNameValuePair>();
                        joinPair.add(new BasicNameValuePair("join", "join"));
                        joinPair.add(new BasicNameValuePair("id", id));
                        joinPair.add(new BasicNameValuePair("password", password));
                        joinPair.add(new BasicNameValuePair("name", name));
                        joinPair.add(new BasicNameValuePair("gender", String.valueOf(gender)));
                        joinPair.add(new BasicNameValuePair("kind", String.valueOf(kind)));
                        new DataPutThread().execute(joinPair);
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

    RadioGroup.OnCheckedChangeListener radioListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (group.getId() == R.id.radio_group_gender) {
                switch (checkedId) {
                    case R.id.radio_button_male:
                        gender = 0;
                        break;
                    case R.id.radio_button_female:
                        gender = 1;
                        break;
                }
            }
            else if (group.getId() == R.id.radio_group_kind) {
                switch (checkedId) {
                    case R.id.radio_button_elderly:
                        kind = 0;
                        break;
                    case R.id.radio_button_family:
                        kind = 1;
                        break;
                    case R.id.radio_button_socialworker:
                        kind = 2;
                        break;
                }
            }
        }
    };
}
