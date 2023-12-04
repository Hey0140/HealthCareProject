package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
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

import com.google.android.gms.tasks.OnCompleteListener;
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

    private final String WHITERED = "#EDA399";
    private final String WHITEGREEN = "#ACE997";
    private final String WHITEYELLOW = "#F1EEA1";

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


        hospitalname.setText("SJP병원");
        address.setText("");
        address.setVisibility(View.VISIBLE);

        petStatusLeftView.setBackgroundColor(Color.parseColor(WHITEGREEN));
        petStatusLeftText.setVisibility(View.INVISIBLE);
        petStatusCenterView.setBackgroundColor(Color.parseColor(WHITEGREEN));
        petStatusCenterText.setVisibility(View.VISIBLE);
        petStatusRightView.setBackgroundColor(Color.parseColor(WHITEGREEN));
        petStatusRightText.setVisibility(View.INVISIBLE);
        petStatusWeight.setText("8");
        petStatusActivity.setText("2000");
        petStatusHeart.setText("200");
        showText.setText("");


        userData = (UserMedia) getActivity().getIntent().getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) getActivity().getIntent().getSerializableExtra("petDataList");

        petData = petDataList.get(MainActivity.petPosition);
        Log.i("check", petData.getPetName());
        name.setText(petData.getPetName());

        getHospitalDataToFire(petData);


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


        }
        if( v == end){
            String feature = note.getText().toString();
            showText.setText(feature);
            note.setText("");
        }
        if(v == showText){
            String fText = showText.getText().toString();
            note.setText(fText);
        }
    }

    public void getHospitalDataToFire (PetMedia pet){
        String id = pet.getuId();
        String petId = String.valueOf(pet.getPetId());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(id)
                .collection("pet").document(petId).collection("hospital").document("hospital");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();


//                        for (int i = 0; i < tempList.size(); i++) {
//                            Map<String, Object> temp = tempList.get(i);
//                            String tempTime = (String) temp.get("duringTime");
//                            walkAllTime += Double.valueOf(tempTime);
//                        }
//                        DecimalFormat df = new DecimalFormat("0.0");
//
//                        walkHour.setText(df.format(walkAllTime));
//                        walkText.setText(String.valueOf(petData.getWalk()));
                    }
                    else{
                        hospitalname.setText("병원을 등록해주세요.");
                        address.setVisibility(View.INVISIBLE);
                    }

                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                }
            }
        });
    }

}