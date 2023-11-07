package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.*;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    TextView text;
    boolean btClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        text = findViewById(R.id.text);

        button.setOnClickListener(this);
        text.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == button) {
            if (btClicked == false){
                text.setText("On");
                btClicked = true;
            }
            else{
                text.setText("Off");
                btClicked = false;
            }
        }

    }

}