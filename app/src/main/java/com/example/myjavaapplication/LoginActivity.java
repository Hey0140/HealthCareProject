package com.example.myjavaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount gsa;
    private GoogleSignInClient gsc;

    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private List<String> mListPairedDevices;
    private Handler mBluetoothHandler;
    //    ConnectedBluetoothThread mThreadConnectedBluetooth;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothSocket mBluetoothSocket;
    private final static int  REQUEST_PERMISSION_CODE = 801;
    private final static int  REQUEST_PERMISSION_UNDER_CODE = 802;
    private boolean professional;
    private boolean GOOGLE;
    private final int RC_SIGN_IN = 9001;
    private final boolean BASIC_MEMBER = false;
    private final boolean PROFESSIONAL = true;


    private CheckBox proCheckBox, memCheckBox;
    private EditText idText, pwText;
    private Button loginButton, googleButton;
    private TextView loginJoin, loginFindOrPw;

    private String email = "", password = "";
    private UserMedia userData;
    private ArrayList<PetMedia> petDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        String [] permission_list = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_SCAN
        };
        ActivityCompat.requestPermissions(LoginActivity.this, permission_list, 1);

        email = "";
        password = "";
        professional = BASIC_MEMBER;
        GOOGLE = false;
        userData = new UserMedia();
        petDataList = new ArrayList<>();

        idText = findViewById(R.id.loginId);
        pwText = findViewById(R.id.loginPassword);
        googleButton = findViewById(R.id.loginSnsButton);
        loginButton = findViewById(R.id.loginButton);
        proCheckBox = findViewById(R.id.professCheckBox);
        memCheckBox = findViewById(R.id.basicCheckBox);
        loginJoin = findViewById(R.id.loginJoin);
        loginFindOrPw = findViewById(R.id.loginFindIdOrPw);

        auth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        loginButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        proCheckBox.setOnClickListener(this);
        memCheckBox.setOnClickListener(this);
        loginJoin.setOnClickListener(this);
        loginFindOrPw.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == proCheckBox) {
            professional = PROFESSIONAL;
            onCheckBox(professional);
        }
        if (v == memCheckBox) {
            professional = BASIC_MEMBER;
            onCheckBox(professional);
        }
        if (v == loginButton) {
            email = idText.getText().toString();
            password = pwText.getText().toString();
            if (email.length() > 0 && password.length() >= 6) {
                auth.signInWithEmailAndPassword(idText.getText().toString(), pwText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user.isEmailVerified()) {
                                        getDataToFirestore(user.getUid());
                                    } else {
                                        Toast.makeText(LoginActivity.this, "이메일 인증 중입니다. 이메일을 인증을 진행해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "아이디 또는 패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }

        }
        if (v == googleButton) {
            gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            if (gsa != null) {
                Toast.makeText(this, "구글 로그인은 일반 회원으로만 로그인할 수 있습니다.", Toast.LENGTH_SHORT).show();
                GOOGLE = true;
                memCheckBox.setChecked(true);
                proCheckBox.setChecked(false);
                professional = BASIC_MEMBER;
                getDataToFirestore(gsa.getId());
            } else {
                memCheckBox.setChecked(true);
                proCheckBox.setChecked(false);
                professional = BASIC_MEMBER;
                Toast.makeText(this, "구글 로그인은 일반 회원으로만 로그인할 수 있습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = gsc.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        }
        if (v == loginJoin) {
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
        }
        if (v == loginFindOrPw) {

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
//        if (requestCode == REQUEST_ENABLE_BT) {
//            if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
//                Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
//
//            } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
//                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
//
//            }
//        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount gsia = completedTask.getResult(ApiException.class);

            if (gsia != null) {
                firebaseAuthWithGoogle(gsia.getIdToken());
                String id = gsia.getId();
                String personName = gsia.getDisplayName();
                String email = gsia.getEmail();
                Uri personPhoto = gsia.getPhotoUrl();

                setDataToFirestore(id, email, personName, "", BASIC_MEMBER, "");
            }
        } catch (ApiException e) {
            Log.e("google", "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("google", "signInWithCredential:success");
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = auth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("google", "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private boolean hasPermissions(Context context, String[] permissions){
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(context, permissions[i])
                    != PackageManager.PERMISSION_GRANTED
            ) {
                return false;
            }
        }
        return true;
    }

    public void isBluetoothSet() {
        String [] permissionList = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_SCAN
        };
        String [] permissionUnderList = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        if(Build.VERSION.SDK_INT >= 31){
            if(!hasPermissions(this, permissionList)){
                requestPermissions(permissionList, REQUEST_PERMISSION_CODE);
            }
        }
        else {
            if(!hasPermissions(this, permissionUnderList)){
                requestPermissions(permissionUnderList, REQUEST_PERMISSION_CODE);
            }
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "블루투스가 활성화 되지않았습니다. 일부 기능이 제한될 수 있습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_CODE){
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "위치 권한을 허용하였습니다.", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "위치 권한을 허용해주셔야 합니다.", Toast.LENGTH_SHORT).show();
            }

            if (grantResults.length > 0 &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "블루투스 권한을 허용하였습니다.", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "권한을 허용해주셔야 합니다.", Toast.LENGTH_SHORT).show();
            }

            if (grantResults.length > 0 &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "위치 권한을 허용하였습니다.", Toast.LENGTH_SHORT).show();
            } else{
//                requestPermissions(permissions, REQUEST_PERMISSION_CODE);
//                Toast.makeText(this, "권한을 허용해주셔야 합니다.", Toast.LENGTH_SHORT).show();
            }

            if (grantResults.length > 0 &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "위치 권한을 허용하였습니다.", Toast.LENGTH_SHORT).show();
            } else{
//                requestPermissions(permissions, REQUEST_PERMISSION_CODE);
//                Toast.makeText(this, "권한을 허용해주셔야 합니다.", Toast.LENGTH_SHORT).show();
            }

        }
        if(requestCode == REQUEST_PERMISSION_UNDER_CODE){
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "위치 권한을 허용하였습니다.", Toast.LENGTH_SHORT).show();
            } else{
//                requestPermissions(permissions, REQUEST_PERMISSION_UNDER_CODE);
                Toast.makeText(this, "권한을 허용해주셔야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void getDataToFirestore(String id){
        isBluetoothSet();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userData = documentSnapshot.toObject(UserMedia.class);

                db.collection("users")
                        .document(id)
                        .collection("pet")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int i = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        PetMedia petMedia = new PetMedia();
                                        Map<String, Object> map = new HashMap<>();

                                        map = document.getData();
                                        petMedia = setPetMeida(map, i);

                                        petDataList.add(petMedia);
                                        Log.i("check", petMedia.getPetId()+": "+petMedia.getPetName()+", "+petMedia.getPetKind());
                                        i++;
                                    }
                                    Log.i("check", String.valueOf(petDataList.size()));
                                    if(userData.isPro() == professional){
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("userData", userData);
                                        intent.putExtra("petDataList", petDataList);
                                        startActivity(intent);
                                    }
                                    else{
                                        if(professional == BASIC_MEMBER && userData.isPro() == PROFESSIONAL){
                                            Toast.makeText(LoginActivity.this, "의사 계정 "+userData.getName()+"님 일반 회원 서비스로 접속합니다.", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("userData", userData);
                                            intent.putExtra("petDataList", petDataList);
                                            startActivity(intent);
                                        }
                                        if(professional == PROFESSIONAL && userData.isPro() == BASIC_MEMBER){
                                            Toast.makeText(LoginActivity.this, "의사 계정이 아닙니다. 일반 회원으로 접속해주세요.", Toast.LENGTH_LONG).show();
                                            auth.signOut();
                                        }
                                    }
                                } else {
                                    Log.d("Firebase", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });


    }

    public PetMedia setPetMeida(Map<String, Object> map, int position){
        PetMedia petMedia = new PetMedia();
        petMedia.setuId(map.get("uid").toString());
        long tempId = (long) map.get("id");
        petMedia.setPetId((int) tempId);
        petMedia.setPetBirth(map.get("birth").toString());
        petMedia.setPetFeat(map.get("feat").toString());
        petMedia.setPetFeed(map.get("feed").toString());
        petMedia.setPetFeedCalorie((Long) map.get("feedcal"));
        petMedia.setImage(map.get("image").toString());
        petMedia.setPetKind((Long) map.get("kind"));
        petMedia.setPetName(map.get("name").toString());
        petMedia.setPetSex((Long) map.get("sex"));
        petMedia.setPetWeight((Long) map.get("weight"));
        petMedia.setWalk((Long) map.get("walk"));
        petMedia.setPetLike((HashMap<String, Boolean>) map.get("petLike"));
        petMedia.setPetVaccine((HashMap<String, Boolean>) map.get("petVaccine"));
        return petMedia;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        petDataList.clear();
        if(GOOGLE == true){
            onCheckBox(professional);
            GOOGLE = false;
        }
        else{
            userData = new UserMedia();
            idText.setText(email);
            pwText.setText(password);
            onCheckBox(professional);
        }
    }

    void onCheckBox(boolean mem){
        if(mem == PROFESSIONAL){
            memCheckBox.setChecked(false);
            proCheckBox.setChecked(true);
        }
        else if(mem == BASIC_MEMBER){
            memCheckBox.setChecked(true);
            proCheckBox.setChecked(false);
        }
    }


    public void setDataToFirestore(String id, String email, String name, String number, Boolean professional, String image){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", id);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("number", number);
        hashMap.put("pro", professional);
        hashMap.put("image", image);
        hashMap.put("count", 0);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(id)
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firebase", "DocumentSnapshot successfully written!");
                        Map<String, Object> walk = new HashMap<>();
                        walk.put("uid", id);
                        db.collection("walk").document(id)
                                .set(walk)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Firebase", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Firebase", "Error writing document", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firebase", "Error writing document", e);
                    }
                });
    }

}

