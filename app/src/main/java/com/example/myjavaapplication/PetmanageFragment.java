package com.example.myjavaapplication;

import static com.example.myjavaapplication.R.color.whitegreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;


public class PetmanageFragment extends Fragment implements View.OnClickListener{
    MainActivity mainActivity;

    private Button petStatusButton,  petChangeButton;
    private TextView name, weight, feed, calorie, months;
    private TextView Mcalorie, Tcalorie, Wcalorie, Thcalorie,  Fcalorie, Sacalorie, Sucalorie;
    private TextView hospital, hname;
    private View monStatusBox, tueStatusBox, wedStatusBox, thuStatusBox, friStatusBox, satStatusBox, sunStatusBox;
    private ConstraintLayout petManageMentLayout;

    private final String WHITERED = "#EDA399";
    private final String WHITEGREEN = "#ACE997";
    private final String WHITEYELLOW = "#F1EEA1";
    private ArrayList<PetChangeData> list = new ArrayList<>();
    private ArrayList<PetMedia> petDataList;
    private UserMedia userData;

    public PetmanageFragment() {
        // Required empty public constructor
    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        list = new ArrayList<>();
//        PetChangeData petChangeData = new PetChangeData();
//        petChangeData.setName("choco");
//        list.add(petChangeData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_petmanage, container, false);

        petStatusButton = view.findViewById(R.id.petManagementStatusButton);
        petChangeButton = view.findViewById(R.id.petManagementChangeButton);

        name = view.findViewById(R.id.petManagementName);
        weight = view.findViewById(R.id.petManagementWeightText);
        feed = view.findViewById(R.id.petManagementFeedText);
        calorie = view.findViewById(R.id.petManagementCalorieText);
        months = view.findViewById(R.id.petManagementMonth);
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
        petManageMentLayout = view.findViewById(R.id.petManagementDayLayout);



        name.setText("초코");
        weight.setText("5Kg");
        feed.setText("고급 사료");
        calorie.setText("3,500Kcal/Kg");
        months.setText("11");
        Mcalorie.setText("2400Kcal");
        Tcalorie.setText("3200Kcal");
        Wcalorie.setText("1800Kcal");
        Thcalorie.setText("2800Kcal");
        Fcalorie.setText("2150Kcal");
        Sacalorie.setText("1530Kcal");
        Sucalorie.setText("2000Kcal");
        hospital.setText("SJP 동물병원");
        hname.setText("박소정");
        monStatusBox.setBackgroundColor(Color.parseColor(WHITEGREEN));
        tueStatusBox.setBackgroundColor(Color.parseColor(WHITEGREEN));
        wedStatusBox.setBackgroundColor(Color.parseColor(WHITEGREEN));
        thuStatusBox.setBackgroundColor(Color.parseColor(WHITEGREEN));
        friStatusBox.setBackgroundColor(Color.parseColor(WHITEGREEN));
        satStatusBox.setBackgroundColor(Color.parseColor(WHITEGREEN));
        sunStatusBox.setBackgroundColor(Color.parseColor(WHITEGREEN));

        petChangeButton.setOnClickListener(this);
        petStatusButton.setOnClickListener(this);
        petManageMentLayout.setOnClickListener(this);


        userData = new UserMedia();
        petDataList = new ArrayList<>();

        userData = (UserMedia) getActivity().getIntent().getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) getActivity().getIntent().getSerializableExtra("petDataList");

        list.clear();
        setPetChangedList();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == petChangeButton){
            PetChangeDialog dialog = new PetChangeDialog(getContext(), list);
            dialog.setChangeDialogListener(
                    new PetChangeDialog.OnChangeDialogListener() {
                        @Override
                        public void onChangeSelected(String data) {
                            name.setText(data);
                        }
                    }
            );
            dialog.show();
        }
        if(v == petStatusButton){
            mainActivity.onChangeToPetStatusFragment();
        }
        if(v == petManageMentLayout){
            Intent intent = new Intent(getActivity(), DaywalkActivity.class);
            startActivity(intent);
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

    public void setPetChangedList(){
        for(int i = 0; i < petDataList.size(); i++){
            PetChangeData petChangeData = new PetChangeData();
            petChangeData.setName(petDataList.get(i).getPetName());
            list.add(petChangeData);
        }
    }

}