package com.example.myfyp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewRoom extends AppCompatActivity {
    EditText txtRNo,txtRName,txtDes;
    TextView txtview;
    Button btnUpdate, btnAu;
    int id;
    String name, Roomno, desc, LeftRoomno , RightRoomno ,FrontRoomno ,BackRoomno;
    List<Room> roomList;
    List<String> roomNameList;

    Spinner spinner1, spinner2, spinner3, spinner4;
    private ProgressDialog pDialog;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    String TempItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);
        getSupportActionBar().setTitle("Update Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtRName= (EditText) findViewById(R.id.txtUpRName);
        txtRNo = (EditText) findViewById(R.id.txtUpRNo);
        txtDes = (EditText) findViewById(R.id.txtUpDes);
    // txtview = (TextView) findViewById(R.id.txtAudio);

        spinner1 = (Spinner) findViewById(R.id.cboUpRL);
        spinner2 = (Spinner) findViewById(R.id.cboUpRR);
        spinner3 = (Spinner) findViewById(R.id.cboUpRF);
        spinner4 = (Spinner) findViewById(R.id.cboUpRB);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        roomList = new ArrayList<>();
        roomNameList = new ArrayList<String>();

        ViewRoom.Downloader d = new ViewRoom.Downloader(ViewRoom.this,URLs.URL_READ_ROOM);
        d.execute();

        TempItem = getIntent().getStringExtra("ListViewValue");

        // HttpWebCall(TempItem);

        // Getting complete product details in background thread
        new GetDetails().execute();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRoom();
            }
        });
    }

    private void updateRoom() {

        final   String roomname = txtRName.getText().toString().trim();
        final   String roomno = txtRNo.getText().toString().trim();
        final   String desc = txtDes.getText().toString().trim();
       // final   String audio = "abc";
      //  final   String LeftRno = spinner1.getSelectedItem().toString().trim() ;
      //  final   String RightRno = spinner2.getSelectedItem().toString().trim();
      //  final   String FrontRno = spinner3.getSelectedItem().toString().trim();
      //  final   String BackRno = spinner4.getSelectedItem().toString().trim();


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

        HashMap<String, String> params = new HashMap<>();
        params.put("id" , TempItem);
        params.put("RoomNo", roomno);
        params.put("Name", roomname);
        params.put("Description", desc);
      //  params.put("audioInfo", audio);

      /*params.put("FrontR_id", FrontRno);
        params.put("BackR_id", BackRno);
        params.put("LeftR_id", LeftRno);
        params.put("RightR_id", RightRno);
       */

        PerformNetworkRequest request = new PerformNetworkRequest(URLs.URL_UPDATE_ROOM, params, CODE_POST_REQUEST);
        request.execute();
        Toast.makeText(getApplicationContext(),"Update Record Successfully" , Toast.LENGTH_LONG);

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
                 // Spinner adapter
                spinner1.setAdapter(new ArrayAdapter<String>(ViewRoom.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roomNameList));
                spinner2.setAdapter(new ArrayAdapter<String>(ViewRoom.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roomNameList));
                spinner3.setAdapter(new ArrayAdapter<String>(ViewRoom.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roomNameList));
                spinner4.setAdapter(new ArrayAdapter<String>(ViewRoom.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        roomNameList));


            }
        }

        private int parseData() {

            try {
                String jsonString = jsonData;

                JSONObject jo = new JSONObject(jsonString);
                JSONArray ja = jo.getJSONArray("roominfo");
                roomList.clear();
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
                    roomNameList.add(name);

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
                ViewRoom.DataParser parser=new ViewRoom.DataParser(c,lv,s);
                parser.execute();

             //  ViewRoom.GetDetails details = new ViewRoom.GetDetails(c, s);
              // details.execute();
               // GetRoom();
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

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewRoom.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pDialog.isShowing())
                pDialog.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    //refreshHeroList(object.getJSONArray("heroes"));
                    // refreshRoomList();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }


    class GetDetails extends AsyncTask<Void, Void, String> {
        Context c;
        String jsonData;

        /**
         * Before starting background thread Show Progress Dialog
         * */
      /*  public GetDetails(Context c, String jsondata) {
            this.c = c;
            this.jsonData = jsondata;

        }
        */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewRoom.this);
            pDialog.setMessage("Loading room details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting details in background thread
         * */
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id", TempItem);

            //returing the response
           return requestHandler.sendPostRequest(URLs.ROOM1, params);

        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            //hiding the progressbar after completion
           // progressBar.setVisibility(View.GONE);
            String sENF = "SFHDJK";

            String success;
            try {
               // String jsonString = jsonData;

                JSONObject jo = new JSONObject(s);
                Log.d("Single Product Details", jo.toString());
                success =jo.getString("success");
                String message= jo.getString("message");

                JSONObject jb = jo.getJSONObject("room1");

                id = jb.getInt("id");
                name = jb.getString("name");
                Roomno = jb.getString("RoomNo");
                desc = jb.getString("Description");

                txtRName.setText(name);
                txtRNo.setText(Roomno);
                txtDes.setText(desc);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
