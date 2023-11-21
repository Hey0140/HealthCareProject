package com.example.myjavaapplication;

import static com.example.myjavaapplication.R.color.whitegreen;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.checkerframework.checker.units.qual.C;


public class PetmanageFragment extends Fragment implements View.OnClickListener{
    MainActivity mainActivity;

    private Button petStatusButton;
    private Button petChangeButton;
    private TextView name;
    private TextView weight;
    private TextView qkq;
    private TextView calorie;
    private TextView months;
    private TextView Mcalorie;
    private TextView Tcalorie;
    private TextView Wcalorie;
    private TextView Thcalorie;
    private TextView Fcalorie;
    private TextView Sacalorie;
    private TextView Sucalorie;
    private TextView hospital;
    private TextView hname;
    private View monStatusBox;
    private View tueStatusBox;
    private View wedStatusBox;
    private View thuStatusBox;
    private View friStatusBox;
    private View satStatusBox;
    private View sunStatusBox;

    public PetmanageFragment() {
        // Required empty public constructor
    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_petmanage, container, false);

        petStatusButton = view.findViewById(R.id.petManagementStatusButton);
        name = view.findViewById(R.id.petManagementName);
        weight = view.findViewById(R.id.petManagementWeightText);
        qkq = view.findViewById(R.id.petManagementFeedText);
        calorie = view.findViewById(R.id.petManagementCalorieText);
        months = view.findViewById(R.id.petManagementMonth);
        monStatusBox = view.findViewById(R.id.petManagementMonth);
        Mcalorie = view.findViewById(R.id.petManagementDayMonCalorie);
        Tcalorie = view.findViewById(R.id.petManagementDayTueCalorie);
        Wcalorie = view.findViewById(R.id.petManagementDayWedCalorie);
        Thcalorie = view.findViewById(R.id.petManagementDayThuCalorie);
        Fcalorie = view.findViewById(R.id.petManagementDayFriCalorie);
        Sacalorie = view.findViewById(R.id.petManagementDaySatCalorie);
        Sucalorie = view.findViewById(R.id.petManagementDaySunCalorie);
        hospital = view.findViewById(R.id.petManagementHospital);
        hname = view.findViewById(R.id.petManagmentDocter);

        monStatusBox = view.findViewById(R.id.petManagementDayMonBox);
        tueStatusBox = view.findViewById(R.id.petManagementDayTueBox);
        wedStatusBox = view.findViewById(R.id.petManagementDayWedBox);
        thuStatusBox = view.findViewById(R.id.petManagementDayThuBox);
        friStatusBox = view.findViewById(R.id.petManagementDayFriBox);
        satStatusBox = view.findViewById(R.id.petManagementDaySatBox);
        sunStatusBox = view.findViewById(R.id.petManagementDaySunBox);


        petChangeButton = view.findViewById(R.id.petManagementChangeButton);

        name.setText("초코");
        weight.setText("5Kg");
        qkq.setText("고급 사료");
        calorie.setText("3,500Kcal/Kg");
        months.setText("11월");
        Mcalorie.setText("2400Kcal");
        Tcalorie.setText("3200Kcal");
        Wcalorie.setText("1800Kcal");
        Thcalorie.setText("2800Kcal");
        Fcalorie.setText("2150Kcal");
        Sacalorie.setText("1530Kcal");
        Sucalorie.setText("2000Kcal");
        hospital.setText("SJP 동물병원");
        hname.setText("박소정");
        monStatusBox.setBackgroundColor(Color.parseColor(String.valueOf(R.color.whitegreen));
        tueStatusBox.setBackgroundColor(Color.parseColor(String.valueOf(R.color.whitegreen)));
        wedStatusBox.setBackgroundColor(Color.parseColor(String.valueOf(R.color.whitegreen)));
        thuStatusBox.setBackgroundColor(Color.parseColor(String.valueOf(R.color.whitegreen)));
        friStatusBox.setBackgroundColor(Color.parseColor(String.valueOf(R.color.whitegreen)));
        satStatusBox.setBackgroundColor(Color.parseColor(String.valueOf(R.color.whitegreen)));
        sunStatusBox.setBackgroundColor(Color.parseColor(String.valueOf(R.color.whitegreen)));

        petChangeButton.setOnClickListener(this);
        petStatusButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == petChangeButton){

        }
        if(v == petStatusButton){
            mainActivity.onChangeToPetStatusFragment();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

}