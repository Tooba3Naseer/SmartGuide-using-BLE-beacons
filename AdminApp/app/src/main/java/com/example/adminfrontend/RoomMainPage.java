package com.example.adminfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RoomMainPage extends AppCompatActivity {

    ImageButton buttonR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_main_page);
        getSupportActionBar().setTitle("Rooms");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonR = findViewById(R.id.iBtnAdd);

        buttonR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),  AddRoom.class);
                startActivity(i);
            }
        });
    }
}
