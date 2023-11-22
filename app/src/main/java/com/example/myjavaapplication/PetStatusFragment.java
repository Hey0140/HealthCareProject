package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PetStatusFragment extends Fragment implements View.OnClickListener {
    private MainActivity mainActivity;

    private View backButton, hospitalInfo;
    private EditText note;
    private View petStatusLeftView, petStatusCenterView, petStatusRightView;
    private TextView name, hospitalname, address;
    private TextView petStatusRightText, petStatusWeight, petStatusCenterText;
    private TextView petStatusActivity, petStatusHeart, petStatusLeftText;
    private Button end;

    private final String WHITERED = "#EDA399";
    private final String WHITEGREEN = "#ACE997";
    private final String WHITEYELLOW = "#F1EEA1";

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
        petStatusHeart=view.findViewById(R.id.petStatusHeart);
        end=view.findViewById(R.id.petFeatureEndButton);
        hospitalInfo = view.findViewById(R.id.petHospitalInfoButton);

        end.setOnClickListener(this);
        hospitalInfo.setOnClickListener(this);
        backButton.setOnClickListener(this);

        note.setText("입력하세요");
        name.setText("초코");
        hospitalname.setText("SJP병원");
        address.setText("서울시 동작구 상도로 123길");
        petStatusLeftView.setBackgroundColor(Color.parseColor(WHITEGREEN));
        petStatusLeftText.setVisibility(View.INVISIBLE);
        petStatusCenterView.setBackgroundColor(Color.parseColor(WHITEGREEN));
        petStatusCenterText.setVisibility(View.VISIBLE);
        petStatusRightView.setBackgroundColor(Color.parseColor(WHITEGREEN));
        petStatusRightText.setVisibility(View.INVISIBLE);
        petStatusWeight.setText("8");
        petStatusActivity.setText("2000");
        petStatusHeart.setText("200");
        end.setText("완료");


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
        }
    }

}