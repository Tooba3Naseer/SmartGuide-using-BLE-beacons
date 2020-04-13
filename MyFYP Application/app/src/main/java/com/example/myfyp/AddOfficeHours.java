package com.example.myfyp;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class AddOfficeHours extends AppCompatActivity {

    List<String> roomMemNameList;

    Spinner spinStfMem,spinDays;
    EditText etStTm, etEndTm;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_office_hours);
        getSupportActionBar().setTitle("Add OfficeHours");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        roomMemNameList = new ArrayList<String>();

        etStTm = (EditText) findViewById(R.id.etStartTime);
        etEndTm = (EditText) findViewById(R.id.etEndTime);
        spinStfMem = (Spinner) findViewById(R.id.spinStfMem);
        spinDays = (Spinner) findViewById(R.id.spinDays);

        save = (Button) findViewById(R.id.buttonOhsave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOfficeHrs();
            }
        });
        AddOfficeHours.Downloader d = new AddOfficeHours.Downloader(AddOfficeHours.this,URLs.URL_READ_STAFFMEM);
        d.execute();
    }
    private void createOfficeHrs() {
        final   String STime = etStTm.getText().toString().trim();
        final   String ETime = etEndTm.getText().toString().trim();
        final   String StfMemName = spinStfMem.getSelectedItem().toString().trim() ;
        final   String Days = spinDays.getSelectedItem().toString().trim() ;

        class CreateOh extends AsyncTask<Void, Void, String> {
            private ProgressBar progressBar;
            @Override

            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... voids) {

                String abc = "twsufay";
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameter

                HashMap<String, String> params = new HashMap<>();
                params.put("tmday", Days);
                params.put("tmopen",STime );
                params.put("tmclose", ETime);
                params.put("stfmemName", StfMemName);


                //returing the response

                URL url;

                System.setProperty("http.proxyHost", "proxy.example.com");
                System.setProperty("http.proxyPort", "3306");
                StringBuilder sb = new StringBuilder();
                try {
                    url = new URL(URLs.URL_CREATE_OffHOURS);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();

                    OutputStream os = conn.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(requestHandler.getPostDataString(params));

                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        sb = new StringBuilder();
                        String response;

                        while ((response = br.readLine()) != null) {
                            sb.append(response);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AddOfficeHours.this, e.toString(), Toast.LENGTH_LONG).show();

                }

                return sb.toString();
            }


            //     return requestHandler.sendPostRequest(URls.URL_REGISTER,params);


            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                String sENF = "SFHDJK";

                try {
                    // Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    Toast.makeText(AddOfficeHours.this, "Timings Added successfully", Toast.LENGTH_LONG).show();
                    // finish();
                    //  startActivity(new Intent(getApplicationContext(), AdminDashboard.class));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        CreateOh oh = new CreateOh();
        oh.execute();
    }

    public class DataParser  extends AsyncTask<Void,Void,Integer> {

        Context c;
        ListView lv;
        String jsonData ;

        ProgressDialog pd;
        ArrayList<Room> rooms = new ArrayList<>();

        public DataParser(Context c, ListView lv, String jsonData) {
            this.c = c;
            this.lv = lv;
            this.jsonData = jsonData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(c);
            pd.setTitle("Parse");
            pd.setMessage("Parsing...Please wait");
            pd.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            return this.parseData();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            pd.dismiss();
            if (result == 0) {
                Toast.makeText(c, "Unable to parse", Toast.LENGTH_SHORT).show();
            } else {
                //CALL ADAPTER TO BIND DATA

                //  Spinner mySpinner = (Spinner) findViewById(R.id.spinRoom);

                // Spinner adapter
                spinStfMem.setAdapter(new ArrayAdapter<String>(AddOfficeHours.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roomMemNameList));


            }
        }

        private int parseData() {

            try {
                String jsonString = jsonData;

                JSONObject jo = new JSONObject(jsonString);
                JSONArray ja = jo.getJSONArray("roommembers");
                //  roomList.clear();
                RoomStaffMems r = null;
                for (int i = 0; i < ja.length(); i++) {

                    jo = ja.getJSONObject(i);

                    int id = jo.getInt("rmid");
                    String roomNo = jo.getString("rmroomno");
                    String name = jo.getString("rmperson");
                    String desig = jo.getString("rmdesignation");
                    String experts = jo.getString("rmexperts");
                    r = new RoomStaffMems(id, name, roomNo, desig,experts);
                    r.setId(id);
                    r.setName(name);
                    r.setRoomno(roomNo);
                    r.setDesg(desig);
                    r.setExperts(experts);
                    //roomList.add(r);
                    // Populate spinner with country names
                    roomMemNameList.add(jo.optString("rmperson"));
                }

                return 1;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return 0;
        }
    }

    public class Downloader extends AsyncTask<Void,Void,String> {

        Context c;
        String urlAddress;
        ListView lv;

        ProgressDialog pd;

        public Downloader(Context c, String urlAddress) {
            this.c = c;
            this.urlAddress = urlAddress;
            this.lv = lv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd=new ProgressDialog(c);
            pd.setTitle("Fetch");
            pd.setMessage("Fetching....Please wait");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return this.downloadData();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pd.dismiss();

            if(s==null)
            {
                Toast.makeText(c,"Unsuccessfull,Null returned",Toast.LENGTH_SHORT).show();
            }else
            {
                //CALL DATA PARSER TO PARSE
                AddOfficeHours.DataParser parser=new AddOfficeHours.DataParser(c,lv,s);
                parser.execute();

            }

        }

        private String downloadData()
        {

            HttpURLConnection con= RequestHandler.connect(urlAddress);
            if(con==null)
            {
                return null;
            }

            InputStream is=null;
            try {

                is=new BufferedInputStream(con.getInputStream());
                BufferedReader br=new BufferedReader(new InputStreamReader(is));

                String line=null;
                StringBuffer response=new StringBuffer();

                if(br != null)
                {
                    while ((line=br.readLine()) != null)
                    {
                        response.append(line+" ");
                    }

                    br.close();

                }else
                {
                    return null;
                }

                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(is != null)
                {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
    }

}
