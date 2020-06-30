package com.example.myfyp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
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

public class AddRoomMembers extends AppCompatActivity {
    List<String> roomNameList;

    Spinner spinner1;
    EditText etName, etExp , etDesg;
    Button buttonsave;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room_members);
        getSupportActionBar().setTitle("Add RoomMembers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = (EditText) findViewById(R.id.editTextName);
        etExp = (EditText) findViewById(R.id.editTextExp);
        spinner1 = (Spinner) findViewById(R.id.spinnerRno);
        etDesg = (EditText) findViewById(R.id.ETDesig);
        roomNameList = new ArrayList<String>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        buttonsave = (Button) findViewById(R.id.buttonRmSave);
        buttonsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(AddRoomMembers.this, "ABC", Toast.LENGTH_LONG).show();
                createRoomMem();
            }
        });
        AddRoomMembers.Downloader d = new AddRoomMembers.Downloader(AddRoomMembers.this,URLs.URL_READ_ROOM);
        d.execute();



    }
    private void createRoomMem() {
        final   String name = etName.getText().toString().trim();
        final   String Exp = etExp.getText().toString().trim();
        final   String Desg = etDesg.getText().toString().trim() ;
        final   String Rno = spinner1.getSelectedItem().toString().trim() ;


        if (TextUtils.isEmpty(name)) {
            etName.setError("Please enter Person name");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Rno)) {
            etExp.setError("Please enter your expertise");
            etExp.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(Desg)) {
            etDesg.setError("Please enter your designation");
            etDesg.requestFocus();
            return;
        }


        class CreatRoomMem extends AsyncTask<Void, Void, String> {
            private ProgressBar progressBar;
            @Override

            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar1);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... voids) {

                String abc = "twsufay";
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters


                HashMap<String, String> params = new HashMap<>();
                params.put("rmroomno",Rno );
                params.put("rmperson", name);
                params.put("rmdesignation",Desg);
                params.put("rmexperts", Exp);

                //returing the response

                URL url;

                System.setProperty("http.proxyHost", "proxy.example.com");
                System.setProperty("http.proxyPort", "3306");
                StringBuilder sb = new StringBuilder();
                try {
                    url = new URL(URLs.URL_CREATE_ROOMMem);
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
                    else {
                        Toast.makeText(AddRoomMembers.this, sb.toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AddRoomMembers.this, e.toString(), Toast.LENGTH_LONG).show();

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

                    Toast.makeText(AddRoomMembers.this, "Added successfully", Toast.LENGTH_LONG).show();
                    // finish();
                    //  startActivity(new Intent(getApplicationContext(), AdminDashboard.class));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        CreatRoomMem cr = new CreatRoomMem();
        cr.execute();
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
                spinner1.setAdapter(new ArrayAdapter<String>(AddRoomMembers.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roomNameList));


            }
        }

        private int parseData() {

            try {
                String jsonString = jsonData;

                JSONObject jo = new JSONObject(jsonString);
                JSONArray ja = jo.getJSONArray("roominfo");
                //  roomList.clear();
                Room r = null;

                for (int i = 0; i < ja.length(); i++) {

                    jo = ja.getJSONObject(i);

                    int id = jo.getInt("id");
                    String name = jo.getString("Name");
                    String roomNo = jo.getString("RoomNo");
                    String description = jo.getString("Description");

                    r = new Room(id, name, roomNo, description);
                    r.setId(id);
                    r.setName(name);
                    r.setRoomno(roomNo);
                    r.setDesc(description);
                    //roomList.add(r);
                    // Populate spinner with country names
                    roomNameList.add(jo.optString("RoomNo"));


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
                AddRoomMembers.DataParser parser=new AddRoomMembers.DataParser(c,lv,s);
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
