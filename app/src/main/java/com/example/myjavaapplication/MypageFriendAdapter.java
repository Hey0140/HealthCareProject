package com.example.myjavaapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MypageFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    ArrayList<MypageFriendItemData> itemList = new ArrayList<>();

    private MypageFriendAdapter.OnListDeleteSelected dListener;

    public interface OnListDeleteSelected {
        void onDeleteSelected(View v, int position);
    }

    MypageFriendAdapter(ArrayList<MypageFriendItemData> list, Context context){
        this.itemList = list;
        this.context = context;
    }

    public void setOnDeleteClickListener(MypageFriendAdapter.OnListDeleteSelected dlistener){
        this.dListener = dlistener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        v = inflater.inflate(R.layout.friend_item_show_layout, parent, false);
        return new MypageFriendAdapter.MypageFriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MypageFriendAdapter.MypageFriendViewHolder) {
            MypageFriendItemData item = itemList.get(position);

            if (!item.getFrImageUrl().equals("")) {

                FirebaseStorage storage = FirebaseStorage.getInstance("gs://pet-healthcare-45a41.appspot.com");
                StorageReference storageReference = storage.getReference().child("images/").child("users/").child(item.getFrUid());
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //이미지 로드 성공시

                        Glide.with(context)
                                .load(uri)
                                .into(((MypageFriendAdapter.MypageFriendViewHolder) holder).profileImageView);


                        ((MypageFriendAdapter.MypageFriendViewHolder) holder).profileIcon.setVisibility(View.INVISIBLE);
                        ((MypageFriendAdapter.MypageFriendViewHolder) holder).name.setText(item.getFrName());
                        ((MypageFriendAdapter.MypageFriendViewHolder) holder).email.setText(item.getFrEmail());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        ((MypageFriendAdapter.MypageFriendViewHolder) holder).profileIcon.setVisibility(View.VISIBLE);
                        ((MypageFriendAdapter.MypageFriendViewHolder) holder).name.setText(item.getFrName());
                        ((MypageFriendAdapter.MypageFriendViewHolder) holder).email.setText(item.getFrEmail());
                    }
                });
            }else{
                ((MypageFriendAdapter.MypageFriendViewHolder) holder).profileIcon.setVisibility(View.VISIBLE);
                ((MypageFriendAdapter.MypageFriendViewHolder) holder).name.setText(item.getFrName());
                ((MypageFriendAdapter.MypageFriendViewHolder) holder).email.setText(item.getFrEmail());
            }
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public class MypageFriendViewHolder  extends  RecyclerView.ViewHolder{
        TextView name;
        TextView email;
        CircleImageView profileImageView;
        View profileIcon, deleteButton;

        public MypageFriendViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.myfriendNameText2);
            email = itemView.findViewById(R.id.myfriendEmailText2);
            profileIcon = itemView.findViewById(R.id.myfriendProfileIcon2);
            profileImageView = itemView.findViewById(R.id.myfriendProfileImage2);
            deleteButton = itemView.findViewById(R.id.myfriendDeleteIcon);


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(dListener != null){
                            dListener.onDeleteSelected(view, position);
                        }
                    }
                }
            });
        }
    }

}
