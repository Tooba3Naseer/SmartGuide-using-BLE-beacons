package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import  java.nio.MappedByteBuffer;
import  java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class RoomLocation extends AppCompatActivity {

    Button buttonR , buttonRM, buttonInfer ;

    TextView txtRoom;
    private TensorFlowInferenceInterface inferenceInterface;
    private static final String INPUT_NODE = "dense_9_input:0";
    private static final String OUTPUT_NODE = "dense_10/Sigmoid:0";
    private static final long[] INPUT_SHAPE = {1,24};
    private static final String[] RoomNames = {"Female Faculty Room", "Corridor Outside GA room", "Corridor Outside Embedded Lab", "DSP Lab 1", "DSP Lab 2", "Seminar Hall",
            "Small Seminar Hall", "Dr Fareed Jafrey Room", "Entrance of Computer Engineering Dept", "Corridor Outside Small Seminar Hall",
  "Corridor Outside Madam Tania Room", "Cabin Room", "Robotics Lab", "Embedded Lab", "DLD Lab", "GA Room", "Admin Office"};

    private static final float[] aList = new float[]{-100,-100,-100,-100,-100,-100,-100,-100,-72,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_location);
        buttonR=(Button)findViewById(R.id.buttonseeR);
        buttonRM = (Button) findViewById(R.id.buttonnRooms);
      //  buttonInfer = (Button) findViewById(R.id.buttonInfer);
        txtRoom = (TextView) findViewById(R.id.textRoomName);
        //final float[] aList = new float[]{-100,-100,-100,-100,-100,-100,-100,-100,-68,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100};
        loadmodel();
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),RoomInformation.class);

                i.putExtra("someName", "Room No 0 ");
                i.putExtra("someName1", "GA Room");
                i.putExtra("someName2", "Graduate assistants in CE department are assistants of the professors, associate professors and assistant professors and they do their graduate studies along with teaching. They take labs of the particular subjects.");
                i.putExtra("path", R.raw.audio1);
                i.putExtra("resource_extra", R.drawable.gcr);
                startActivity(i);
            }
        });

        buttonRM.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),NearbyRoomInformation.class);
                startActivity(i);
            }
        });

    }



public void loadmodel(){
    // Load model from assets
    inferenceInterface = new TensorFlowInferenceInterface(getAssets(),"model13.1.pb");
    float output[] = new float[17];
    inferenceInterface.feed(INPUT_NODE, aList,INPUT_SHAPE);
    inferenceInterface.run(new String[] {OUTPUT_NODE});
    inferenceInterface.fetch(OUTPUT_NODE, output);
    float maxi = 0;
    int clas = -1;
    for(int i =0 ; i < output.length; i++)
    {
        if (output[i]> maxi)
        {
            maxi = output[i];
            clas = i;
        }
    }
    txtRoom.setText(RoomNames[clas]);

}

}
