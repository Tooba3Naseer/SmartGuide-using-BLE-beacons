package com.example.blescannerrssi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class labels extends AppCompatActivity {

    private Button btn_Start;
    public EditText editText, editText2, editText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labels);

        btn_Start = (Button) findViewById(R.id.btnS);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);


        findViewById(R.id.btnS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(labels.this, MainActivity.class);

                intent.putExtra("key", editText.getText().toString());
                intent.putExtra("delay", editText3.getText().toString());
                intent.putExtra("iterations", editText2.getText().toString());
                //Toast.makeText(labels.this,editText.getText().toString() , Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
    }
}