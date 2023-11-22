package com.example.myjavaapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class MonthWalkActivity extends AppCompatActivity {
    private ArrayList<MonthWalkData> itemList;
    private final int REQUEST_CODE = 2032;
    private Bitmap image;
    private TextView plus;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthwalk_page);

        itemList = new ArrayList<>();
        MonthWalkData monthWalkData = new MonthWalkData();
        monthWalkData.setImage("");

        itemList.add(monthWalkData);

        RecyclerView recyclerView = findViewById(R.id.monthWalkRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        MonthWalkAdapter adapter = new MonthWalkAdapter(itemList);
        adapter.setOnItemClickListener(new MonthWalkAdapter.OnListItemSelected() {
            @Override
            public void onItemSelected(View v, int position) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);

                itemList.get(position).setType(Code.ViewType.BITMAP);
                itemList.get(position).setBitmap(image);
            }
        });
        recyclerView.setAdapter(adapter);
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
                }catch (Exception e){

                }
            }
        }
    }


}