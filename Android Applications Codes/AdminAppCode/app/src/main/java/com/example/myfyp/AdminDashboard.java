package com.example.myfyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminDashboard extends AppCompatActivity {
  Button buttonR , buttonRM , buttonOH, buttonP, buttonL;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_dashboard);

    getSupportActionBar().setTitle("AdminDashboard");

    buttonR=(Button)findViewById(R.id.btnRoom);
    buttonRM = (Button) findViewById(R.id.btnRoomMem);
    buttonOH=(Button)findViewById(R.id.btnOh);
    buttonP=(Button)findViewById(R.id.btnPic);
    buttonL=(Button)findViewById(R.id.btnLogout);

    buttonR.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(),RoomMainPage.class);
        startActivity(i);
      }
    });

    buttonRM.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(),RoomMembersMainPage.class);
        startActivity(i);
      }
    });
    buttonOH.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(), OfficeHoursMainPage.class);
        startActivity(i);
      }
    });
    buttonP.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(),AddPictures.class);
        startActivity(i);
      }
    });
    buttonL.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent i = new Intent(getApplicationContext(),AdminLogin.class);
        startActivity(i);
        finish();
      }
    });
  }
}
