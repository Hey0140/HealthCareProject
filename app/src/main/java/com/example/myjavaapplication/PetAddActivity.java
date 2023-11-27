package com.example.myjavaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetAddActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 2001;
    private final long NONE = 0;
    private final long MALE = 11;
    private final long FEMALE = 12;
    private final long MALENEUTER = 13;
    private final long FEMALENEUTER = 14;
    private final long SMALL = 21;
    private final long MIDDLE = 22;
    private final long BIG = 23;
    private UserMedia userData;
    private ArrayList<PetMedia> petDataList;
    private PetMedia petData;
    private int petCount;
    private long petSex = 0;
    private long petKind = 0;
    private CircleImageView petProfileBox;
    private View petProfileImage;

    private EditText petName, petBirth, petWeight;

    private CheckBox male, female, maleNeutering, femaleNeutering;
    private Button petAddNextButton, petKindButton;
    private Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petadd_page);

        petProfileBox = findViewById(R.id.petProfileBox);
        petProfileImage = findViewById(R.id.petProfileImage);
        petName = findViewById(R.id.petName);
        petKindButton = findViewById(R.id.petKindButton);
        petBirth = findViewById(R.id.petBirth);
        petWeight = findViewById(R.id.petWeight);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        maleNeutering = findViewById(R.id.maleNeutering);
        femaleNeutering = findViewById(R.id.femaleNeutering);

        petAddNextButton = findViewById(R.id.petAddNextButton);

        petAddNextButton.setOnClickListener(this);
        petKindButton.setOnClickListener(this);
        petProfileBox.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        maleNeutering.setOnClickListener(this);
        femaleNeutering.setOnClickListener(this);

        image = null;
        petData = new PetMedia();

        petBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength;
                if(petBirth.isFocusable() && !s.toString().equals("")) {
                    try{
                        textlength = petBirth.getText().toString().length();
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        return;
                    }

                    if (textlength == 4 && before != 1) {

                        petBirth.setText(petBirth.getText().toString()+".");
                        petBirth.setSelection(petBirth.getText().length());

                    }else if (textlength == 7&& before != 1){

                        petBirth.setText(petBirth.getText().toString()+".");
                        petBirth.setSelection(petBirth.getText().length());

                    }else if(textlength == 5 && !petBirth.getText().toString().contains(".")){

                        petBirth.setText(petBirth.getText().toString().substring(0,4)+"."+petBirth.getText().toString().substring(4));
                        petBirth.setSelection(petBirth.getText().length());

                    }else if(textlength == 8 && !petBirth.getText().toString().substring(7,8).equals(".")){

                        petBirth.setText(petBirth.getText().toString().substring(0,7)+"."+petBirth.getText().toString().substring(7));
                        petBirth.setSelection(petBirth.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Intent intent = new Intent(this.getIntent());
        userData = (UserMedia) intent.getSerializableExtra("userData");
        petDataList = (ArrayList<PetMedia>) intent.getSerializableExtra("petDataList");

        petCount = userData.getCount();
    }


    @Override
    public void onClick(View v) {
        if(v == petAddNextButton){
            if(petName.getText().toString().length() < 1){
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if(petKind == NONE){
                Toast.makeText(this, "품종을 선택해주세요", Toast.LENGTH_SHORT).show();
            } else if(petSex == NONE){
                Toast.makeText(this, "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
            } else if(petBirth.getText().toString().length() < 10){
                Toast.makeText(this, "생일을 선택해주세요", Toast.LENGTH_SHORT).show();
            } else if(petWeight.getText().toString().length() < 1) {
                Toast.makeText(this, "몸무게를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                onSetPetData();

                Intent intent = new Intent(getApplicationContext(), PetAdd2Activity.class);
                intent.putExtra("userData", userData);
                intent.putExtra("petDataList", petDataList);
                intent.putExtra("petData", petData);
                startActivity(intent);
            }
        }
        if ( v== petProfileBox){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_CODE);
        }
        if( v== petKindButton){
            PetKindSelectedDialog dialog = new PetKindSelectedDialog(this);
            dialog.setKindDialogListener(new PetKindSelectedDialog.OnPetKindDialogListener() {
                @Override
                public void onPetKindSelected(String data) {
                    petKindButton.setText(data);
                    if( data.equals("소형견")){
                        petKind = SMALL;
                    } else if ( data.equals("중형견")){
                        petKind = MIDDLE;
                    } else if ( data.equals("대형견")){
                        petKind = BIG;
                    } else {
                        petKind = NONE;
                    }
                }
            });
            dialog.show();
        }
        if ( v== male){
            if (male.isChecked()){
                male.setChecked(true);
                female.setChecked(false);
                maleNeutering.setChecked(false);
                femaleNeutering.setChecked(false);
                petSex = MALE;
            }else{
                petSex = NONE;
            }
        }
        if ( v== female){
            if (female.isChecked()){
                male.setChecked(false);
                female.setChecked(true);
                maleNeutering.setChecked(false);
                femaleNeutering.setChecked(false);
                petSex = FEMALE;
            }else{
                petSex = NONE;
            }
        }
        if ( v== maleNeutering){
            if (maleNeutering.isChecked()){
                male.setChecked(false);
                female.setChecked(false);
                maleNeutering.setChecked(true);
                femaleNeutering.setChecked(false);
                petSex = MALENEUTER;
            }else{
                petSex = NONE;
            }
        }
        if ( v== femaleNeutering){
            if (femaleNeutering.isChecked()){
                male.setChecked(false);
                female.setChecked(false);
                maleNeutering.setChecked(false);
                femaleNeutering.setChecked(true);
                petSex = FEMALENEUTER;
            }else{
                petSex = NONE;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    image = BitmapFactory.decodeStream(in);
                    in.close();
                    petProfileBox.setImageBitmap(image);
                    petProfileImage.setVisibility(View.INVISIBLE);
                }catch (Exception e){

                }
            }
        }
    }

    private void onSetPetData(){
        petData.setuId(userData.getUid());
        if(image != null){
            petData.setImage(image.toString());
        }
        else{
            petData.setImage("");
        }
        petData.setPetId(petCount);
        petData.setPetName(petName.getText().toString());
        petData.setPetKind(petKind);
        petData.setPetSex(petSex);
        petData.setPetBirth(petBirth.getText().toString());
        petData.setPetWeight(Long.parseLong(petWeight.getText().toString()));
    }
}