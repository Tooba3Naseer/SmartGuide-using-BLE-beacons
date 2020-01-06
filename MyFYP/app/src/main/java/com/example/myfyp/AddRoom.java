package com.example.myfyp;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

public class AddRoom extends AppCompatActivity {

    EditText txtRNo,txtRName,txtDes;
    TextView txtview;
    Button btnSave, btnAu;
    ProgressBar progressBar;
    private String selectedPath;

    List<Room> roomList;
    List<String> roomNameList;

    Spinner spinner1, spinner2, spinner3, spinner4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        getSupportActionBar().setTitle("Add Rooms");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtRName= (EditText) findViewById(R.id.txtRName);
        txtRNo = (EditText) findViewById(R.id.txtRNo);
        txtDes = (EditText) findViewById(R.id.txtDes);
        txtview = (TextView) findViewById(R.id.txtAudio);

        spinner1 = (Spinner) findViewById(R.id.cboRL);
        spinner2 = (Spinner) findViewById(R.id.cboRR);
        spinner3 = (Spinner) findViewById(R.id.cboRF);
        spinner4 = (Spinner) findViewById(R.id.cboRB);
        roomList = new ArrayList<>();

        // Create an array to populate the spinner
        roomNameList = new ArrayList<String>();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnAu = (Button) findViewById(R.id.btnAudio);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoom();
            }
        });
        btnAu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseAudio();
            }
        });

        AddRoom.Downloader d = new AddRoom.Downloader(AddRoom.this,URLs.URL_READ_ROOM);
        d.execute();

    }

    private void createRoom() {
        final   String roomname = txtRName.getText().toString().trim();
        final   String roomno = txtRNo.getText().toString().trim();
        final   String desc = txtDes.getText().toString().trim();
        final   String audio = txtview.getText().toString().trim();
        final   String LeftRno = spinner1.getSelectedItem().toString().trim() ;
        final   String RightRno = spinner2.getSelectedItem().toString().trim();
        final   String FrontRno = spinner3.getSelectedItem().toString().trim();
        final   String BackRno = spinner4.getSelectedItem().toString().trim();


        if (TextUtils.isEmpty(roomname)) {
            txtRName.setError("Please enter Room name");
            txtRName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(roomno)) {
            txtRNo.setError("Please enter Room no");
            txtRNo.requestFocus();
            return;
        }



        class CreatRoom extends AsyncTask<Void, Void, String> {
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

                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("RoomNo", roomno);
                params.put("Name", roomname);
                params.put("Description", desc);
                params.put("audioInfo", audio);

                params.put("FrontR_id", FrontRno);
                params.put("BackR_id", BackRno);
                params.put("LeftR_id", LeftRno);
                params.put("RightR_id", RightRno);

                //returing the response

                URL url;

                System.setProperty("http.proxyHost", "proxy.example.com");
                System.setProperty("http.proxyPort", "3306");
                StringBuilder sb = new StringBuilder();
                try {
                    url = new URL(URLs.URL_CREATE_ROOM);
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
                        Toast.makeText(AddRoom.this, sb.toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AddRoom.this, e.toString(), Toast.LENGTH_LONG).show();

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

                    Toast.makeText(AddRoom.this, "Room Added successfully", Toast.LENGTH_LONG).show();
                    // finish();
                    //  startActivity(new Intent(getApplicationContext(), AdminDashboard.class));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        CreatRoom cr = new CreatRoom();
        cr.execute();


    }

    private void chooseAudio() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Select an Audio"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                txtview.setText(selectedPath);
            }
        }
    }


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        cursor.close();

        return path;
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
                Spinner  spinner1 = (Spinner) findViewById(R.id.cboRL);

                // Spinner adapter
                spinner1.setAdapter(new ArrayAdapter<String>(AddRoom.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roomNameList));
                spinner2.setAdapter(new ArrayAdapter<String>(AddRoom.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roomNameList));
                spinner3.setAdapter(new ArrayAdapter<String>(AddRoom.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roomNameList));
                spinner4.setAdapter(new ArrayAdapter<String>(AddRoom.this,
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
                    roomList.add(r);
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
                AddRoom.DataParser parser=new AddRoom.DataParser(c,lv,s);
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
