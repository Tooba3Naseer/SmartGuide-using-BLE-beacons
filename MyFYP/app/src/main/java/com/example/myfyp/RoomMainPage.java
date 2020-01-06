package com.example.myfyp;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static android.view.View.GONE;

public class RoomMainPage extends AppCompatActivity {

    ImageButton buttonR;
    List<Room> roomList;
    List<String> roomNameList;

    ProgressBar progressBar;

    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_main_page);
        getSupportActionBar().setTitle("Rooms");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonR = findViewById(R.id.iBtnAdd);
        final ListView lv =(ListView) findViewById(R.id.lvRooms);
        roomList = new ArrayList<>();
        // Create an array to populate the spinner
        roomNameList = new ArrayList<String>();


        spinner=(Spinner)findViewById(R.id.spinRoom);

        buttonR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),  AddRoom.class);
                startActivity(i);
            }
        });
    }

    public class  RoomAdapter extends ArrayAdapter<Room>
 {

     List<Room> roomList;
     public RoomAdapter(List<Room> roomsList) {
         super( RoomMainPage.this, R.layout.activity_main, roomsList);
         this.roomList = roomsList;
       Toast.makeText(RoomMainPage.this, "Hi Adapter", Toast.LENGTH_SHORT).show();

     }

     public View getView(final int position, View convertView, ViewGroup parent) {

         LayoutInflater inflater = getLayoutInflater();
        // View itemView = inflater.inflate(R.layout.activity_main, parent, false);
         View listViewItem = inflater.inflate(R.layout.activity_main, null, true);

         if (convertView == null) {
            // Toast.makeText(RoomMainPage.this, "Hi Adapter 23", Toast.LENGTH_SHORT).show();

             //getting the textview for displaying name
             TextView textViewName = listViewItem.findViewById(R.id.txtRoom);
             ImageButton btnDel = listViewItem.findViewById(R.id.iBtnDel);
             ImageButton btnEdit = listViewItem.findViewById(R.id.iBtnEdit);
             ImageButton btnView = listViewItem.findViewById(R.id.iBtnView);
             Toast.makeText(RoomMainPage.this, "Hi Adapter 2", Toast.LENGTH_SHORT).show();

             final Room room = roomList.get(position);
             // textViewName.setText(room.getName());
             textViewName.setText(roomList.get(position).getName());
             // textViewName.setText(roomList.get(position).getName());
         }
         return listViewItem;
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
                RoomAdapter adapter = new RoomAdapter(rooms);
                lv.setAdapter(adapter);

                Spinner mySpinner = (Spinner) findViewById(R.id.spinRoom);
                //Spinner  spinner1 = (Spinner) findViewById(R.id.cboRL);

                // Spinner adapter
                mySpinner.setAdapter(new ArrayAdapter<String>(RoomMainPage.this,
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
                    roomNameList.add(jo.optString("Name"));


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

        public Downloader(Context c, String urlAddress, ListView lv) {
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
                DataParser parser=new DataParser(c,lv,s);
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
                        response.append(line+"");
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

