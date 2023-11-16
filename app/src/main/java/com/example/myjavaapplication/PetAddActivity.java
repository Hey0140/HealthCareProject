package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

public class PetAddActivity extends AppCompatActivity implements View.OnClickListener {
    View petProfileBox;
    View cameraBox;

    ImageView petProfileImage;

    EditText petName;
    EditText petKind;
    EditText petBirth;
    EditText petWeight;

    CheckBox male, female, maleNeutering, femaleNeutering;
    Button petAddNextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petadd_page);

        petProfileBox = findViewById(R.id.petProfileBox);
        petProfileImage = findViewById(R.id.petProfileImage);
        cameraBox = findViewById(R.id.cameraBox);
        petName = findViewById(R.id.petKind);
        petKind = findViewById(R.id.petKind);
        petBirth = findViewById(R.id.petBirth);
        petWeight = findViewById(R.id.petWeight);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        maleNeutering = findViewById(R.id.maleNeutering);
        femaleNeutering = findViewById(R.id.femaleNeutering);

        petAddNextButton = findViewById(R.id.petAddNextButton);

        petAddNextButton.setOnClickListener(this);
        petProfileBox.setOnClickListener(this);
        cameraBox.setOnClickListener(this);

        Intent intent = new Intent(this.getIntent());
        String uid = intent.getStringExtra("userData");
        Log.i("intent", uid);
    }


    @Override
    public void onClick(View v) {
        if(v == petAddNextButton){

        }
        if ( v== petProfileBox){

        }
        if( v== cameraBox){

        }
    }
}