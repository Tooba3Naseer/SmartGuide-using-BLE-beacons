package com.example.adminfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewOfficeHours extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_office_hours);
        getSupportActionBar().setTitle("View OfficeHours");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
