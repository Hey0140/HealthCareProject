package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    Button buttonright;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        buttonright = findViewById(R.id.buttonRIGHT);
        buttonright.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        if (view == button ){

        }
        else if (view == buttonright){
            Log.i("button", "click2");
        }

    }

}