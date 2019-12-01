package com.example.adminfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddPictures extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pictures);
        getSupportActionBar().setTitle("Add Pictures");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
