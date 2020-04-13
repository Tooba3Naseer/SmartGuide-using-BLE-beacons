package com.example.myfyp;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import okhttp3.internal.http2.Header;

public class AddPictures extends AppCompatActivity {
    List<String> roomNameList;

    Spinner spinner1;
    private int PICK_IMAGE_REQUEST = 1;
    public static final String UPLOAD_KEY = "image";
    TextView  tvFileName;

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String selectedFilePath;
    private String SERVER_URL = "http://192.168.1.100/Admin/UploadToServer.php";
    ImageView ivAttachment;

    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pictures);
        getSupportActionBar().setTitle("Add Picture");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner1 = (Spinner) findViewById(R.id.spinrompic);
        tvFileName = (TextView)findViewById(R.id.textView2);
        ivAttachment = findViewById(R.id.imgViewpic);

        // Create an array to populate the spinner
        roomNameList = new ArrayList<String>();
        AddPictures.Downloader d = new AddPictures.Downloader(AddPictures.this,URLs.URL_READ_ROOM);
        d.execute();

        Button selectImage = findViewById(R.id.buttonUpload);
        Button saveImage = findViewById(R.id.buttonSaveImg);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on upload button Click
                if (selectedFilePath != null) {
                    dialog = ProgressDialog.show(AddPictures.this, "", "Uploading File...", true);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //creating new thread to handle Http Operations
                            uploadFile(selectedFilePath);
                        }
                    }).start();
                } else {
                    Toast.makeText(AddPictures.this, "Please choose a File First", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    //android upload file to server
    public int uploadFile(final String selectedFilePath) {

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://192.168.0.66/UploadImage/uploads/" + fileName);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddPictures.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(AddPictures.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AddPictures.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }

    public void chooseImage() {
        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            selectedFilePath = FilePath.getPath(AddPictures.this, uri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ivAttachment.setImageBitmap(bitmap);
                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    tvFileName.setText(selectedFilePath);
                } else {
                    Toast.makeText(this, "Cannot upload", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                Spinner spinner1 = (Spinner) findViewById(R.id.spinrompic);

                // Spinner adapter
                spinner1.setAdapter(new ArrayAdapter<String>(AddPictures.this,
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
                    //  roomList.add(r);
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
                AddPictures.DataParser parser=new AddPictures.DataParser(c,lv,s);
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

            InputStream is  = null;
            try {

                is  =new BufferedInputStream(con.getInputStream());
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
