package com.example.myjavaapplication;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MypageFragment extends Fragment implements View.OnClickListener {
    private final static int  REQUEST_IMAGE_CODE = 903;
//    private final static int  REQUEST_IMAGE_CODE2 = 1003;
    private MainActivity mActivity;
    private FirebaseAuth auth;

    private ArrayList<MyPetInfoData> list= new ArrayList<>();

    private Button logoutButton;
    private MypetInfoAdapter adapter;

    private UserMedia userData;
    private ArrayList<PetMedia> petDataList;
    private TextView profileName, profileEmail;
    private ConstraintLayout walkRecordButton, profileInfo;
    private View profileImage, myOwnButton, myPageLove, myFriend;
    private CircleImageView profileView;
    private Uri imageUri;
    private Bitmap image;
    private WeekStatusData weekStatus = new WeekStatusData();
    private int petPosition = 0;
    private final long SMALL = 21;
    private final long MIDDLE = 22;
    private final long BIG = 23;
    private final long GOOD = 41;
    private final long NORMAL = 42;
    private final long WORSE = 43;

//    private ArrayList<String> imageString = new ArrayList<>();
//    private PetMedia petData = new PetMedia();

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
        profileView = view.findViewById(R.id.profileView);
        myOwnButton = view.findViewById(R.id.myOwnPostButton);
        myPageLove = view.findViewById(R.id.myPageLoveButton);
        myFriend = view.findViewById(R.id.myPetFriendButton);
        walkRecordButton = view.findViewById(R.id.walkRecordButton);
        profileInfo = view.findViewById(R.id.mypageProfileInfoLayout);


        logoutButton.setOnClickListener(this);
        profileView.setOnClickListener(this);
        myOwnButton.setOnClickListener(this);
        myPageLove.setOnClickListener(this);
        myFriend.setOnClickListener(this);
        walkRecordButton.setOnClickListener(this);
        profileInfo.setOnClickListener(this);

        petDataList = new ArrayList<>();

        userData = (UserMedia) getActivity().getIntent().getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) getActivity().getIntent().getSerializableExtra("petDataList");



        if(userData.getImage() != null){
            if(userData.getImage().equals("")){
                profileImage.setVisibility(View.VISIBLE);
                profileView.setImageResource(R.drawable.my_pet_profile);
            }
            else {
                getPetProfileImage(userData);
            }
        }
        profileName.setText(userData.getName());
        profileEmail.setText(userData.getEmail());

        list.clear();

        petPosition = MainActivity.petPosition;
        for (int i = 0; i < petDataList.size(); i++) {
            MyPetInfoData mpid = new MyPetInfoData();
            PetMedia petMedia = petDataList.get(i);
            mpid.setName(petMedia.getPetName());
            if(!petMedia.getImage().equals("")){
                String imageId = petMedia.getuId() + "_" + petMedia.getPetId();
                mpid.setImageId(imageId);
            }else{
                mpid.setImageId("");
            }
            mpid.setViewType(Code.ViewType.BASIC);
            Log.i("check", petMedia.getPetName());
            list.add(mpid);
        }

        lastAddData();

        RecyclerView recyclerView = view.findViewById(R.id.myPetInfoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        adapter = new MypetInfoAdapter(list, getContext());
        adapter.setOnItemClickListener(new MypetInfoAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position, int vg) {
                        if(vg == Code.ViewType.BASIC){

//                            Intent intent = new Intent();
//                            intent.setType("image/*");
//                            intent.setAction(intent.ACTION_GET_CONTENT);
//                            startActivityForResult(intent, 1003);
//                            petData = petDataList.get(position);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("확인창")
                                    .setMessage("삭제하시겠습니까?")
                                    .setCancelable(false)
                                    .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            PetMedia data = petDataList.get(position);
                                            onSetDeletePet(data, position);
                                        }
                                    })
                                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        if(vg == Code.ViewType.ADD){
                            Intent intent = new Intent(getActivity(), PetAddActivity.class);
                            intent.putExtra("userData", userData);
                            intent.putExtra("petDataList", petDataList);
                            startActivity(intent);
                        }
            }
        });
        recyclerView.setAdapter(adapter);

        if(petDataList.size() > 0){
            Calendar now = Calendar.getInstance();
            int day = now.get(Calendar.DAY_OF_WEEK);
            setWeekStatus(day);
        }


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
        if(v == profileView){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 903);
        }
        if(v == myOwnButton){
            MypageCommunityDialog dialog = new MypageCommunityDialog(mActivity, userData);
            dialog.setMypageCommunityDialogListener(new MypageCommunityDialog.OnMypageCommunityDialogListener() {
                @Override
                public void onItemSelected(CommunityMedia communityMedia) {

                }
            });
            dialog.show();
        }
        if(v == myPageLove){
            MypageLoveDialog dialog = new MypageLoveDialog(mActivity, userData);
            dialog.setMypageLoveDialogListener(new MypageLoveDialog.OnMypageLoveDialogListener() {
                @Override
                public void onItemSelected(CommunityMedia communityMedia) {

                }
            });
            dialog.show();
        }
        if(v == myFriend){
            MypageFriendDialog dialog = new MypageFriendDialog(mActivity, userData);
            dialog.setMyFriendDialogListener(new MypageFriendDialog.OnMyfriendDialogListener() {
                @Override
                public void onItemSelected(MypageFriendItemData friendData) {

                }
            });
            dialog.show();
        }
        if(v == walkRecordButton){
            if (petDataList.size() > 0){
                Intent intent = new Intent(mActivity, DaywalkActivity.class);
                intent.putExtra("weekStatus", weekStatus);
                intent.putExtra("petDataList", petDataList);
                intent.putExtra("petPosition", petPosition);
                startActivity(intent);
            }
            else {
                Toast.makeText(getContext(), "펫을 먼저 등록해주세요.", Toast.LENGTH_SHORT).show();
            }

        }
        if(v == profileInfo){
            NameChangeDialog dialog = new NameChangeDialog(mActivity);
            dialog.setChangeDialogListener(new NameChangeDialog.OnChangeDialogListener() {
                @Override
                public void onChangeSelected(String data) {
                    setProfileChangedName(data);
                }
            });
            dialog.show();
        }
    }



    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == REQUEST_IMAGE_CODE){
            if(resultCode == RESULT_OK){
                imageUri = data.getData();
                try{

                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                    image = BitmapFactory.decodeStream(in);
                    in.close();
                    profileView.setImageBitmap(image);
                    profileImage.setVisibility(View.INVISIBLE);

                    userData.setImage(String.valueOf(imageUri));
                    setProfileImage(userData, imageUri);
                }catch (Exception e){

                }
            }
        }
