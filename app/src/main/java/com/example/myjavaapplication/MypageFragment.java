package com.example.myjavaapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class MypageFragment extends Fragment implements View.OnClickListener {
    FirebaseAuth auth;

    ArrayList<MyPetInfoData> list= new ArrayList<>();

    Button logoutButton;


//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;
//
//    public HomeFragment() {
//
//    }
//
//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);

        list.clear();

        for (int i = 0; i < 3; i++) {
            MyPetInfoData mpid = new MyPetInfoData();
            mpid.setName("choco");
            mpid.setImageId(R.drawable.mypetprofile_basic_icon);
            mpid.setViewType(Code.ViewType.BASIC);
            list.add(mpid);
        }

        lastAddData();

        RecyclerView recyclerView = view.findViewById(R.id.myPetInfoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        MypetInfoAdapter adapter = new MypetInfoAdapter(list);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void lastAddData(){
        MyPetInfoData mpid = new MyPetInfoData();
        mpid.setViewType(Code.ViewType.ADD);
        list.add(mpid);
    }

    @Override
    public void onClick(View v) {
        if( v == logoutButton){
            auth.signOut();
            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }
}