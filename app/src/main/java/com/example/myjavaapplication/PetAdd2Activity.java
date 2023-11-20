package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
    EditText petFeat;


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
        petFeat = findViewById(R.id.petFeat);
        
        endButton.setOnClickListener(this);
        petVaccineButton.setOnClickListener(this);
        diet.setOnClickListener(this);
        health.setOnClickListener(this);
        walk.setOnClickListener(this);
        setItemList();

        Intent intent = new Intent(this.getIntent());
        userData = (UserMedia) intent.getSerializableExtra("userData");

        petLikeList.put("diet", false);
        petLikeList.put("health", false);
        petLikeList.put("walk", false);
    }

    @Override
    public void onClick(View v) {
        if(v == endButton){
            if(petFeed.getText().toString().length() < 1){
                Toast.makeText(this, "사료를 입력하세요.",Toast.LENGTH_SHORT).show();
            } else if (petFeedCalorie.getText().toString().length() < 1){
                Toast.makeText(this, "사료 한 끼 칼로리를 입력하세요.",Toast.LENGTH_SHORT).show();
            } else if (petFeat.getText().toString().length() < 1){
                Toast.makeText(this, "성격을 입력하세요.",Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(PetAdd2Activity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("userData", userData);
                startActivity(intent);
                finish();
            }
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

            PetVaccineSelectedDialog dialog = new PetVaccineSelectedDialog(this, vaccineList);
            dialog.setVaccineDialogListener(new PetVaccineSelectedDialog.OnVaccineDialogListener() {
                @Override
                public void onVaccineSelected(HashMap<String, Boolean> data) {
                    vaccineList = data;
                    String petVaccineData = "";
                    for(Map.Entry<String, Boolean> hashMap : vaccineList.entrySet()){
                        if(hashMap.getValue() == true){
                            petVaccineData = petVaccineData + hashMap.getKey() + ", ";
                        }
                    }
                    if( petVaccineData.length() > 3){
                        petVaccine.setText(petVaccineData.substring(0,petVaccineData.length()-2));
                    }
                }
            });
            dialog.show();
        }
    }

    private void setItemList(){
        for(int i = 1; i <=5; i++){
            String name = "종합백신 "+i+"차 (생후"+(i*2+4)+"주차)";
            vaccineList.put(name, false);
        }
        for(int i = 1; i <=2; i++){
            String name = "코로나 장염 "+i+"차 (생후 "+(i*2+4)+"주차)";
            vaccineList.put(name, false);
        }
        for(int i = 1; i <=2; i++){
            String name = "켄넬코프 "+i+"차 (생후 "+(i*2+8)+"주차)";
            vaccineList.put(name, false);
        }

        vaccineList.put("광견병 1차 (생후 12주차)", false);
        vaccineList.put("광견병 2차 (생후 1년차)", false);

        for(int i = 1; i <=2; i++){
            String name = "인플루엔자 "+i+"차 (생후 "+(i*2+12)+"주차)";
            vaccineList.put(name, false);
        }
    }

}