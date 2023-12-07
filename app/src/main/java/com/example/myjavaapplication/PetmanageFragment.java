package com.example.myjavaapplication;

import static com.example.myjavaapplication.R.color.whitegreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class PetmanageFragment extends Fragment implements View.OnClickListener{
    MainActivity mainActivity;

    private Button petStatusButton,  petChangeButton;
    private TextView name, weight, feed, calorie, months;
    private TextView Mcalorie, Tcalorie, Wcalorie, Thcalorie,  Fcalorie, Sacalorie, Sucalorie;
    private TextView hospital, hname;
    private TextView hospitalCheckText, leftParse, rightParse;
    private View monStatusBox, tueStatusBox, wedStatusBox, thuStatusBox, friStatusBox, satStatusBox, sunStatusBox;
    private CircleImageView petView;
    private View petImageView;
    private ConstraintLayout petManageMentLayout;
    private final long SMALL = 21;
    private final long MIDDLE = 22;
    private final long BIG = 23;
    private final long GOOD = 41;
    private final long NORMAL = 42;
    private final long WORSE = 43;

    private final String WHITERED = "#EDA399";
    private final String WHITEGREEN = "#ACE997";
    private final String WHITEYELLOW = "#F1EEA1";
    private ArrayList<PetChangeData> list = new ArrayList<>();
    private ArrayList<PetMedia> petDataList;
    private UserMedia userData;
    private PetMedia petData;
    private HospitalMedia hospitalData;
    private int petPostion;
    private WeekStatusData weekStatus;

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

        petView = view.findViewById(R.id.petManagementView);
        petImageView = view.findViewById(R.id.petMangementImageView);

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
        hospitalCheckText = view.findViewById(R.id.petManagementCheckHospitalText);
        leftParse = view.findViewById(R.id.leftParse);
        rightParse = view.findViewById(R.id.rightParse);

        monStatusBox = view.findViewById(R.id.petManagementDayMonBox);
        tueStatusBox = view.findViewById(R.id.petManagementDayTueBox);
        wedStatusBox = view.findViewById(R.id.petManagementDayWedBox);
        thuStatusBox = view.findViewById(R.id.petManagementDayThuBox);
        friStatusBox = view.findViewById(R.id.petManagementDayFriBox);
        satStatusBox = view.findViewById(R.id.petManagementDaySatBox);
        sunStatusBox = view.findViewById(R.id.petManagementDaySunBox);
        petManageMentLayout = view.findViewById(R.id.petManagementDayLayout);


        Calendar now = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        String m  = df.format(now.get(Calendar.MONTH) + 1);

        months.setText(m);
        Mcalorie.setText("00Kcal");
        Tcalorie.setText("00Kcal");
        Wcalorie.setText("00Kcal");
        Thcalorie.setText("00Kcal");
        Fcalorie.setText("00Kcal");
        Sacalorie.setText("00Kcal");
        Sucalorie.setText("00Kcal");
        hospital.setVisibility(View.INVISIBLE);
        hname.setVisibility(View.INVISIBLE);
        leftParse.setVisibility(View.INVISIBLE);
        rightParse.setVisibility(View.INVISIBLE);
        hospitalCheckText.setVisibility(View.VISIBLE);

        petChangeButton.setOnClickListener(this);
        petStatusButton.setOnClickListener(this);
        petManageMentLayout.setOnClickListener(this);


        userData = new UserMedia();
        petDataList = new ArrayList<>();
        weekStatus = new WeekStatusData();
        hospitalData = new HospitalMedia();

        userData = (UserMedia) getActivity().getIntent().getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) getActivity().getIntent().getSerializableExtra("petDataList");


        petPostion = MainActivity.petPosition;
        petData = new PetMedia();
        if (petDataList.size() > 0) {
            petData = petDataList.get(petPostion);
            setPagePetData(petData);
            getHospitalDataToFire(petData);

            int day = now.get(Calendar.DAY_OF_WEEK);
            setWeekStatus(day);

            petView.setImageResource(R.drawable.pet_temp_image);
            petImageView.setVisibility(View.INVISIBLE);
        } else {
            name.setText("반려동물을 추가해주세요.");
            weight.setText("0Kg");
            feed.setText("사료");
            calorie.setText("00Kcal");
            hospital.setVisibility(View.INVISIBLE);
            hname.setVisibility(View.INVISIBLE);
            leftParse.setVisibility(View.INVISIBLE);
            rightParse.setVisibility(View.INVISIBLE);
            hospitalCheckText.setText("반려동물을 추가해주세요.");
            hospitalCheckText.setVisibility(View.VISIBLE);
        }
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
                        public void onChangeSelected(String data, int position) {
                            petPostion = position;
                            MainActivity.petPosition = position;
                            petData = petDataList.get(petPostion);
                            setPagePetData(petData);
                            getHospitalDataToFire(petData);

                            Calendar now = Calendar.getInstance();
                            int day = now.get(Calendar.DAY_OF_WEEK);
                            setWeekStatus(day);
                        }
                    }
            );
            dialog.show();
        }
        if(v == petStatusButton){
            if(petDataList.size() > 0 ){
                MainActivity.petPosition = petPostion;

                mainActivity.onChangeToPetStatusFragment();
            }
            else{
                Toast.makeText(getContext(), "펫을 먼저 등록해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == petManageMentLayout){
            Intent intent = new Intent(getActivity(), DaywalkActivity.class);
            intent.putExtra("weekStatus", weekStatus);
            intent.putExtra("petDataList", petDataList);
            intent.putExtra("petPosition", petPostion);
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

    public void setPagePetData(PetMedia data){
        String petName = data.getPetName();
        String petWeight = String.valueOf(data.getPetWeight());
        String feedName = data.getPetFeed();
        String feedCalorie = String.valueOf(data.getPetFeedCalorie());

        name.setText(petName);
        weight.setText(petWeight + "Kg");
        feed.setText(feedName);
        calorie.setText(feedCalorie + "kcal");
        hospital.setText("SJP 동물병원");
        hname.setText("박소정");
        hospital.setVisibility(View.VISIBLE);
        hname.setVisibility(View.VISIBLE);
        leftParse.setVisibility(View.VISIBLE);
        rightParse.setVisibility(View.VISIBLE);
        hospitalCheckText.setVisibility(View.INVISIBLE);
    }


    public void setWeekStatus(int day){
        Calendar cal = Calendar.getInstance();
        Calendar nowTime = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        String month  = df.format(nowTime.get(Calendar.MONTH) + 1);
        String year = String.valueOf(nowTime.get(Calendar.YEAR));
        String mon = "";

        if(day == 1){
            cal.add(Calendar.DATE, -6);
            mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        }
        else if (day == 2){
            mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        }
        else if(day == 3){
            cal.add(Calendar.DATE, -1);
            mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        }
        else if(day == 4){
            cal.add(Calendar.DATE, -2);
            mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        }
        else if(day == 5){
            cal.add(Calendar.DATE, -3);
            mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        }
        else if(day == 6){
            cal.add(Calendar.DATE, -4);
            mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        }
        else if(day == 7){
            cal.add(Calendar.DATE, -5);
            mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        }

        String monday = year + month + mon;

        cal.add(Calendar.DATE, 1);
        String tue = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        String tuesday = year + month + tue;

        cal.add(Calendar.DATE, 1);
        String wed = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        String wednesday = year + month + wed;

        cal.add(Calendar.DATE, 1);
        String thu = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        String thursday = year + month + thu;

        cal.add(Calendar.DATE, 1);
        String fri = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        String friday = year + month + fri;

        cal.add(Calendar.DATE, 1);
        String sat = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        String saturday = year + month + sat;

        cal.add(Calendar.DATE, 1);
        String sun = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        String sunday = year + month + sun;

        String id = userData.getUid();
        String petId = String.valueOf(petDataList.get(petPostion).getPetId());
        long kind = petDataList.get(petPostion).getPetKind();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRefm = db.collection("users").document(id)
                .collection("pet").document(petId).collection("walk").document(monday);
        docRefm.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();
                        ArrayList<Map<String, Object>> tempList = (ArrayList<Map<String, Object>>) map.get("walkList");

                        double dayTotal = 0;
                        for (int i = 0; i < tempList.size(); i++) {
                            Map<String, Object> temp = tempList.get(i);
                            dayTotal += Double.valueOf((String) temp.get("duringTime"));
                        }

                        if (kind == SMALL) {
                            if (dayTotal < 10 || dayTotal > 40) {
                                monStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setMonday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                monStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setMonday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                monStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setMonday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                monStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setMonday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                monStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setMonday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                monStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setMonday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                monStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setMonday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                monStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setMonday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                monStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setMonday(GOOD);
                            }
                        }

                        if(day == 2){
                            MainActivity.petTodayStatus = weekStatus.getMonday();
                        }
                    }
                    else{
                        weekStatus.setMonday(WORSE);
                        monStatusBox.setBackgroundResource(R.drawable.status_red);
                        if(day == 2){
                            MainActivity.petTodayStatus = WORSE;
                        }
                    }
                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docReft = db.collection("users").document(id)
                .collection("pet").document(petId).collection("walk").document(tuesday);
        docReft.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();
                        ArrayList<Map<String, Object>> tempList = (ArrayList<Map<String, Object>>) map.get("walkList");

                        double dayTotal = 0;
                        for (int i = 0; i < tempList.size(); i++) {
                            Map<String, Object> temp = tempList.get(i);
                            dayTotal += Double.valueOf((String) temp.get("duringTime"));
                        }

                        if (kind == SMALL) {
                            if (dayTotal < 10 || dayTotal > 40) {
                                tueStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setTuesday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                tueStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setTuesday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                tueStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setTuesday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                tueStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setTuesday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                tueStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setTuesday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                tueStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setTuesday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                tueStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setTuesday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                tueStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setTuesday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                tueStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setTuesday(GOOD);
                            }
                        }
                        if(day == 3){
                            MainActivity.petTodayStatus = weekStatus.getTuesday();
                        }
                    }
                    else{
                        weekStatus.setTuesday(WORSE);
                        tueStatusBox.setBackgroundResource(R.drawable.status_red);
                        if(day == 3){
                            MainActivity.petTodayStatus = WORSE;
                        }
                    }
                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docRefw = db.collection("users").document(id)
                .collection("pet").document(petId).collection("walk").document(wednesday);
        docRefw.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();
                        ArrayList<Map<String, Object>> tempList = (ArrayList<Map<String, Object>>) map.get("walkList");

                        double dayTotal = 0;
                        for (int i = 0; i < tempList.size(); i++) {
                            Map<String, Object> temp = tempList.get(i);
                            dayTotal += Double.valueOf((String) temp.get("duringTime"));
                        }

                        if (kind == SMALL) {
                            if (dayTotal < 10 || dayTotal > 40) {
                                wedStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setWednesday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                wedStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setWednesday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                wedStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setWednesday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                wedStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setWednesday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                wedStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setWednesday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                wedStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setWednesday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                wedStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setWednesday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                wedStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setWednesday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                wedStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setWednesday(GOOD);
                            }
                        }
                        if(day == 4){
                            MainActivity.petTodayStatus = weekStatus.getWednesday();
                        }
                    }
                    else{
                        weekStatus.setWednesday(WORSE);
                        wedStatusBox.setBackgroundResource(R.drawable.status_red);
                        if(day == 4){
                            MainActivity.petTodayStatus = WORSE;
                        }
                    }
                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docRefth = db.collection("users").document(id)
                .collection("pet").document(petId).collection("walk").document(thursday);
        docRefth.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();
                        ArrayList<Map<String, Object>> tempList = (ArrayList<Map<String, Object>>) map.get("walkList");

                        double dayTotal = 0;
                        for (int i = 0; i < tempList.size(); i++) {
                            Map<String, Object> temp = tempList.get(i);
                            dayTotal += Double.valueOf((String) temp.get("duringTime"));
                        }

                        if (kind == SMALL) {
                            if (dayTotal < 10 || dayTotal > 40) {
                                thuStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setThursday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                thuStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setThursday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                thuStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setThursday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                thuStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setThursday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                thuStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setThursday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                thuStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setThursday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                thuStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setThursday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                thuStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setThursday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                thuStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setThursday(GOOD);
                            }
                        }
                        if(day == 5){
                            MainActivity.petTodayStatus = weekStatus.getThursday();
                        }
                    }
                    else{
                        weekStatus.setThursday(WORSE);
                        thuStatusBox.setBackgroundResource(R.drawable.status_red);
                        if(day == 5){
                            MainActivity.petTodayStatus = WORSE;
                        }
                    }
                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docReff = db.collection("users").document(id)
                .collection("pet").document(petId).collection("walk").document(friday);
        docReff.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();
                        ArrayList<Map<String, Object>> tempList = (ArrayList<Map<String, Object>>) map.get("walkList");

                        double dayTotal = 0;
                        for (int i = 0; i < tempList.size(); i++) {
                            Map<String, Object> temp = tempList.get(i);
                            dayTotal += Double.valueOf((String) temp.get("duringTime"));
                        }

                        if (kind == SMALL) {
                            if (dayTotal < 10 || dayTotal > 40) {
                                friStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setFriday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                friStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setFriday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                friStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setFriday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                friStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setFriday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                friStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setFriday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                friStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setFriday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                friStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setFriday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                friStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setFriday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                friStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setFriday(GOOD);
                            }
                        }
                        if(day == 6){
                            MainActivity.petTodayStatus = weekStatus.getSaturday();
                        }
                    }
                    else{
                        weekStatus.setFriday(WORSE);
                        friStatusBox.setBackgroundResource(R.drawable.status_red);
                        if(day == 6){
                            MainActivity.petTodayStatus = WORSE;
                        }
                    }
                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docRefsa = db.collection("users").document(id)
                .collection("pet").document(petId).collection("walk").document(saturday);
        docRefsa.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();
                        ArrayList<Map<String, Object>> tempList = (ArrayList<Map<String, Object>>) map.get("walkList");

                        double dayTotal = 0;
                        for (int i = 0; i < tempList.size(); i++) {
                            Map<String, Object> temp = tempList.get(i);
                            dayTotal += Double.valueOf((String) temp.get("duringTime"));
                        }

                        if (kind == SMALL) {
                            if (dayTotal < 10 || dayTotal > 40) {
                                satStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setSaturday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                satStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setSaturday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                satStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setSaturday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                satStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setSaturday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                satStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setSaturday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                satStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setSaturday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                satStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setSaturday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                satStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setSaturday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                satStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setSaturday(GOOD);
                            }
                        }
                        if(day == 1){
                            MainActivity.petTodayStatus = weekStatus.getSunday();
                        }
                    }
                    else{
                        weekStatus.setSaturday(WORSE);
                        satStatusBox.setBackgroundResource(R.drawable.status_red);
                        if(day == 1){
                            MainActivity.petTodayStatus = weekStatus.getSunday();
                        }
                    }
                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docRefs = db.collection("users").document(id)
                .collection("pet").document(petId).collection("walk").document(sunday);
        docRefs.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();
                        ArrayList<Map<String, Object>> tempList = (ArrayList<Map<String, Object>>) map.get("walkList");

                        double dayTotal = 0;
                        for (int i = 0; i < tempList.size(); i++) {
                            Map<String, Object> temp = tempList.get(i);
                            dayTotal += Double.valueOf((String) temp.get("duringTime"));
                        }

                        if (kind == SMALL) {
                            if (dayTotal < 10 || dayTotal > 40) {
                                sunStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setSunday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                sunStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setSunday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                sunStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setSunday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                sunStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setSunday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                sunStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setSunday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                sunStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setSunday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                sunStatusBox.setBackgroundResource(R.drawable.status_red);
                                weekStatus.setSunday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                sunStatusBox.setBackgroundResource(R.drawable.status_yellow);
                                weekStatus.setSunday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                sunStatusBox.setBackgroundResource(R.drawable.status_green);
                                weekStatus.setSunday(GOOD);
                            }
                        }
                    }
                    else{
                        weekStatus.setSunday(WORSE);
                        sunStatusBox.setBackgroundResource(R.drawable.status_red);
                    }
                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                }
            }
        });

    }

    public void getHospitalDataToFire (PetMedia pet){
        String id = pet.getuId();
        String petId = String.valueOf(pet.getPetId());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("hospital").document(id)
                .collection("pet").document(petId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = document.getData();

                        hospitalData.setUid(petData.getuId());
                        hospitalData.setId(petData.getPetId());
                        hospitalData.setName((String) map.get("name"));
                        hospitalData.setDoctor((String) map.get("doctor"));
                        hospitalData.setAddress((String) map.get("address"));
                        hospitalData.setNumber((String) map.get("number"));
                        hospitalData.setFeature((String) map.get("feature"));
                        MainActivity.hospitalData = hospitalData;
                        MainActivity.isHospital = true;

                        hospital.setText(hospitalData.getName());
                        hname.setText(hospitalData.getDoctor());
                        hospital.setVisibility(View.VISIBLE);
                        hname.setVisibility(View.VISIBLE);
                        hospitalCheckText.setVisibility(View.INVISIBLE);
                    }
                    else{
                        hospitalData = new HospitalMedia();
                        MainActivity.hospitalData = hospitalData;
                        MainActivity.isHospital = false;

                        hospital.setVisibility(View.INVISIBLE);
                        hname.setVisibility(View.INVISIBLE);
                        leftParse.setVisibility(View.INVISIBLE);
                        rightParse.setVisibility(View.INVISIBLE);
                        hospitalCheckText.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                    hospitalData = new HospitalMedia();
                    MainActivity.hospitalData = hospitalData;

                    hospital.setVisibility(View.INVISIBLE);
                    hname.setVisibility(View.INVISIBLE);
                    leftParse.setVisibility(View.INVISIBLE);
                    rightParse.setVisibility(View.INVISIBLE);
                    hospitalCheckText.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}