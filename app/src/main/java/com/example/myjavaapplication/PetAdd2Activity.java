package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PetAdd2Activity extends AppCompatActivity implements View.OnClickListener {
    
    Button endButton;
    Button petVaccineButton;
    private UserMedia userData;
    CheckBox diet;
    CheckBox health;
    CheckBox walk;
    EditText petFeed;
    EditText petFeedCalorie;
    EditText petVaccine;


    HashMap<String, Boolean> petLikeList = new HashMap<>();
    HashMap<String, Boolean> vaccineList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petadd2_page);
        
        endButton = findViewById(R.id.petAddEndButton);
        petVaccineButton = findViewById(R.id.petKindVaccineButton);

        diet = findViewById(R.id.dietBox);
        health = findViewById(R.id.healthBox);
        walk = findViewById(R.id.walkBox);
        petFeed = findViewById(R.id.petFeed);
        petFeedCalorie = findViewById(R.id.petFeedCalorie);
        petVaccine = findViewById(R.id.petVaccine);
        
        endButton.setOnClickListener(this);
        petVaccineButton.setOnClickListener(this);
        diet.setOnClickListener(this);
        health.setOnClickListener(this);
        walk.setOnClickListener(this);

        Intent intent = new Intent(this.getIntent());
        userData = (UserMedia) intent.getSerializableExtra("userData");

        petLikeList.put("diet", false);
        petLikeList.put("health", false);
        petLikeList.put("walk", false);
    }

    @Override
    public void onClick(View v) {
        if(v == endButton){
            Intent intent = new Intent(PetAdd2Activity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("userData", userData);
            startActivity(intent);
            finish();
        }
        if(v == diet){
            if(diet.isChecked()){
                petLikeList.put("diet", true);
            }else{
                petLikeList.put("diet", false);
            }
        }
        if(v == health){
            if(health.isChecked()){
                petLikeList.put("health", true);
            }else{
                petLikeList.put("health", false);
            }
        }
        if(v == walk){
            if(walk.isChecked()){
                petLikeList.put("walk", true);
            }else{
                petLikeList.put("walk", false);
            }
        }
        if(v == petVaccineButton){
            PetVaccineSelectedDialog dialog = new PetVaccineSelectedDialog(this);
            dialog.setVaccineDialogListener(new PetVaccineSelectedDialog.OnVaccineDialogListener() {
                @Override
                public void onVaccineSelected(HashMap<String, Boolean> data) {
                    vaccineList = data;
                }
            });
            dialog.show();
        }

    }
}