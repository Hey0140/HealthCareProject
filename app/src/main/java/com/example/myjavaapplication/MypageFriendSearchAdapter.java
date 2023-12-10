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

public class MypageFriendSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    ArrayList<MypageFriendItemData> itemList = new ArrayList<>();

    private MypageFriendSearchAdapter.OnListAddSelected adListener;

    public interface OnListAddSelected {
        void onAddSelected(View v, int position);
    }

    MypageFriendSearchAdapter(ArrayList<MypageFriendItemData> list, Context context){
        this.itemList = list;
        this.context = context;
    }

    public void setOnAddClickListener(MypageFriendSearchAdapter.OnListAddSelected addlistener){
        this.adListener = addlistener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        v = inflater.inflate(R.layout.friend_item_layout, parent, false);
        return new MypageFriendSearchAdapter.MypageFriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MypageFriendSearchAdapter.MypageFriendViewHolder) {
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
                                .into(((MypageFriendSearchAdapter.MypageFriendViewHolder) holder).profileImageView);


                        ((MypageFriendSearchAdapter.MypageFriendViewHolder) holder).profileIcon.setVisibility(View.INVISIBLE);
                        ((MypageFriendSearchAdapter.MypageFriendViewHolder) holder).name.setText(item.getFrName());
                        ((MypageFriendSearchAdapter.MypageFriendViewHolder) holder).email.setText(item.getFrEmail());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        ((MypageFriendSearchAdapter.MypageFriendViewHolder) holder).profileIcon.setVisibility(View.VISIBLE);
                        ((MypageFriendSearchAdapter.MypageFriendViewHolder) holder).name.setText(item.getFrName());
                        ((MypageFriendSearchAdapter.MypageFriendViewHolder) holder).email.setText(item.getFrEmail());
                    }
                });
            }else{
                ((MypageFriendSearchAdapter.MypageFriendViewHolder) holder).profileIcon.setVisibility(View.VISIBLE);
                ((MypageFriendSearchAdapter.MypageFriendViewHolder) holder).name.setText(item.getFrName());
                ((MypageFriendSearchAdapter.MypageFriendViewHolder) holder).email.setText(item.getFrEmail());
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
        View profileIcon, addButton;

        public MypageFriendViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.myfriendNameText);
            email = itemView.findViewById(R.id.myfriendEmailText);
            profileIcon = itemView.findViewById(R.id.myfriendProfileIcon);
            profileImageView = itemView.findViewById(R.id.myfriendProfileImage);
            addButton = itemView.findViewById(R.id.myfriendAddIcon);


            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(adListener != null){
                            adListener.onAddSelected(view, position);
                        }
                    }
                }
            });
        }
    }

}
