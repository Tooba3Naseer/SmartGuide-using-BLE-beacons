package com.example.adminfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddRoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        getSupportActionBar().setTitle("Add Rooms");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
