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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.ArrayList;
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
    private View profileImage, myOwnButton, myPageLove, myFriend;
    private CircleImageView profileView;
    private Uri imageUri;
    private Bitmap image;

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

        logoutButton.setOnClickListener(this);
        profileView.setOnClickListener(this);
        myOwnButton.setOnClickListener(this);
        myPageLove.setOnClickListener(this);
        myFriend.setOnClickListener(this);

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
}