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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myfyp.R;

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

public class ViewRoomMembers extends AppCompatActivity {

    List<String> roomNameList;

    Spinner spinner1;
    EditText etName, etExp , etDesg;
    Button buttonupdate;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ProgressDialog  pDialog;
    String TempItem;
    int id;
    String pname, Roomno, desg, expretise;

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_room_members);
    getSupportActionBar().setTitle("Update RoomMember");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      etName = (EditText) findViewById(R.id.txtPname);
      etExp = (EditText) findViewById(R.id.txtexpertise);
      spinner1 = (Spinner) findViewById(R.id.cmbRoomNo);
      etDesg = (EditText) findViewById(R.id.txtDesg);
      roomNameList = new ArrayList<String>();
      buttonupdate = (Button) findViewById(R.id.btnUpdateRm);

      roomNameList = new ArrayList<String>();

      ViewRoomMembers.Downloader d = new ViewRoomMembers.Downloader(ViewRoomMembers.this,URLs.URL_READ_ROOM);
      d.execute();

      TempItem = getIntent().getStringExtra("ListViewValue1");
      new ViewRoomMembers.GetDetails().execute();

      buttonupdate.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              updateRoomMem();
          }
      });


  }
    private void updateRoomMem() {

        final   String name = etName.getText().toString().trim();
        final   String Exp = etExp.getText().toString().trim();
        final   String Desg = etDesg.getText().toString().trim() ;
        final   String Rno = spinner1.getSelectedItem().toString().trim() ;


        if (TextUtils.isEmpty(name)) {
            etName.setError("Please enter Room Member name");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Exp)) {
            etExp.setError("Please enter RoomMem Expertise");
            etExp.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(Desg)) {
            etDesg.setError("Please enter RoomMem Designation");
            etDesg.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("rmid" , TempItem);
        params.put("rmroomno",Rno );
        params.put("rmperson", name);
        params.put("rmdesignation",Desg);
        params.put("rmexperts", Exp);


        ViewRoomMembers.PerformNetworkRequest request = new ViewRoomMembers.PerformNetworkRequest(URLs.URL_UPDATE_ROOMMEM, params, CODE_POST_REQUEST);
        request.execute();
        Toast.makeText(getApplicationContext(),"Update Record Successfully" , Toast.LENGTH_LONG);

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
            pDialog = new ProgressDialog(ViewRoomMembers.this);
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
                spinner1.setAdapter(new ArrayAdapter<String>(ViewRoomMembers.this,
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
                ViewRoomMembers.DataParser parser=new ViewRoomMembers.DataParser(c,lv,s);
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
    class GetDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewRoomMembers.this);
            pDialog.setMessage("Loading  details. Please wait...");
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
            params.put("rmid", TempItem);

            //returing the response
            return requestHandler.sendPostRequest(URLs.ROOMMEM1, params);

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

                JSONObject jb = jo.getJSONObject("roommem1");

                id = jb.getInt("rmid");
                pname = jb.getString("rmperson");
                Roomno = jb.getString("rmroomno");
                desg = jb.getString("rmdesignation");
                expretise = jb.getString("rmexperts");

                etName.setText(pname);
              //  spinner1.setSelection(((ArrayAdapter<String>) spinner1.getAdapter()).getPosition(Roomno));
                etDesg.setText(desg);
                etExp.setText(expretise);
                //txtRName.setText(name);
               // txtRNo.setText(Roomno);
               //  txtDes.setText(desc);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
