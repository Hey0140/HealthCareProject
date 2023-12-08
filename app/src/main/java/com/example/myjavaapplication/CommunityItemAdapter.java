package com.example.myjavaapplication;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

public class CommunityItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<CommunityItemData> itemList = new ArrayList<>();
    private CommunityItemAdapter.OnListItemSelected mListener;
    private CommunityItemAdapter.OnListHeartSelected mheartListener;

    public interface OnListItemSelected {
        void onItemSelected(View v, int position, int vg);
    }
    public interface OnListHeartSelected {
        void onHeartSelected(View v, int position, int vg);
    }

    CommunityItemAdapter(ArrayList<CommunityItemData> list, Context context){
        this.itemList = list;
        this.context = context;
    }

    public void setOnItemClickListener(CommunityItemAdapter.OnListItemSelected listener){
        this.mListener = listener;
    }
    public void setOnHeartClickListener(CommunityItemAdapter.OnListHeartSelected hlistener){
        this.mheartListener = hlistener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == Code.ViewType.IMAGE){
            v = inflater.inflate(R.layout.community_item_layout, parent, false);
            return new CommunityItemAdapter.CommunityItemViewHolder(v);
        }
        else {
            v = inflater.inflate(R.layout.community_item_noimage_layout, parent, false);
            return new CommunityItemAdapter.CommunityItemTextViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommunityItemAdapter.CommunityItemViewHolder) {
            CommunityItemData item = itemList.get(position);

            if (!item.getImageUri().equals("")) {

                FirebaseStorage storage = FirebaseStorage.getInstance("gs://pet-healthcare-45a41.appspot.com");
                StorageReference storageReference = storage.getReference().child("images/").child("community/").child(item.getComId());
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //이미지 로드 성공시

                        Glide.with(context)
                                .load(uri)
                                .into(((CommunityItemAdapter.CommunityItemViewHolder) holder).imageView);

                        ((CommunityItemAdapter.CommunityItemViewHolder) holder).title.setText(item.getTitle());
                        ((CommunityItemAdapter.CommunityItemViewHolder) holder).body.setText(item.getBody());
                        ((CommunityItemAdapter.CommunityItemViewHolder) holder).heartNumber.setText(String.valueOf(item.getHeartNumber()));
                        ((CommunityItemAdapter.CommunityItemViewHolder) holder).commentNumber.setText(String.valueOf(item.getCommentNumber()));


                        if (item.isHeart()) {
                            ((CommunityItemAdapter.CommunityItemViewHolder) holder).heartImage.setBackgroundResource(R.drawable.filled_favorite_icon);
                        } else {
                            ((CommunityItemAdapter.CommunityItemViewHolder) holder).heartImage.setBackgroundResource(R.drawable.not_fill_favorite_icon);
                        }
                    }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //이미지 로드 실패시
                        }
                    });
                }
            } else {
                CommunityItemData item = itemList.get(position);

                ((CommunityItemAdapter.CommunityItemTextViewHolder) holder).title.setText(item.getTitle());
                ((CommunityItemAdapter.CommunityItemTextViewHolder) holder).body.setText(item.getBody());
                ((CommunityItemAdapter.CommunityItemTextViewHolder) holder).heartNumber.setText(String.valueOf(item.getHeartNumber()));
                ((CommunityItemAdapter.CommunityItemTextViewHolder) holder).commentNumber.setText(String.valueOf(item.getCommentNumber()));

                if (item.isHeart()) {
                    ((CommunityItemAdapter.CommunityItemTextViewHolder) holder).heartImage.setBackgroundResource(R.drawable.filled_favorite_icon);
                } else {
                    ((CommunityItemAdapter.CommunityItemTextViewHolder) holder).heartImage.setBackgroundResource(R.drawable.not_fill_favorite_icon);
                }
            }
        }


    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public class CommunityItemViewHolder  extends  RecyclerView.ViewHolder{
        TextView title;
        TextView body;
        View heartImage;
        TextView heartNumber;
        TextView commentNumber;

        CircleImageView imageView;

        public CommunityItemViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.communityItemTitle);
            body = itemView.findViewById(R.id.communityItemBody);
            heartImage = itemView.findViewById(R.id.communityHeartButton);
            heartNumber = itemView.findViewById(R.id.communityHeartNumber);
            commentNumber = itemView.findViewById(R.id.communityCommentNumber);

            imageView = itemView.findViewById(R.id.communityImageView);

            heartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mheartListener != null){
                            mheartListener.onHeartSelected(view, position, Code.ViewType.IMAGE);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemSelected(view, position, Code.ViewType.IMAGE);
                        }
                    }
                }
            });

        }
    }

    public class CommunityItemTextViewHolder  extends  RecyclerView.ViewHolder{
        TextView title;
        TextView body;
        View heartImage;
        TextView heartNumber;
        TextView commentNumber;

        public CommunityItemTextViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.communityItemTitle2);
            body = itemView.findViewById(R.id.communityItemBody2);
            heartImage = itemView.findViewById(R.id.communityHeartButton2);
            heartNumber = itemView.findViewById(R.id.communityHeartNumber2);
            commentNumber = itemView.findViewById(R.id.communityCommentNumber2);

            heartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mheartListener != null){
                            mheartListener.onHeartSelected(view, position, Code.ViewType.TEXT);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemSelected(view, position, Code.ViewType.TEXT);
                        }
                    }
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position){
        return itemList.get(position).getViewType();
    }
}
