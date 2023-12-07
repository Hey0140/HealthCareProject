package com.example.myjavaapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MypetInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MyPetInfoData> itemList;
    private OnListItemSelected mListener;
    private Context context;
    public interface OnListItemSelected {
        void onItemSelected(View v, int position, int vg);
    }
    MypetInfoAdapter(ArrayList<MyPetInfoData> list, Context context){
        this.itemList = list;
        this.context = context;
    }
    public void setOnItemClickListener(OnListItemSelected listener){
        this.mListener = listener;
    }

    public class MypetInfoViewHolder  extends  RecyclerView.ViewHolder{
        TextView name;
        View imageIcon;
        CircleImageView image;
        public MypetInfoViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.myPetInfoName);
            imageIcon = itemView.findViewById(R.id.myPetInfoProfile);
            image = itemView.findViewById(R.id.myPetInfoView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemSelected(view, position, Code.ViewType.BASIC);
                        }
                    }
                }
            });
        }
    }

    public class MypetInfoAddViewHolder  extends  RecyclerView.ViewHolder{
        public MypetInfoAddViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemSelected(view, position, Code.ViewType.ADD);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == Code.ViewType.BASIC){
            v = inflater.inflate(R.layout.mypet_info_item_layout, parent, false);
            return new MypetInfoViewHolder(v);
        }
        else {
            v = inflater.inflate(R.layout.mypet_info_lastitem_layout, parent, false);
            return new MypetInfoAddViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MypetInfoViewHolder){
            MyPetInfoData item = itemList.get(position);

            if(!item.getImageId().equals("")){

                FirebaseStorage storage = FirebaseStorage.getInstance("gs://pet-healthcare-45a41.appspot.com");
                StorageReference storageReference = storage.getReference().child("images/").child("pet/").child(item.getImageId());
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //이미지 로드 성공시

                        Glide.with(context)
                                .load(uri)
                                .into(((MypetInfoViewHolder) holder).image);

                        ((MypetInfoViewHolder) holder).name.setText(item.getName());
                        ((MypetInfoViewHolder) holder).imageIcon.setVisibility(View.INVISIBLE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //이미지 로드 실패시
                    }
                });
            } else{
                ((MypetInfoViewHolder) holder).name.setText(item.getName());
                ((MypetInfoViewHolder) holder).imageIcon.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position){
        return itemList.get(position).getViewType();
    }

}
