package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DaywalkActivity extends AppCompatActivity {

    TextView months, walkNumber, walkHour, calorie, leftCalorie;
    TextView monText, tueText, wedText, thuText, friText, satText, sunText;
    View monBox, tueBox, wedBox, thuBox, friBox, satBox, sunBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daywalk_page);

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
        walkNumber = findViewById(R.id.dayWalkPageWalk);
        walkHour = findViewById(R.id.dayWalkPageWalkHour);
        calorie = findViewById(R.id.dayWalkPageCalorie);
        leftCalorie = findViewById(R.id.dayWalkPageLeftCalorie);

        setCalender();

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

}