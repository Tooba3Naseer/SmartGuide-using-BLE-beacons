package com.example.adminfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewRoomMembers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room_members);
        getSupportActionBar().setTitle("View RoomMembers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
