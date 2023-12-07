package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class HospitalActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText hospitalName, doctorName, hospitalAddress, hospitalPhoneNumber;
    private Button hospitalButton;
//    private UserMedia userData = new UserMedia();
//    private ArrayList<PetMedia> petDataList = new ArrayList<>();
    private PetMedia petData = new PetMedia();
    private HospitalMedia hospitalData = new HospitalMedia();
    private boolean isHospital = false;
//    private int petPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hosipital_page);

        hospitalName = findViewById(R.id.hospitalName);
        doctorName = findViewById(R.id.doctorName);
        hospitalAddress = findViewById(R.id.hospitalAddress);
        hospitalPhoneNumber = findViewById(R.id.hospitalPhoneNumber);
        hospitalButton = findViewById(R.id.hospitalEndButton);

        Intent intent = new Intent(this.getIntent());
        petData = (PetMedia) intent.getSerializableExtra("petData");

        isHospital = MainActivity.isHospital;
        if(isHospital){
            hospitalData = MainActivity.hospitalData;
            hospitalName.setText(hospitalData.getName());
            doctorName.setText(hospitalData.getDoctor());
            hospitalAddress.setText(hospitalData.getAddress());
            hospitalPhoneNumber.setText(hospitalData.getNumber());
        }
        else{
            hospitalName.setText("");
            doctorName.setText("");
            hospitalAddress.setText("");
            hospitalPhoneNumber.setText("");
        }

        hospitalButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if( view == hospitalButton){
            if (hospitalName.getText().toString().equals("")){
                Toast.makeText(this, "병원 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (doctorName.getText().toString().equals("")){
                Toast.makeText(this, "주치의 성명을 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (hospitalAddress.getText().toString().equals("")){
                Toast.makeText(this, "병원 주소를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else if (hospitalPhoneNumber.getText().toString().equals("")){
                Toast.makeText(this, "병원 전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "완료되었습니다.", Toast.LENGTH_SHORT).show();
                addHospitalToFirestore(petData);

            }
        }
    }

    public void addHospitalToFirestore(PetMedia pet){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = pet.getuId();
        String id = String.valueOf(pet.getPetId());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("id", pet.getPetId());

        String name = hospitalName.getText().toString();
        String doctor = doctorName.getText().toString();
        String address = hospitalAddress.getText().toString();
        String number = hospitalPhoneNumber.getText().toString();

        HospitalMedia hospital = new HospitalMedia();

        hospital.setUid(pet.getuId());
        hospital.setId(pet.getPetId());
        hospital.setName(name);
        hospital.setDoctor(doctor);
        hospital.setAddress(address);
        hospital.setNumber(number);

        hashMap.put("name", name);
        hashMap.put("number", number);
        hashMap.put("doctor", doctor);
        hashMap.put("address", address);

        if ( !isHospital){
            hashMap.put("feature", "");
            hospital.setFeature("");
        }
        else {
            String feature = MainActivity.hospitalData.getFeature();
            hospital.setFeature(feature);
            hashMap.put("feature", feature);
        }


        db.collection("hospital").document(uid)
                .collection("pet").document(id)
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firebase", "DocumentSnapshot successfully written!");
                        MainActivity.isHospital = true;
                        MainActivity.hospitalData = hospital;
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
}