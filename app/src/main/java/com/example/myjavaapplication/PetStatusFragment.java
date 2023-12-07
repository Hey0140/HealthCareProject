package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class PetStatusFragment extends Fragment implements View.OnClickListener {
    private MainActivity mainActivity;

    private View backButton, hospitalInfo;
    private EditText note;
    private View petStatusLeftView, petStatusCenterView, petStatusRightView;
    private TextView name, hospitalname, address;
    private TextView petStatusRightText, petStatusWeight, petStatusCenterText;
    private TextView petStatusActivity, petStatusHeart, petStatusLeftText, showText;
    private Button end;

    private final long GOOD = 41;
    private final long NORMAL = 42;
    private final long WORSE = 43;
    private final long MALE = 11;
    private final long FEMALE = 12;
    private final long MALENEUTER = 13;
    private final long FEMALENEUTER = 14;
    private double needCalorie = 0;
    private boolean isHospital = false;

    private ArrayList<PetMedia> petDataList = new ArrayList<>();
    private PetMedia petData = new PetMedia();
    private UserMedia userData = new UserMedia();
    private int petPosition = 0;

    public PetStatusFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_petstatus, container, false);

        backButton = view.findViewById(R.id.petStatusBackButton);

        note = view.findViewById(R.id.petFeature);
        name = view.findViewById(R.id.petNameTitle);
        hospitalname = view.findViewById(R.id.petHospitalName);
        address = view.findViewById(R.id.petHospitalAddress);

        petStatusLeftView = view.findViewById(R.id.petStatusLeftView);
        petStatusLeftText = view.findViewById(R.id.petStatusLeftText);
        petStatusCenterView = view.findViewById(R.id.petStatusCenterView);
        petStatusCenterText = view.findViewById(R.id.petStatusCenterText);
        petStatusRightView = view.findViewById(R.id.petStatusRightView);
        petStatusRightText = view.findViewById(R.id.petStatusRightText);
        petStatusWeight = view.findViewById(R.id.petStatusWeight);
        petStatusActivity =view.findViewById(R.id.petStatusActivity);
        showText = view.findViewById(R.id.petFeatrueAddText);
        petStatusHeart=view.findViewById(R.id.petStatusHeart);
        end=view.findViewById(R.id.petFeatureEndButton);
        hospitalInfo = view.findViewById(R.id.petHospitalInfoButton);

        end.setOnClickListener(this);
        hospitalInfo.setOnClickListener(this);
        backButton.setOnClickListener(this);
        showText.setOnClickListener(this);

        userData = (UserMedia) getActivity().getIntent().getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) getActivity().getIntent().getSerializableExtra("petDataList");

        petData = petDataList.get(MainActivity.petPosition);



        name.setText(petData.getPetName());
        setPetStatus(MainActivity.petTodayStatus);


        petStatusWeight.setText(String.valueOf(petData.getPetWeight()));

        long rer = petData.getPetWeight() * 30 + 70;
        if (petData.getPetSex() == MALE || petData.getPetSex() == FEMALE) {
            needCalorie = rer * 1.8;
        }
        if (petData.getPetSex() == MALENEUTER || petData.getPetSex() == FEMALENEUTER) {
            needCalorie = rer * 1.6;
        }
        String calorieName = String.valueOf(needCalorie);
        petStatusActivity.setText(calorieName);

        petStatusHeart.setText("00");


        isHospital = MainActivity.isHospital;
        getHospitalData(MainActivity.hospitalData);


        return view;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }


    @Override
    public void onClick(View v) {
        if(v == backButton){
            mainActivity.onChangeToPetManageFragment();
        }
        if(v == hospitalInfo){
            if( isHospital){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("확인창")
                        .setMessage("수정하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getActivity(), HospitalActivity.class);
                                intent.putExtra("petData", petData);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                //병원 등록이 되지 않았을 시
                Intent intent = new Intent(getActivity(), HospitalActivity.class);
                intent.putExtra("petData", petData);
                startActivity(intent);
            }
        }
        if( v == end){
            if (isHospital){
                String feature = note.getText().toString();
                updateFeatureToFirestore(MainActivity.hospitalData, feature);
            } else{
                Toast.makeText(getContext(), "병원 등록부터 해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == showText){
            if (isHospital){
                String fText = showText.getText().toString();
                note.setText(fText);
            }
        }
    }

    public void getHospitalData (HospitalMedia hospital){
        if (isHospital){
            hospitalname.setText(hospital.getName());
            address.setVisibility(View.VISIBLE);
            address.setText(hospital.getAddress());
            showText.setText(hospital.getFeature());
        }
        else{
            hospitalname.setText("병원을 등록해주세요.");
            address.setVisibility(View.INVISIBLE);
        }
    }

    public void setPetStatus(long status){
        if(status == GOOD){
            petStatusLeftText.setVisibility(View.INVISIBLE);
            petStatusCenterText.setVisibility(View.INVISIBLE);
            petStatusRightText.setVisibility(View.VISIBLE);
        } else if (status == NORMAL) {
            petStatusLeftText.setVisibility(View.INVISIBLE);
            petStatusCenterText.setVisibility(View.VISIBLE);
            petStatusRightText.setVisibility(View.INVISIBLE);
        } else if (status == WORSE){
            petStatusLeftText.setVisibility(View.VISIBLE);
            petStatusCenterText.setVisibility(View.INVISIBLE);
            petStatusRightText.setVisibility(View.INVISIBLE);
        }
        else{
            petStatusLeftText.setVisibility(View.INVISIBLE);
            petStatusCenterText.setVisibility(View.INVISIBLE);
            petStatusRightText.setVisibility(View.INVISIBLE);
        }
    }

    public void updateFeatureToFirestore(HospitalMedia hospital, String feature){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = hospital.getUid();
        String petId = String.valueOf(hospital.getId());
        DocumentReference documentReference = db.collection("hospital").document(uid)
                .collection("pet").document(petId);

        documentReference.update("feature", feature)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showText.setText(feature);
                        note.setText("");
                        MainActivity.hospitalData.setFeature(feature);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase", "Error updating document", e);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        isHospital = MainActivity.isHospital;
        getHospitalData(MainActivity.hospitalData);
    }
}