package com.atti.atti_android.data;

import android.util.Log;

import com.atti.atti_android.http.HttpServerConnection;
import com.atti.atti_android.person.ElderlyPerson;
import com.atti.atti_android.person.Family;
import com.atti.atti_android.person.SocialWorker;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by BoWoon on 2016-04-28.
 */
//public class DataGetThread extends AsyncTask<String, Integer, String> {
//    private Context context;
//    private ProgressDialog progress;
//
//    @Override
//    protected String doInBackground(String... params) {
//        DefaultHttpClient httpClient = HttpServerConnection.getInstance();
//        String responseString = null;
//        String urlString = "http://52.79.147.144/mobile/family/1";
//        String imgUrl = "http://52.79.147.144/images/profile/";
//
//        if (params[0].equals("family"))
//            urlString = "http://52.79.147.144/mobile/family/1";
//        else if (params[0].equals("friends"))
//            urlString = "http://52.79.147.144/mobile/friends/1";
//        else if (params[0].equals("friendship"))
//            urlString = "http://52.79.147.144/mobile/friendship/1";
//        else if (params[0].equals("mypage"))
//            urlString = "http://52.79.147.144/mobile/user/mypage";
//        else if (params[0].equals("login"))
//            urlString = "http://52.79.147.144/mobile/user";
//
//        try {
//            HttpGet httpGet = new HttpGet(urlString);
//
//            HttpResponse response = httpClient.execute(httpGet);
//            responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
//
//            Log.d("DataGetThread", responseString);
//
//            JSONObject object = new JSONObject(responseString);
//            JSONArray jarray = new JSONArray();
//
//            if (params[0].equals("family"))
//                jarray = new JSONArray(object.getString("family_list"));
//            else if (params[0].equals("friends"))
//                jarray = new JSONArray(object.getString("friend_list"));
//            else if (params[0].equals("friendship"))
//                jarray = new JSONArray(object.getString("friendship_list"));
//            else if (params[0].equals("mypage"))
//                jarray = new JSONArray(object.getString("mypage"));
//            else if (params[0].equals("login"))
//                return "logout";
//
//            for (int i = 0; i < jarray.length(); i++) {
//                JSONObject jObject = jarray.getJSONObject(i);
//
//                String name = jObject.getString("name");
//                String img = jObject.getString("profile_name");
//
//                if (params[0].equals("family"))
//                    UsersDataManager.getUsersInstance().addData(new Family(name, imgUrl + img));
//                else if (params[0].equals("friends"))
//                    UsersDataManager.getUsersInstance().addData(new ElderlyPerson(name, imgUrl + img));
//                else if (params[0].equals("friendship"))
//                    UsersDataManager.getUsersInstance().addData(new SocialWorker(name, imgUrl + img));
//            }
//        } catch (ClientProtocolException e) {
//            Log.e("ClientProtocolException", e.getLocalizedMessage());
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.e("IOException", e.getLocalizedMessage());
//            e.printStackTrace();
//        } catch (JSONException e) {
//            Log.e("JSONException", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//
//        return responseString;
//    }
//
//    public DataGetThread(Context context) {
//        super();
//        this.context = context;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        progress = new ProgressDialog(context);
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setTitle("데이터 수신중...");
//        progress.setMessage("잠시만 기다려주세요...");
//        progress.setCancelable(false);
//        progress.setProgress(0);
//        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                cancel(true);
//            }
//        });
//        progress.show();
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        progress.dismiss();
//    }
//
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
//        progress.setProgress(values[0]);
//    }
//
//    @Override
//    protected void onCancelled(String s) {
//        super.onCancelled(s);
//        progress.dismiss();
//    }
//
//    @Override
//    protected void onCancelled() {
//        super.onCancelled();
//        progress.dismiss();
//    }
//}

public class DataGetThread {
    private String keyword;
    private String url;

    public DataGetThread() {
        this.keyword = "family";
        this.url = "http://52.79.147.144/mobile/family/1";
        startGetThread();
    }

    public DataGetThread(String keyword, String url) {
        this.keyword = keyword;
        this.url = url;
        startGetThread();
    }

    private void startGetThread() {
        DownThread thread = new DownThread(url);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    class DownThread extends Thread {
        private String url;

        public DownThread(String url) {
            this.url = url;
        }

        public void run() {
            Log.i("URLNull", "" + url);
            HttpGet get = new HttpGet(url);
            DefaultHttpClient client = HttpServerConnection.getInstance();
            try {
                client.execute(get, mResHandler);
            } catch (Exception e) {
                ;
            }
        }
    }

    ResponseHandler<String> mResHandler = new ResponseHandler<String>() {
        public String handleResponse(HttpResponse response) {
            StringBuilder html = new StringBuilder();
            String imgUrl = "http://52.79.147.144/images/profile/";

            try {
                BufferedReader br = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));
                for (; ; ) {
                    String line = br.readLine();
                    if (line == null) break;
                    html.append(line + '\n');
                }
                br.close();

                String str = html.toString();
                Log.i("StringBuilder", str);

                JSONObject object = new JSONObject(str);
                JSONArray jarray = new JSONArray();

                if (keyword.equals("family"))
                    jarray = new JSONArray(object.getString("family_list"));
                else if (keyword.equals("friends"))
                    jarray = new JSONArray(object.getString("friend_list"));
                else if (keyword.equals("friendship"))
                    jarray = new JSONArray(object.getString("friendship_list"));
                else if (keyword.equals("mypage"))
                    jarray = new JSONArray(object.getString("mypage"));

                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jObject = jarray.getJSONObject(i);

                    String name = jObject.getString("name");
                    String img = jObject.getString("profile_name");

                    if (keyword.equals("family"))
                        UsersDataManager.getUsersInstance().addData(new Family(name, imgUrl + img));
                    else if (keyword.equals("friends"))
                        UsersDataManager.getUsersInstance().addData(new ElderlyPerson(name, imgUrl + img));
                    else if (keyword.equals("friendship"))
                        UsersDataManager.getUsersInstance().addData(new SocialWorker(name, imgUrl + img));
                }
            } catch (Exception e) {
                ;
            }
            return html.toString();
        }
    };
}