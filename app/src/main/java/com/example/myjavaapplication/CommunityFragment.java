package com.example.myjavaapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommunityFragment extends Fragment implements View.OnClickListener {
    private MainActivity mActivity;
    private UserMedia userData = new UserMedia();
    private ArrayList<CommunityItemData> itemList = new ArrayList<>();
    private ArrayList<CommunityMedia> communityDataList = new ArrayList<>();
    private int communityPosition = 0;
    private CommunityMedia communityData = new CommunityMedia();
    private CommunityItemAdapter adapter;
    private View createView;


    public CommunityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        createView = view.findViewById(R.id.communityCreateView);

        userData = (UserMedia) getActivity().getIntent().getSerializableExtra("userData");

        itemList.clear();
        communityDataList.clear();

        createView.setOnClickListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.communityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        adapter = new CommunityItemAdapter(itemList, mActivity);
        recyclerView.setAdapter(adapter);
        adapter.setOnHeartClickListener(new CommunityItemAdapter.OnListHeartSelected() {
            @Override
            public void onHeartSelected(View v, int position, int vg) {
                if(vg == Code.ViewType.TEXT){
                    View heart = v.findViewById(R.id.communityHeartButton2);
                    CommunityItemData data = itemList.get(position);
                    CommunityMedia com = communityDataList.get(position);
                    if(data.isHeart()){
                        data.setHeart(false);
                        com.setHeart(false);
                        heart.setBackgroundResource(R.drawable.not_fill_favorite_icon);
                    }
                    else{
                        data.setHeart(true);
                        com.setHeart(true);
                        heart.setBackgroundResource(R.drawable.filled_favorite_icon);
                    }
                }

            }
        });
        adapter.setOnItemClickListener(new CommunityItemAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, int vg) {
                if(vg == Code.ViewType.TEXT){
                    CommunityItemData data = itemList.get(position);
                    communityPosition = position;
                    communityData = communityDataList.get(position);
                    Intent intent = new Intent(getActivity(), CommunityReadActivity.class);
                    intent.putExtra("communityData", communityData);
                    intent.putExtra("userData", userData);
                    startActivity(intent);
                }
                if(vg == Code.ViewType.IMAGE){
                    CommunityItemData data = itemList.get(position);
                    communityPosition = position;
                    communityData = communityDataList.get(position);
                    Intent intent = new Intent(getActivity(), CommunityReadActivity.class);
                    intent.putExtra("communityData", communityData);
                    intent.putExtra("userData", userData);
                    startActivity(intent);
                }
            }
        });

        getFirebaseToCommunity();

        return view;
    }

    public void setCommunityItemRecyclerList(){
        for(int i = 0; i < communityDataList.size(); i++){
            CommunityMedia com = communityDataList.get(i);
            if(!com.getImageUri().equals("")){
                CommunityItemData data = new CommunityItemData(Code.ViewType.IMAGE, com.getImageUri());
                data.setComId(com.getComid());
                data.setTitle(com.getTitle());
                data.setBody(com.getBody());
                data.setHeartNumber(com.getHeartNumber());
                data.setCommentNumber(com.getCommentNumber());
                data.setHeart(com.isHeart());
                itemList.add(data);
            }
            else{
                CommunityItemData data = new CommunityItemData(Code.ViewType.TEXT, "");
                data.setComId(com.getComid());
                data.setTitle(com.getTitle());
                data.setBody(com.getBody());
                data.setHeartNumber(com.getHeartNumber());
                data.setCommentNumber(com.getCommentNumber());
                data.setHeart(com.isHeart());
                itemList.add(data);
            }

        }
        adapter.notifyItemRangeInserted(0, itemList.size());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }


    @Override
    public void onClick(View v) {
        if( v == createView){
            Intent intent = new Intent(mActivity, CommunityWriteActivity.class);
            intent.putExtra("userData", userData);
            startActivity(intent);
        }
    }


    public void getFirebaseToCommunity(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("community")
                .orderBy("timeStamp", Query.Direction.DESCENDING )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CommunityMedia com = new CommunityMedia();
                                Map<String, Object> map = document.getData();
                                com.setUid((String) map.get("uid"));
                                com.setComid(document.getId());
                                com.setTitle((String) map.get("title"));
                                com.setBody((String) map.get("body"));
                                com.setImageUri((String) map.get("image"));
                                com.setHeart((Boolean) map.get("heart"));
                                com.setHeartNumber((Long) map.get("heartNumber"));
                                com.setCommentNumber((Long) map.get("commentNumber"));

                                communityDataList.add(com);
                            }
                            Log.d("Firesotre", "successs");
                            setCommunityItemRecyclerList();
                        } else {
                            Log.d("Firesotre", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}