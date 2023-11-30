package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PetAdd2Activity extends AppCompatActivity implements View.OnClickListener {
    

    private UserMedia userData;
    private PetMedia petData;
    private ArrayList<PetMedia> petDataList;
    private CheckBox diet, health, walk;
    private EditText petFeed, petFeedCalorie, petVaccine, petFeat;
    private Button endButton, petVaccineButton;

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

        petVaccine.setEnabled(false);
        
        endButton.setOnClickListener(this);
        petVaccineButton.setOnClickListener(this);
        diet.setOnClickListener(this);
        health.setOnClickListener(this);
        walk.setOnClickListener(this);
        setItemList();

        Intent intent = new Intent(this.getIntent());
        userData = (UserMedia) intent.getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) intent.getSerializableExtra("petDataList");
        petData = (PetMedia) intent.getSerializableExtra("petData");

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
                onSetPetData();
                onSetDataToFirebase(petData);
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
            String name = "종합백신 "+i+"차 (생후 "+(i*2+4)+"주차)";
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

    private void onSetPetData(){
        petData.setPetFeed(petFeed.getText().toString());
        petData.setPetFeedCalorie(Integer.parseInt(petFeedCalorie.getText().toString()));
        petData.setPetFeat(petFeat.getText().toString());
        petData.setPetLike(petLikeList);
        petData.setPetVaccine(vaccineList);
        petData.setWalk(0);
    }

    private void onSetDataToFirebase(PetMedia pet){
        HashMap<String, Object> hashMap = new HashMap<>();
        String uid = pet.getuId();
        int id = pet.getPetId();
        hashMap.put("uid", uid);
        hashMap.put("id", pet.getPetId());
        hashMap.put("birth", pet.getPetBirth());
        hashMap.put("feat", pet.getPetFeat());
        hashMap.put("feed", pet.getPetFeed());
        hashMap.put("feedcal", pet.getPetFeedCalorie());
        hashMap.put("image", "");
        hashMap.put("kind", pet.getPetKind());
        hashMap.put("name", pet.getPetName());
        hashMap.put("sex", pet.getPetSex());
        hashMap.put("weight", pet.getPetWeight());
        hashMap.put("walk", pet.getWalk());
        hashMap.put("petLike", pet.getPetLike());
        hashMap.put("petVaccine", pet.getPetVaccine());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid)
                .collection("pet").document(String.valueOf(id))
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        petDataList.add(petData);
                        Log.d("Firebase", "DocumentSnapshot successfully written!");

                        DocumentReference documentReference = db.collection("users").document(uid);
                        documentReference.update("count", userData.getCount()+1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        int preCount = userData.getCount();
                                        userData.setCount(preCount+1);

                                        Log.d("Firebase", "DocumentSnapshot successfully updated!");
                                        Intent intent = new Intent(PetAdd2Activity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("userData", userData);
                                        intent.putExtra("petDataList", petDataList);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Firebase", "Error updating document", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase", "Error writing document", e);
                    }
                });



    }

}