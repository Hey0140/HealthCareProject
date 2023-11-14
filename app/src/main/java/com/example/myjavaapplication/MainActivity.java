package com.example.myjavaapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fmanager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private PetmanageFragment petmanageFragment = new PetmanageFragment();
    private CommunityFragment communityFragment = new CommunityFragment();
    private MypageFragment mypageFragment = new MypageFragment();
    String email= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Intent intent = new Intent(this.getIntent());
        email = intent.getStringExtra("email");
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
                ftransaction.replace(R.id.mainFragmentLayout, homeFragment).commitAllowingStateLoss();
            }
            if(item.getItemId() ==  R.id.petIcon) {
                ftransaction.replace(R.id.mainFragmentLayout, petmanageFragment).commitAllowingStateLoss();
            }
            if(item.getItemId() == R.id.communityIcon) {
                ftransaction.replace(R.id.mainFragmentLayout, communityFragment).commitAllowingStateLoss();
            }
            if(item.getItemId() == R.id.mypageIcon) {
                ftransaction.replace(R.id.mainFragmentLayout, mypageFragment).commitAllowingStateLoss();
            }
            return true;
        }
    }

}