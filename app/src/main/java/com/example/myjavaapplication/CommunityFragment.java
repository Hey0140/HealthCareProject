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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommunityFragment extends Fragment implements View.OnClickListener {
    private MainActivity mActivity;
    private UserMedia userData = new UserMedia();
    public static long selectedCommentNumber = 0;
    public static boolean isComment = false;
    public static long selectedHeartNumber = 0;
    public static boolean isHeart = false;
    public static boolean statusHeart = false;
    public static boolean isNew = false;
    private int fragmentStatus = 0;
    private ArrayList<CommunityItemData> itemList = new ArrayList<>();
    private ArrayList<CommunityMedia> communityDataList = new ArrayList<>();
    public static int communityPosition = 0;
    private CommunityMedia communityData = new CommunityMedia();
    private CommunityItemAdapter adapter;
    private View createView, searchButton, cancelButton;
    public EditText searchText;
    public ArrayList<String> likeList = new ArrayList<>();


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
        fragmentStatus = 0;

        createView = view.findViewById(R.id.communityCreateView);
        searchText = view.findViewById(R.id.communitySearchText);
        searchButton = view.findViewById(R.id.communitySearchIcon);
        cancelButton = view.findViewById(R.id.communitySearchCancelButtton);

        userData = (UserMedia) getActivity().getIntent().getSerializableExtra("userData");

        itemList.clear();
        communityDataList.clear();
        likeList.clear();

        cancelButton.setVisibility(View.INVISIBLE);

        createView.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

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
                    communityPosition = position;
                    communityData = communityDataList.get(position);
                    if(data.isHeart()){
                        itemList.get(position).setHeart(false);
                        communityDataList.get(position).setHeart(false);
                        heart.setBackgroundResource(R.drawable.not_fill_favorite_icon);

                        setFalseHeartOnFirebase(communityData);
                    }
                    else{
                        itemList.get(position).setHeart(true);
                        communityDataList.get(position).setHeart(true);
                        heart.setBackgroundResource(R.drawable.filled_favorite_icon);

                        setTrueHeartOnFirebase(communityData);
                    }
                }
                if(vg == Code.ViewType.IMAGE){
                    View heart = v.findViewById(R.id.communityHeartButton);
                    CommunityItemData data = itemList.get(position);
                    communityPosition = position;
                    communityData = communityDataList.get(position);
                    if(data.isHeart()){
                        itemList.get(position).setHeart(false);
                        communityDataList.get(position).setHeart(false);
                        heart.setBackgroundResource(R.drawable.not_fill_favorite_icon);

                        setFalseHeartOnFirebase(communityData);
                    }
                    else{
                        itemList.get(position).setHeart(true);
                        communityDataList.get(position).setHeart(true);
                        heart.setBackgroundResource(R.drawable.filled_favorite_icon);

                        setTrueHeartOnFirebase(communityData);
                    }
                }

            }
        });
        adapter.setOnItemClickListener(new CommunityItemAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, int vg) {
                fragmentStatus = 1;
                if(vg == Code.ViewType.TEXT){
                    CommunityItemData data = itemList.get(position);
                    communityPosition = position;
                    communityData = communityDataList.get(position);

                    selectedCommentNumber = communityData.getCommentNumber();
                    selectedHeartNumber = communityData.getHeartNumber();
                    communityPosition = position;
                    isComment = false;
                    isHeart = false;
                    statusHeart = data.isHeart();

                    Intent intent = new Intent(getActivity(), CommunityReadActivity.class);
                    intent.putExtra("communityData", communityData);
                    intent.putExtra("userData", userData);
                    startActivity(intent);
                }
                if(vg == Code.ViewType.IMAGE){
                    CommunityItemData data = itemList.get(position);
                    communityPosition = position;
                    communityData = communityDataList.get(position);

                    selectedCommentNumber = communityData.getCommentNumber();
                    selectedHeartNumber = communityData.getHeartNumber();
                    communityPosition = position;
                    isComment = false;
                    isHeart = false;
                    statusHeart = data.isHeart();

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

        getLikeDataToFirebase();
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
            fragmentStatus = 2;
            isNew = false;
            Intent intent = new Intent(mActivity, CommunityWriteActivity.class);
            intent.putExtra("userData", userData);
            startActivity(intent);
        }
        if( v == searchButton){
            String search = searchText.getText().toString();
            cancelButton.setVisibility(View.VISIBLE);

            int count = itemList.size();
            itemList.clear();
            communityDataList.clear();
            adapter.notifyItemRangeRemoved(0, count);
            getSearchFirebaseToCommunity(search);
        }
        if(v == cancelButton){
            cancelButton.setVisibility(View.INVISIBLE);

            int count = itemList.size();
            itemList.clear();
            communityDataList.clear();
            adapter.notifyItemRangeRemoved(0, count);
            getFirebaseToCommunity();
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


    public void getSearchFirebaseToCommunity(String title){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference citiesRef = db.collection("community");

        Query query = citiesRef.whereEqualTo("title", title);
        query.orderBy("timeStamp", Query.Direction.DESCENDING );
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firesotre", "successs");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Firesotre", "successs22");
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
    @Override
    public void onStart() {
        super.onStart();
        if(fragmentStatus == 1){
            fragmentStatus = 0;
            if(isComment|| isHeart){
                itemList.get(communityPosition).setCommentNumber(selectedCommentNumber);
                itemList.get(communityPosition).setHeartNumber(selectedHeartNumber);
                itemList.get(communityPosition).setHeart(statusHeart);
                communityDataList.get(communityPosition).setHeart(statusHeart);
                adapter.notifyItemChanged(communityPosition);
            }
        }
        if(fragmentStatus == 2){
            fragmentStatus = 0;
            int count = itemList.size();
            itemList.clear();
            communityDataList.clear();
            adapter.notifyItemRangeRemoved(0, count);
            getFirebaseToCommunity();
        }
    }

    public void setTrueHeartOnFirebase(CommunityMedia com){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();
        String uid = com.getUid();
        hashMap.put("uid", uid);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String time = sdf1.format(now);

        hashMap.put("timeStamp", time);
        hashMap.put("title", com.getTitle());
        hashMap.put("body", com.getBody());
        hashMap.put("image", com.getImageUri());
        hashMap.put("heart", com.isHeart());
        hashMap.put("heartNumber", com.getHeartNumber());
        hashMap.put("commentNumber", com.getCommentNumber());
        hashMap.put("comId", com.getComid());


        db.collection("communityUser").document(com.getUid())
                .collection("like").document(com.getComid())
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                        DocumentReference dr = db.collection("community").document(com.getComid());
                        dr.update("heartNumber", com.getHeartNumber() + 1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        long count = com.getHeartNumber() + 1;
                                        communityDataList.get(communityPosition).setHeartNumber(count);
                                        itemList.get(communityPosition).setHeartNumber(count);
                                        adapter.notifyItemChanged(communityPosition);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error writing document", e);

                    }
                });

    }
    public void setFalseHeartOnFirebase(CommunityMedia com){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("communityUser").document(com.getUid())
                .collection("like").document(com.getComid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        DocumentReference dr = db.collection("community").document(com.getComid());
                        dr.update("heartNumber", com.getHeartNumber() - 1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        long count = com.getHeartNumber() - 1;
                                        communityDataList.get(communityPosition).setHeartNumber(count);
                                        itemList.get(communityPosition).setHeartNumber(count);
                                        adapter.notifyItemChanged(communityPosition);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void getLikeDataToFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("communityUser").document(userData.getUid())
                .collection("like")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CommunityMedia com = new CommunityMedia();
                                String comId = document.getId();
                                likeList.add(comId);
                            }


                            for(int i = 0; i < itemList.size(); i++){
                                if(likeList.contains(itemList.get(i).getComId())){
                                    itemList.get(i).setHeart(true);
                                    communityDataList.get(i).setHeart(true);
                                    adapter.notifyItemChanged(i);
                                }
                            }
                        } else {
                            Log.d("Firesotre", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}