package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NearbyRoomInformation extends AppCompatActivity {

    Button buttonLeft , buttonRight, buttonUp, buttonDown ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_room_information);

        buttonLeft=(Button)findViewById(R.id.btnRoomLeft);
        buttonRight = (Button) findViewById(R.id.btnRoomRight);
        buttonUp=(Button)findViewById(R.id.btnRoomUp);
        buttonDown = (Button) findViewById(R.id.btnRoomDown);

        buttonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),RoomInformation.class);
                i.putExtra("someName", "Room No 3 ");
                i.putExtra("someName1", "DLD Lab");
                i.putExtra("someName2", "This lab lets the students to get a free hand with circuits, breadboards, ICs and signal generator. All instruments related to Digital logic design and Circuit Analysis are available in this lab.");
                i.putExtra("resource_extra", R.drawable.gcr);
                i.putExtra("resource_extra1", R.raw.dldlab);
                startActivity(i);
            }
        });
        buttonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),RoomInformation.class);
                i.putExtra("someName", "Room No 2");
                i.putExtra("someName1", "Admin Office ");
                i.putExtra("someName2", "Chairperson room is present in that area. Also here Mr. Anjam Rashid entertains students in admin room. If students face problems relating to lms i.e. Login Id of lms, changing of password etc. they fix them.  ");
                i.putExtra("resource_extra", R.drawable.gcr);
                i.putExtra("resource_extra1", R.raw.admin);

                startActivity(i);
            }
        });  buttonUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),RoomInformation.class);
                i.putExtra("someName", "Room No 1");
                i.putExtra("someName1", "Corridor Outside GA Room ");
                i.putExtra("someName2", "Here is Corridor Outside GA Room and Admin office. ");
                i.putExtra("resource_extra", R.drawable.logo);

                startActivity(i);
            }
        });  buttonDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),RoomInformation.class);
                i.putExtra("someName", "Room No 4 ");
                i.putExtra("someName1", "Nothing...");
                i.putExtra("resource_extra", R.drawable.blue);

                startActivity(i);
            }
        });
    }
}
