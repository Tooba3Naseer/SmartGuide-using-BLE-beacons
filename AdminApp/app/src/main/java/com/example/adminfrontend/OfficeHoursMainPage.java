package com.example.adminfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class OfficeHoursMainPage extends AppCompatActivity {

    ImageButton buttonR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_hours_main_page);

        getSupportActionBar().setTitle("Office Hours");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        buttonR = findViewById(R.id.iBtnAdd);

        buttonR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),  AddOfficeHours.class);
                startActivity(i);
            }
        });

    }
}