//        if( requestCode == REQUEST_IMAGE_CODE2){
//            if(resultCode == RESULT_OK){
//                imageUri = data.getData();
//                try{
//                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
//                    image = BitmapFactory.decodeStream(in);
//                    in.close();
//                    profileView.setImageBitmap(image);
//                    profileImage.setVisibility(View.INVISIBLE);
//                    petData.setImage(String.valueOf(imageUri));
//                    setPetImage(petData, imageUri);
//                }catch (Exception e){
//
//                }
//            }
//        }
    }

//    private void setPetImage(PetMedia petData, Uri uri) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String uid = petData.getuId();
//        String petId = String.valueOf(petData.getPetId());
//        DocumentReference documentReference = db.collection("users").document(uid)
//                .collection("pet").document(petId);
//
//        documentReference.update("image", String.valueOf(uri))
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        petData.setImage(String.valueOf(uri));
//                        Log.d("Firebase", "DocumentSnapshot successfully updated!");
//
//                        FirebaseStorage storage = FirebaseStorage.getInstance();
//                        String fileName = uid + "_"+petId;
//
//                        StorageReference storageRef = storage.getReference().child("images/").child("pet/").child(fileName);
//                        UploadTask uploadTask = storageRef.putFile(uri);
//
//                        Toast.makeText(getContext(), "업로드 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
//                        uploadTask.addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                Log.i("Firebase Storage", "error");
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Log.i("Firebase Storage", "success");
//                                Toast.makeText(getContext(), "업로드 되었습니다!", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("Firebase", "Error updating document", e);
//                    }
//                });
//    }

    public void onSetDeletePet(PetMedia pet, int position){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = pet.getuId();
        String pid = String.valueOf(pet.getPetId());

        db.collection("users").document(id)
                .collection("pet").document(pid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                        adapter.notifyItemRemoved(position);
                        list.remove(position);
                        petDataList.remove(position);

                        MainActivity.petPosition = 0;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error deleting document", e);
                    }
                });

    }


    public void setProfileImage(UserMedia user, Uri uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String fileName = user.getUid();
        StorageReference storageRef = storage.getReference().child("images/").child("users/").child(fileName);
        UploadTask uploadTask = storageRef.putFile(uri);


        Toast.makeText(getContext(), "업로드 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
        db.collection("users").document(user.getUid())
                .update("image", String.valueOf(uri))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.i("Firebase Storage", "error");
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.i("Firebase Storage", "success");
                                Toast.makeText(getContext(), "업로드 되었습니다!", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    public void getPetProfileImage(UserMedia user){
        String userId = user.getUid();
        String fileName = userId;
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://pet-healthcare-45a41.appspot.com");
        StorageReference storageReference = storage.getReference().child("images/").child("users/").child(fileName);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                if(mActivity != null){
                    Glide.with(getContext())
                            .load(uri)
                            .into(profileView);

                    profileImage.setVisibility(View.INVISIBLE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                profileImage.setVisibility(View.VISIBLE);
                profileView.setImageResource(R.drawable.my_pet_profile);
            }
        });
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
        String petId = String.valueOf(petDataList.get(petPosition).getPetId());
        long kind = petDataList.get(petPosition).getPetKind();


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
                                weekStatus.setMonday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                weekStatus.setMonday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                weekStatus.setMonday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                weekStatus.setMonday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                weekStatus.setMonday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                weekStatus.setMonday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                weekStatus.setMonday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                weekStatus.setMonday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                weekStatus.setMonday(GOOD);
                            }
                        }

                        if(day == 2){
                            MainActivity.petTodayStatus = weekStatus.getMonday();
                        }
                    }
                    else{
                        weekStatus.setMonday(WORSE);
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
                                weekStatus.setTuesday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                weekStatus.setTuesday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                weekStatus.setTuesday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                weekStatus.setTuesday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                weekStatus.setTuesday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                weekStatus.setTuesday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                weekStatus.setTuesday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                weekStatus.setTuesday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                weekStatus.setTuesday(GOOD);
                            }
                        }
                        if(day == 3){
                            MainActivity.petTodayStatus = weekStatus.getTuesday();
                        }
                    }
                    else{
                        weekStatus.setTuesday(WORSE);
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
                                weekStatus.setWednesday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                weekStatus.setWednesday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                weekStatus.setWednesday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                weekStatus.setWednesday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                weekStatus.setWednesday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                weekStatus.setWednesday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                weekStatus.setWednesday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                weekStatus.setWednesday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                weekStatus.setWednesday(GOOD);
                            }
                        }
                        if(day == 4){
                            MainActivity.petTodayStatus = weekStatus.getWednesday();
                        }
                    }
                    else{
                        weekStatus.setWednesday(WORSE);
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
                                weekStatus.setThursday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                weekStatus.setThursday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                weekStatus.setThursday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                weekStatus.setThursday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                weekStatus.setThursday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                weekStatus.setThursday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                weekStatus.setThursday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                weekStatus.setThursday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                weekStatus.setThursday(GOOD);
                            }
                        }
                        if(day == 5){
                            MainActivity.petTodayStatus = weekStatus.getThursday();
                        }
                    }
                    else{
                        weekStatus.setThursday(WORSE);
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
                                weekStatus.setFriday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                weekStatus.setFriday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                weekStatus.setFriday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                weekStatus.setFriday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                weekStatus.setFriday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                weekStatus.setFriday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                weekStatus.setFriday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                weekStatus.setFriday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                weekStatus.setFriday(GOOD);
                            }
                        }
                        if(day == 6){
                            MainActivity.petTodayStatus = weekStatus.getSaturday();
                        }
                    }
                    else{
                        weekStatus.setFriday(WORSE);
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
                                weekStatus.setSaturday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                weekStatus.setSaturday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                weekStatus.setSaturday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                weekStatus.setSaturday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                weekStatus.setSaturday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                weekStatus.setSaturday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                weekStatus.setSaturday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                weekStatus.setSaturday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                weekStatus.setSaturday(GOOD);
                            }
                        }
                        if(day == 7){
                            MainActivity.petTodayStatus = weekStatus.getSaturday();
                        }
                    }
                    else{
                        weekStatus.setSaturday(WORSE);
                        if(day == 7){
                            MainActivity.petTodayStatus = WORSE;
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
                                weekStatus.setSunday(WORSE);
                            } else if (dayTotal >= 10 && dayTotal < 20) {
                                weekStatus.setSunday(NORMAL);
                            } else if (dayTotal >= 20 && dayTotal <= 40) {
                                weekStatus.setSunday(GOOD);
                            }
                        } else if (kind == MIDDLE) {
                            if (dayTotal < 30 || dayTotal > 70) {
                                weekStatus.setSunday(WORSE);
                            } else if (dayTotal >= 30 && dayTotal < 45) {
                                weekStatus.setSunday(NORMAL);
                            } else if (dayTotal >= 45 && dayTotal <= 70) {
                                weekStatus.setSunday(GOOD);
                            }
                        } else if (kind == BIG) {
                            if (dayTotal < 40 || dayTotal > 130) {
                                weekStatus.setSunday(WORSE);
                            } else if (dayTotal >= 40 && dayTotal < 90) {
                                weekStatus.setSunday(NORMAL);
                            } else if (dayTotal >= 90 && dayTotal <= 130) {
                                weekStatus.setSunday(GOOD);
                            }
                        }
                        if(day == 1){
                            MainActivity.petTodayStatus = weekStatus.getSunday();
                        }
                    }
                    else{
                        weekStatus.setSunday(WORSE);
                        if(day == 1){
                            MainActivity.petTodayStatus = WORSE;
                        }
                    }
                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                }
            }
        });

    }


    public void setProfileChangedName(String name){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference dr = db.collection("users").document(userData.getUid());

        dr.update("name", name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userData.setName(name);
                        MainActivity.userData = userData;
                        //괜찮은지?
                        profileName.setText(name);
                        Toast.makeText(mActivity, "이름이 변경됩니다. 지금까지 작성한 댓글의 이름은 변경되지 않습니다.",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


}