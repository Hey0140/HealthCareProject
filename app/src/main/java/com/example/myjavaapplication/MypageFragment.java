package com.example.myjavaapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MypageFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth auth;

    private ArrayList<MyPetInfoData> list= new ArrayList<>();

    private Button logoutButton;

    private UserMedia userData;
    private ArrayList<PetMedia> petDataList;
    private TextView profileName, profileEmail;
    private ImageView profileImage;
    private int petCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        logoutButton = view.findViewById(R.id.logoutButton);
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileImage = view.findViewById(R.id.profileImage);

        logoutButton.setOnClickListener(this);

        petDataList = new ArrayList<>();

        userData = (UserMedia) this.getArguments().getSerializable("userData");
        petDataList = (ArrayList<PetMedia>) this.getArguments().getSerializable("petDataList");

        if(userData.getImage() != null){
            if(!userData.getImage().equals("")){
                profileImage.setImageURI(Uri.parse(userData.getImage()));
            }
        }
        profileName.setText(userData.getName());
        profileEmail.setText(userData.getEmail());

        list.clear();

        petCount = userData.getCount();
        for (int i = 0; i < petCount; i++) {
            MyPetInfoData mpid = new MyPetInfoData();
            PetMedia petMedia = petDataList.get(i);
            mpid.setName(petMedia.getPetName());
            mpid.setImageId(R.drawable.mypetprofile_basic_icon);
            mpid.setViewType(Code.ViewType.BASIC);
            list.add(mpid);
        }

        lastAddData();

        RecyclerView recyclerView = view.findViewById(R.id.myPetInfoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        MypetInfoAdapter adapter = new MypetInfoAdapter(list);
        adapter.setOnItemClickListener(new MypetInfoAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, int vg) {
                        if(vg == Code.ViewType.BASIC){
                            Toast.makeText(getContext(), position+"BASIC", Toast.LENGTH_SHORT).show();
                        }
                        if(vg == Code.ViewType.ADD){
                            Intent intent = new Intent(getActivity(), PetAddActivity.class);
                            intent.putExtra("userData", userData);
                            startActivity(intent);
                        }
            }
        });
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