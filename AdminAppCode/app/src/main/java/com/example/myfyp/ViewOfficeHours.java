package com.example.myfyp;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

public class ViewOfficeHours extends AppCompatActivity {

    List<String> roomMemNameList;

    Spinner spinStfMem,spinDays;
    EditText etStTm, etEndTm;
    Button update;
    private ProgressDialog pDialog;
    String StartTm, EndTm , Days , pname;
    int id;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    String TempItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_office_hours);
        getSupportActionBar().setTitle("Update OfficeHours");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etStTm = (EditText) findViewById(R.id.txtStime);
        etEndTm = (EditText) findViewById(R.id.txtETime);
        spinStfMem = (Spinner) findViewById(R.id.spinMemName);
        spinDays = (Spinner) findViewById(R.id.spinday);
        roomMemNameList = new ArrayList<String>();

        update = (Button) findViewById(R.id.btnUpdateTm);
        ViewOfficeHours.Downloader d = new ViewOfficeHours.Downloader(ViewOfficeHours.this,URLs.URL_READ_STAFFMEM);
        d.execute();
        TempItem = getIntent().getStringExtra("ListViewValue2");
        new ViewOfficeHours.GetDetails().execute();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTimings();
            }
        });


    }
    private void updateTimings() {

        final   String STime = etStTm.getText().toString().trim();
        final   String ETime = etEndTm.getText().toString().trim();
        final   String StfMemName = spinStfMem.getSelectedItem().toString().trim() ;
        final   String Days = spinDays.getSelectedItem().toString().trim() ;


        HashMap<String, String> params = new HashMap<>();
        params.put("tmid" , TempItem);
        params.put("tmday", Days);
        params.put("tmopen",STime );
        params.put("tmclose", ETime);
        params.put("stfmemName", StfMemName);



        ViewOfficeHours.PerformNetworkRequest request = new ViewOfficeHours.PerformNetworkRequest(URLs.URL_UPDATE_TIMINGS, params, CODE_POST_REQUEST);
        request.execute();
        Toast.makeText(getApplicationContext(),"Update Record Successfully" , Toast.LENGTH_LONG);

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
                ViewOfficeHours.DataParser parser=new ViewOfficeHours.DataParser(c,lv,s);
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
                spinStfMem.setAdapter(new ArrayAdapter<String>(ViewOfficeHours.this,
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
            pDialog = new ProgressDialog(ViewOfficeHours.this);
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
            pDialog = new ProgressDialog(ViewOfficeHours.this);
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
            params.put("tmid", TempItem);

            //returing the response
            return requestHandler.sendPostRequest(URLs.OFFICEHOURS1, params);

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

                JSONObject jb = jo.getJSONObject("Timings");

                id = jb.getInt("tmid");
                Days= jb.getString("tmday");
                StartTm = jb.getString("tmopen");
                EndTm = jb.getString("tmclose");
                pname = jb.getString("stfmemName");

              //  spinDays.setSelection(((ArrayAdapter<String>) spinDays.getAdapter()).getPosition(Days));
                etStTm.setText(StartTm);
                etEndTm.setText(EndTm);
               // spinStfMem.setSelection(((ArrayAdapter<String>) spinStfMem.getAdapter()).getPosition(pname));

               /* txtRName.setText(name);
                txtRNo.setText(Roomno);
                txtDes.setText(desc);
*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
