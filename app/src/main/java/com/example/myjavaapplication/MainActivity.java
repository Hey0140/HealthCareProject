package com.example.myjavaapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private final static int  REQUEST_IMAGE_CODE = 903;

    private FragmentManager fmanager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private PetmanageFragment petmanageFragment = new PetmanageFragment();
    private MypageFragment mypageFragment = new MypageFragment();

    private PetStatusFragment petStatusFragment = new PetStatusFragment();
    //    private CommunityFragment communityFragment = new CommunityFragment();
    private FirebaseAuth auth;
    private UserMedia userData;
    public static int petPosition = 0;
    public static long petTodayStatus = 0;
    public static HospitalMedia hospitalData = new HospitalMedia();
    public static boolean isHospital = false;
    private ArrayList<PetMedia> petDataList = new ArrayList<>();
    private ArrayList<WalkRecordData> walkList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        auth = FirebaseAuth.getInstance();

        Intent intent = new Intent(this.getIntent());
        userData = (UserMedia) intent.getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) intent.getSerializableExtra("petDataList");

        FragmentTransaction transaction = fmanager.beginTransaction();

        transaction.replace(R.id.mainFragmentLayout, homeFragment).commitAllowingStateLoss();
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ftransaction = fmanager.beginTransaction();

            if (item.getItemId() == R.id.homeIcon){
                Bundle bundle = new Bundle();
                bundle.putSerializable("userData", userData);
                bundle.putSerializable("petDataList", petDataList);
                homeFragment.setArguments(bundle);
                ftransaction.replace(R.id.mainFragmentLayout, homeFragment).commitAllowingStateLoss();
            }
            if(item.getItemId() ==  R.id.petIcon) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userData", userData);
                bundle.putSerializable("petDataList", petDataList);
                petmanageFragment.setArguments(bundle);
                ftransaction.replace(R.id.mainFragmentLayout, petmanageFragment).commitAllowingStateLoss();
            }
//            if(item.getItemId() == R.id.communityIcon) {
//                ftransaction.replace(R.id.mainFragmentLayout, communityFragment).commitAllowingStateLoss();
//            }
            if(item.getItemId() == R.id.mypageIcon) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userData", userData);
                bundle.putSerializable("petDataList", petDataList);
                mypageFragment.setArguments(bundle);
                ftransaction.replace(R.id.mainFragmentLayout, mypageFragment).commitAllowingStateLoss();
            }
            return true;
        }
    }

    public void onChangeToPetStatusFragment(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("userData", userData);
        bundle.putSerializable("petDataList", petDataList);
        petStatusFragment.setArguments(bundle);
        FragmentTransaction ftransaction = fmanager.beginTransaction();
        ftransaction.replace(R.id.mainFragmentLayout, petStatusFragment).commitAllowingStateLoss();
    }
    public void onChangeToPetManageFragment(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("userData", userData);
        bundle.putSerializable("petDataList", petDataList);
        petmanageFragment.setArguments(bundle);
        FragmentTransaction ftransaction = fmanager.beginTransaction();
        ftransaction.replace(R.id.mainFragmentLayout, petmanageFragment).commitAllowingStateLoss();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this.getIntent());
        userData = (UserMedia) intent.getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) intent.getSerializableExtra("petDataList");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}