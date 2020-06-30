package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RoomInformation extends AppCompatActivity {

    TextView txtRoomNo, txtRoomName, txt3b;
    public static ImageView imagePreview;
    MediaPlayer ring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_information);
        Intent intent = getIntent();
      //  String message = intent.getStringExtra("someName");
        txtRoomNo = (TextView) findViewById(R.id.txt1b);
        txtRoomName = (TextView) findViewById(R.id.txt2b);
        txt3b = (TextView) findViewById(R.id.txt3b);
        imagePreview = (ImageView)findViewById(R.id.img1);

        txtRoomNo.setText(getIntent().getExtras().getString("someName"));
        txtRoomName.setText(getIntent().getExtras().getString("someName1"));
        txt3b.setText(getIntent().getExtras().getString("someName2"));
        ring= MediaPlayer.create(this, getIntent().getIntExtra("resource_extra1", R.raw.audio1));
        imagePreview.setImageResource(getIntent().getIntExtra("resource_extra", R.drawable.img1));

       // int path = Integer.parseInt (getIntent().getStringExtra("path"));
       // int media=Integer.parseInt(path);
        //ring= MediaPlayer.create(this, R.raw.audio1);
       // ring.setDataSource(path);
    }
    public  void  PlayIt(View v){
        ring.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ring.release();
    }
}
