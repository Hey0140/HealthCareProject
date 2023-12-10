package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DaywalkActivity extends AppCompatActivity {

    TextView months, walkText, walkHour, calorie, leftCalorie;
    TextView monText, tueText, wedText, thuText, friText, satText, sunText;
    View monBox, tueBox, wedBox, thuBox, friBox, satBox, sunBox;
    private WeekStatusData weekStatus = new WeekStatusData();
    private ArrayList<PetMedia> petDataList = new ArrayList<>();
    private PetMedia petData = new PetMedia();
    private int petPosition = 0;
    private final long GOOD = 41;
    private final long NORMAL = 42;
    private final long WORSE = 43;
    private final long MALE = 11;
    private final long FEMALE = 12;
    private final long MALENEUTER = 13;
    private final long FEMALENEUTER = 14;
    private double walkAllTime = 0.0;
    private double needCalorie = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daywalk_page);

        Intent intent = getIntent();
        weekStatus = (WeekStatusData) intent.getSerializableExtra("weekStatus");
        petDataList = (ArrayList<PetMedia>) intent.getSerializableExtra("petDataList");
        petPosition = intent.getIntExtra("petPosition",0);

        months = findViewById(R.id.petDayWalkMonth);
        monText = findViewById(R.id.petDayWalkMonText);
        tueText = findViewById(R.id.petDayWalkTueText);
        wedText = findViewById(R.id.petDayWalkWedText);
        thuText = findViewById(R.id.petDayWalkThuText);
        friText = findViewById(R.id.petDayWalkFriText);
        satText = findViewById(R.id.petDayWalkSatText);
        sunText = findViewById(R.id.petDayWalkSunText);

        monBox = findViewById(R.id.petDayWalkMonBox);
        tueBox = findViewById(R.id.petDayWalkTueBox);
        wedBox = findViewById(R.id.petDayWalkWedBox);
        thuBox = findViewById(R.id.petDayWalkThuBox);
        friBox = findViewById(R.id.petDayWalkFriBox);
        satBox = findViewById(R.id.petDayWalkSatBox);
        sunBox = findViewById(R.id.petDayWalkSunBox);

        walkHour = findViewById(R.id.dayWalkPageWalkHour);
        walkText = findViewById(R.id.dayWalkPageWalkNumber);
        calorie = findViewById(R.id.dayWalkPageCalorie);
        leftCalorie = findViewById(R.id.dayWalkPageLeftCalorie);

        if(petDataList.size() > 0){
            petData = petDataList.get(petPosition);

            long rer = petData.getPetWeight() * 30 + 70;
            if (petData.getPetSex() == MALE || petData.getPetSex() == FEMALE) {
                needCalorie = rer * 1.8;
            }
            if (petData.getPetSex() == MALENEUTER || petData.getPetSex() == FEMALENEUTER) {
                needCalorie = rer * 1.6;
            }
            String calorieName = String.valueOf(needCalorie);
            leftCalorie.setText(calorieName);

            long walk =  petData.getWalk();
            walkText.setText(String.valueOf(walk));

            getWalkDataToFirestore(petData);
        }
        else{
            leftCalorie.setText("0.0");
            walkText.setText("0");
        }
        walkHour.setText("0.0");

        setCalender();
        setWeekStatus();
    }

    private void setWeekStatus() {
        long mon = weekStatus.getMonday();
        long tue = weekStatus.getTuesday();
        long wed = weekStatus.getWednesday();
        long thu = weekStatus.getThursday();
        long fri = weekStatus.getFriday();
        long sat = weekStatus.getSaturday();
        long sun = weekStatus.getSunday();
        if( mon == GOOD){
            monBox.setBackgroundResource(R.drawable.status_green);
        } else if (mon == NORMAL) {
            monBox.setBackgroundResource(R.drawable.status_yellow);
        } else if ( mon == WORSE){
            monBox.setBackgroundResource(R.drawable.status_red);
        }

        if( tue == GOOD){
            tueBox.setBackgroundResource(R.drawable.status_green);
        } else if (tue == NORMAL) {
            tueBox.setBackgroundResource(R.drawable.status_yellow);
        } else if ( tue == WORSE){
            tueBox.setBackgroundResource(R.drawable.status_red);
        }

        if( wed == GOOD){
            wedBox.setBackgroundResource(R.drawable.status_green);
        } else if (wed == NORMAL) {
            wedBox.setBackgroundResource(R.drawable.status_yellow);
        } else if ( wed == WORSE){
            wedBox.setBackgroundResource(R.drawable.status_red);
        }

        if( thu == GOOD){
            thuBox.setBackgroundResource(R.drawable.status_green);
        } else if (thu == NORMAL) {
            thuBox.setBackgroundResource(R.drawable.status_yellow);
        } else if ( thu == WORSE){
            thuBox.setBackgroundResource(R.drawable.status_red);
        }

        if( fri == GOOD){
            friBox.setBackgroundResource(R.drawable.status_green);
        } else if (fri == NORMAL) {
            friBox.setBackgroundResource(R.drawable.status_yellow);
        } else if ( fri == WORSE){
            friBox.setBackgroundResource(R.drawable.status_red);
        }

        if( sat == GOOD){
            satBox.setBackgroundResource(R.drawable.status_green);
        } else if (sat == NORMAL) {
            satBox.setBackgroundResource(R.drawable.status_yellow);

        } else if ( sat == WORSE){
            satBox.setBackgroundResource(R.drawable.status_red);

        }

        if( sun == GOOD){
            sunBox.setBackgroundResource(R.drawable.status_green);

        } else if (sun == NORMAL) {
            sunBox.setBackgroundResource(R.drawable.status_yellow);

        } else if ( sun == WORSE){
            sunBox.setBackgroundResource(R.drawable.status_red);
        }

    }


    public void setCalender(){
        Calendar now = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        String m  = df.format(now.get(Calendar.MONTH) + 1);
        months.setText(m);

        int day = now.get(Calendar.DAY_OF_WEEK);

        Calendar cal = Calendar.getInstance();
        if(day == 1){
            cal.add(Calendar.DATE, -6);
            String mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
            monText.setText(mon);
        }
        else if (day == 2){
            String mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
            monText.setText(mon);
        }
        else if(day == 3){
            cal.add(Calendar.DATE, -1);
            String mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
            monText.setText(mon);
        }
        else if(day == 4){
            cal.add(Calendar.DATE, -2);
            String mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
            monText.setText(mon);
        }
        else if(day == 5){
            cal.add(Calendar.DATE, -3);
            String mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
            monText.setText(mon);
        }
        else if(day == 6){
            cal.add(Calendar.DATE, -4);
            String mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
            monText.setText(mon);
        }
        else if(day == 7){
            cal.add(Calendar.DATE, -5);
            String mon = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
            monText.setText(mon);
        }

        cal.add(Calendar.DATE, 1);
        String tue = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        tueText.setText(tue);

        cal.add(Calendar.DATE, 1);
        String wed = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        wedText.setText(wed);

        cal.add(Calendar.DATE, 1);
        String thu = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        thuText.setText(thu);

        cal.add(Calendar.DATE, 1);
        String fri = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        friText.setText(fri);

        cal.add(Calendar.DATE, 1);
        String sat = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        satText.setText(sat);

        cal.add(Calendar.DATE, 1);
        String sun = cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE): String.valueOf(cal.get(Calendar.DATE));
        sunText.setText(sun);

        months.setText(m);
    }

    public void getWalkDataToFirestore(PetMedia pet){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = pet.getuId();

        if(petDataList.size() > 0){
            String petId = String.valueOf(pet.getPetId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar c1 = Calendar.getInstance();
            String strToday = sdf.format(c1.getTime());
            Log.i("strToday", strToday);

            DocumentReference docRef = db.collection("users").document(id)
                    .collection("pet").document(petId).collection("walk").document(strToday);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                            Map<String, Object> map = document.getData();
                            ArrayList<Map<String, Object>> tempList = (ArrayList<Map<String, Object>>) map.get("walkList");

                            for (int i = 0; i < tempList.size(); i++) {
                                Map<String, Object> temp = tempList.get(i);
                                String tempTime = (String) temp.get("duringTime");
                                walkAllTime += Double.valueOf(tempTime);
                            }
                            DecimalFormat df = new DecimalFormat("0.0");

                            walkHour.setText(df.format(walkAllTime));
                        }
                        else{
                            walkHour.setText("0.0");
                        }

                    } else {
                        Log.d("Firebase", "get failed with ", task.getException());
                    }
                }
            });

        }
        else{
            Log.i("HomeFragment", "펫이 없음");
        }
    }

}