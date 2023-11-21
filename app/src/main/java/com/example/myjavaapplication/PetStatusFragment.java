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

    private View arrow, backButton;
    private EditText note;
    private View back;
    private TextView name;
    private TextView hospitalname;
    private TextView address;
    private View petStatusLeftView;
    private TextView petStatusLeftText;
    private View petStatusCenterView;
    private TextView petStatusCenterText;
    private View petStatusRightView;
    private TextView petStatusRightText;
    private TextView petStatusWeight;
    private TextView petStatusActivity;
    private TextView petStatusHeart;
    private Button end;

    public PetStatusFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.petstatus_page, container, false);

        backButton = view.findViewById(R.id.petStatusBackButton);

        backButton.setOnClickListener(this);
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
        end.setOnClickListener(this);

        note.setText("입력하세요");
        name.setText("초코");
        hospitalname.setText("SJP병원");
        address.setText("서울시 동작구 상도로 123길");
        petStatusLeftView.setBackgroundColor(Color.parseColor(String.valueOf(R.color.whitegreen)));
        petStatusLeftText.setVisibility(View.INVISIBLE);
        petStatusCenterView.setBackgroundColor(Color.parseColor(String.valueOf(R.color.whitegreen)));
        petStatusCenterText.setVisibility(View.VISIBLE);
        petStatusRightView.setBackgroundColor(Color.parseColor(String.valueOf(R.color.whitegreen)));
        petStatusRightText.setVisibility(View.INVISIBLE);
        petStatusWeight.setText("8Kg");
        petStatusActivity.setText("2000cal");
        petStatusHeart.setText("200bpm");
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
    }

}