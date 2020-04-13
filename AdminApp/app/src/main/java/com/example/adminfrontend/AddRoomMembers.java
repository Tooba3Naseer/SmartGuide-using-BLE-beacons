package com.example.adminfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddRoomMembers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room_members);
        getSupportActionBar().setTitle("Add RoomMembers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
