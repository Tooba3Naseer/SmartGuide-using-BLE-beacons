package com.example.adminfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddOfficeHours extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_office_hours);
        getSupportActionBar().setTitle("Add OfficeHours");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
