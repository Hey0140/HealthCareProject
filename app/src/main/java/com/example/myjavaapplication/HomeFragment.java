package com.example.myjavaapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.type.DateTime;

import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> devices;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private Thread workerThread = null;
    private byte[] readBuffer;
    private int readBufferPosition;
    private List<String> dataPaired;
    private WalkRecordAdapter adapter;
    private final static int  REQUEST_PERMISSION_CODE = 801;
    private final static int  REQUEST_PERMISSION_UNDER_CODE = 802;
    private final long MALE = 11;
    private final long FEMALE = 12;
    private final long MALENEUTER = 13;
    private final long FEMALENEUTER = 14;
    private CircleImageView profileMainImage;
    private ImageView profile;
    private TextView puppyName;
    private TextView homePageWalkNumber, homePageWalkTime, homePageHeart, homePageNeedCalorie;
    private Button homePageChangeButton;
    private TextView walkStartText;
    private View walkStartButton, restartButton, pauseButton, replayButton;
    private View bluetoothButton;
    private Chronometer chronometer;
    private ArrayList<PetChangeData> petChangelist = new ArrayList<>();
    private ArrayList<WalkRecordData> walkList = new ArrayList<>();
    private UserMedia userData;
    private PetMedia petData;
    private ArrayList<PetMedia> petDataList;
    private int petPostion = 0;
    private double needCalorie = 0;
    private long pauseTime = 0;
    private String startTime = "";
    private String endTime = "";
    private String duringTime = "";

    private double walkAllTime = 0;
    private boolean isPaused = false;
    private Dialog bluetoothDialog;
    private int walkListSize = 0;


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

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        puppyName = view.findViewById(R.id.puppyName);
        homePageWalkNumber = view.findViewById(R.id.homePageWalkNumber);
        homePageWalkTime = view.findViewById(R.id.homePageWalkTime);
        homePageHeart = view.findViewById(R.id.homePageHeart);
        homePageNeedCalorie = view.findViewById(R.id.homePageNeedCalorie);
        homePageChangeButton = view.findViewById(R.id.homePageChangeButton);
        profileMainImage = view.findViewById(R.id.homePageProfileView);
        profile = view.findViewById(R.id.homePageProfileImageView);
        walkStartButton = view.findViewById(R.id.walkStartButton);
        walkStartText = view.findViewById(R.id.walkStartText);
        bluetoothButton = view.findViewById(R.id.bluetoothButton);
        chronometer = (Chronometer) view.findViewById(R.id.homePageChronometer);
        restartButton = view.findViewById(R.id.homePageWalkRestart);
        pauseButton = view.findViewById(R.id.homePageWalkPause);
        replayButton = view.findViewById(R.id.homePageWalkReplay);

        walkStartButton.setOnClickListener(this);
        homePageChangeButton.setOnClickListener(this);
        restartButton.setOnClickListener(this);
        replayButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        bluetoothButton.setOnClickListener(this);

        chronometer.setVisibility(View.INVISIBLE);
        walkStartText.setVisibility(View.VISIBLE);

        restartButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        replayButton.setVisibility(View.INVISIBLE);
        isPaused = false;

        userData = new UserMedia();
        petDataList = new ArrayList<>();
        petData = new PetMedia();

        userData = (UserMedia) getActivity().getIntent().getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) getActivity().getIntent().getSerializableExtra("petDataList");

        if (petDataList.size() > 0) {
            petData = petDataList.get(petPostion);
            puppyName.setText(petData.getPetName());

            long rer = petData.getPetWeight() * 30 + 70;
            if (petData.getPetSex() == MALE || petData.getPetSex() == FEMALE) {
                needCalorie = rer * 1.8;
            }
            if (petData.getPetSex() == MALENEUTER || petData.getPetSex() == FEMALENEUTER) {
                needCalorie = rer * 1.6;
            }
            String calorieName = String.valueOf(needCalorie);
            homePageNeedCalorie.setText(calorieName);

            profileMainImage.setImageResource(R.drawable.pet_temp_image);
            profile.setVisibility(View.INVISIBLE);
        } else {
            petData.setPetName("이름");
            petData.setPetWeight(0);
            puppyName.setText("반려동물을 추가해주세요.");
            homePageWalkNumber.setText(0);
            homePageWalkTime.setText(0);
            homePageHeart.setText(0);
            homePageNeedCalorie.setText((int) needCalorie);
        }

        petChangelist.clear();
        setPetChangedList();

        walkListSize = walkList.size();

        walkList.clear();
        RecyclerView recyclerView = view.findViewById(R.id.walkRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        adapter = new WalkRecordAdapter(walkList);
        recyclerView.setAdapter(adapter);

        bluetoothDialog = new Dialog(getActivity());

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == homePageChangeButton) {
            PetChangeDialog dialog = new PetChangeDialog(getContext(), petChangelist);
            dialog.setChangeDialogListener(
                    new PetChangeDialog.OnChangeDialogListener() {
                        @Override
                        public void onChangeSelected(String data, int position) {
                            puppyName.setText(data);
                            petPostion = position;

                            petData = petDataList.get(petPostion);
                            puppyName.setText(petData.getPetName());
                            long rer = petData.getPetWeight() * 30 + 70;
                            if (petData.getPetSex() == MALE || petData.getPetSex() == FEMALE) {
                                needCalorie = rer * 1.8;
                            }
                            if (petData.getPetSex() == MALENEUTER || petData.getPetSex() == FEMALENEUTER) {
                                needCalorie = rer * 1.6;
                            }
                            String calorieName = String.valueOf(needCalorie);
                            homePageNeedCalorie.setText(calorieName);


                        }
                    }
            );
            dialog.show();
        }
        if (view == walkStartButton) {
            chronometer.setVisibility(View.VISIBLE);
            walkStartText.setVisibility(View.INVISIBLE);

            restartButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
            replayButton.setVisibility(View.VISIBLE);

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            LocalTime localTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localTime = LocalTime.now();
                startTime = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                Log.i("check111", startTime);
            }
            else{
                Toast.makeText(getContext(), "버전이 낮습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == restartButton) {
            if (isPaused) {
                chronometer.setBase(pauseTime + SystemClock.elapsedRealtime());
                chronometer.start();
            }
        }
        if (view == pauseButton) {
            chronometer.stop();
            isPaused = true;
            pauseTime = chronometer.getBase() - SystemClock.elapsedRealtime();
            Log.i("pause", pauseTime + ": pauseTime " + chronometer.getBase() + ": getbase " + SystemClock.elapsedRealtime() + ": realTiem ");
        }
        if (view == replayButton) {
            chronometer.stop();
            double realTime = (double) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000) / 60;
            walkAllTime += realTime;
            DecimalFormat df = new DecimalFormat("0.0");
            homePageWalkTime.setText(df.format(walkAllTime));
            duringTime = df.format(realTime);

            pauseTime = 0;
            isPaused = false;

            restartButton.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.INVISIBLE);
            replayButton.setVisibility(View.INVISIBLE);
            chronometer.setVisibility(View.INVISIBLE);

            walkStartText.setVisibility(View.VISIBLE);
            LocalTime localTime = null;


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                localTime = LocalTime.now();
                endTime = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            }else{
                Toast.makeText(getContext(), "버전이 낮습니다.", Toast.LENGTH_SHORT).show();
            }

            WalkRecordData walkRecordData = new WalkRecordData();
            walkRecordData.setStartTime(startTime);
            walkRecordData.setEndTime(endTime);
            walkRecordData.setDuringTime(duringTime);
            Log.i("check", startTime+", "+endTime+", "+duringTime);
            walkList.add(walkRecordData);

            adapter.notifyItemInserted(walkListSize);
            walkListSize++;
        }
        if (view == bluetoothButton) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter != null) {
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(getContext(), "블루투스 활성화를 먼저 진행해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    selectBluettothDevice();
                }
            } else {
                Toast.makeText(getContext(), "블루투스가 불가능한 안드로이드 입니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setPetChangedList() {
        for (int i = 0; i < petDataList.size(); i++) {
            PetChangeData petChangeData = new PetChangeData();
            petChangeData.setName(petDataList.get(i).getPetName());
            petChangelist.add(petChangeData);
        }
    }

    @SuppressLint("MissingPermission")
    public void selectBluettothDevice() {

        devices = bluetoothAdapter.getBondedDevices();

        int pairedCount = devices.size();
        if (pairedCount == 0) {
            Toast.makeText(getContext(), "페어링된 장치가 없습니다. 페어링 진행해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("페어링 된 블루투스 디바이스 목록");
            dataPaired  = new ArrayList<>();

            for (BluetoothDevice bluettothDevice : devices) {
                dataPaired.add(bluettothDevice.getName());
            }
            dataPaired.add("취소");

            final CharSequence[] charSequences = dataPaired.toArray(new CharSequence[dataPaired.size()]);
            dataPaired.toArray(new CharSequence[dataPaired.size()]);

            builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    connectDevice(charSequences[i].toString());

                }
            });

            AlertDialog alertDialog = builder.create();

            alertDialog.show();
        }


    }

    @SuppressLint("MissingPermission")
    public void connectDevice(String deviceName) {
        if(deviceName.equals("취소")){
            return;
        }
        else{
            for (BluetoothDevice tempDevice : devices) {
                if (deviceName.equals(tempDevice.getName())) {
                    bluetoothDevice = tempDevice;
                    break;
                }
            }

            Toast.makeText(getContext(), bluetoothDevice.getName() + "로 연결완료", Toast.LENGTH_SHORT).show();

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            try {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void receiveData(){
        final Handler handler = new Handler();
        readBufferPosition = 0;
        readBuffer = new byte[2048];

        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()){
                    try {
                        int byteAvailable = inputStream.available();
                        if ( byteAvailable > 0){
                            byte[] bytes = new byte[byteAvailable];
                            inputStream.read(bytes);
                            for ( int i = 0; i < byteAvailable; i++){
                                byte tempByte = bytes[i];
                                if (tempByte == '\n'){
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String text = new String(encodedBytes, "UTF-8");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                        }
                                    });
                                }
                                else{
                                    readBuffer[readBufferPosition++] = tempByte;
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }
            }
        });
        workerThread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_CODE){
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "위치 권한을 허용하였습니다.", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getContext(), "위치 권한을 허용해주셔야 합니다.", Toast.LENGTH_SHORT).show();
            }

            if (grantResults.length > 0 &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "블루투스 권한을 허용하였습니다.", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getContext(), "권한을 허용해주셔야 합니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "위치 권한을 허용하였습니다.", Toast.LENGTH_SHORT).show();
            } else{
//                requestPermissions(permissions, REQUEST_PERMISSION_UNDER_CODE);
                Toast.makeText(getContext(), "권한을 허용해주셔야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}