package com.example.myjavaapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private TextView puppyName;
    private TextView homePageWalkNumber;
    private TextView homePageWalkTime;
    private TextView homePageCalorie;
    private TextView homePageNeedCalorie;
    private Button homePageChangeButton;
    private Button walkStartButton;
    public HomeFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        puppyName=view.findViewById(R.id.puppyName);
        puppyName.setText("초코");
        homePageWalkNumber=view.findViewById(R.id.homePageWalkNumber);
        homePageWalkTime=view.findViewById(R.id.homePageWalkTime);
        homePageCalorie=view.findViewById(R.id.homePageCalorie);
        homePageNeedCalorie=view.findViewById(R.id.homePageNeedCalorie);
        homePageChangeButton=view.findViewById(R.id.homePageChangeButton);
        homePageChangeButton.setOnClickListener(this);
        walkStartButton = view.findViewById(R.id.walkStartButton);
        walkStartButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == homePageChangeButton) {
            Toast.makeText(getContext(), "확인.", Toast.LENGTH_LONG).show();
        }
        if (view== walkStartButton){
            Toast.makeText(getContext(), "확인.", Toast.LENGTH_LONG).show();
        }
    }
}