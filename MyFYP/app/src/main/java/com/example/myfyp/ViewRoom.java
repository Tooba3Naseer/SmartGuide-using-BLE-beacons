package com.example.myfyp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewRoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);
        getSupportActionBar().setTitle("View Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
