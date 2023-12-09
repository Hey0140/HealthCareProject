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

public class MypageCommunityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<CommunityItemData> itemList = new ArrayList<>();
    private MypageCommunityAdapter.OnListItemSelected mListener;
    private MypageCommunityAdapter.OnListHeartSelected mheartListener;

    public interface OnListItemSelected {
        void onItemSelected(View v, int position, int vg);
    }
    public interface OnListHeartSelected {
        void onHeartSelected(View v, int position, int vg);
    }

    MypageCommunityAdapter(ArrayList<CommunityItemData> list, Context context){
        this.itemList = list;
        this.context = context;
    }

    public void setOnItemClickListener(MypageCommunityAdapter.OnListItemSelected listener){
        this.mListener = listener;
    }
    public void setOnHeartClickListener(MypageCommunityAdapter.OnListHeartSelected hlistener){
        this.mheartListener = hlistener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == Code.ViewType.IMAGE){
            v = inflater.inflate(R.layout.mypagecommunity_item_layout, parent, false);
            return new MypageCommunityAdapter.MypageCommunityViewHolder(v);
        }
        else {
            v = inflater.inflate(R.layout.mypagecommunity_item_noimage_layout, parent, false);
            return new MypageCommunityAdapter.MypageCommunityTextViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MypageCommunityAdapter.MypageCommunityViewHolder) {
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
                                .into(((MypageCommunityAdapter.MypageCommunityViewHolder) holder).imageView);

                        ((MypageCommunityAdapter.MypageCommunityViewHolder) holder).title.setText(item.getTitle());
                        ((MypageCommunityAdapter.MypageCommunityViewHolder) holder).body.setText(item.getBody());
                        ((MypageCommunityAdapter.MypageCommunityViewHolder) holder).heartNumber.setText(String.valueOf(item.getHeartNumber()));
                        ((MypageCommunityAdapter.MypageCommunityViewHolder) holder).commentNumber.setText(String.valueOf(item.getCommentNumber()));


                        if (item.isHeart()) {
                            ((MypageCommunityAdapter.MypageCommunityViewHolder) holder).heartImage.setBackgroundResource(R.drawable.filled_favorite_icon);
                        } else {
                            ((MypageCommunityAdapter.MypageCommunityViewHolder) holder).heartImage.setBackgroundResource(R.drawable.not_fill_favorite_icon);
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

            ((MypageCommunityAdapter.MypageCommunityTextViewHolder) holder).title.setText(item.getTitle());
            ((MypageCommunityAdapter.MypageCommunityTextViewHolder) holder).body.setText(item.getBody());
            ((MypageCommunityAdapter.MypageCommunityTextViewHolder) holder).heartNumber.setText(String.valueOf(item.getHeartNumber()));
            ((MypageCommunityAdapter.MypageCommunityTextViewHolder) holder).commentNumber.setText(String.valueOf(item.getCommentNumber()));

            if (item.isHeart()) {
                ((MypageCommunityAdapter.MypageCommunityTextViewHolder) holder).heartImage.setBackgroundResource(R.drawable.filled_favorite_icon);
            } else {
                ((MypageCommunityAdapter.MypageCommunityTextViewHolder) holder).heartImage.setBackgroundResource(R.drawable.not_fill_favorite_icon);
            }
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public class MypageCommunityViewHolder  extends  RecyclerView.ViewHolder{
        TextView title;
        TextView body;
        View heartImage;
        TextView heartNumber;
        TextView commentNumber;

        CircleImageView imageView;

        public MypageCommunityViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.communityItemTitle3);
            body = itemView.findViewById(R.id.communityItemBody3);
            heartImage = itemView.findViewById(R.id.communityHeartButton3);
            heartNumber = itemView.findViewById(R.id.communityHeartNumber3);
            commentNumber = itemView.findViewById(R.id.communityCommentNumber3);

            imageView = itemView.findViewById(R.id.communityImageView3);

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

    public class MypageCommunityTextViewHolder  extends  RecyclerView.ViewHolder{
        TextView title;
        TextView body;
        View heartImage;
        TextView heartNumber;
        TextView commentNumber;

        public MypageCommunityTextViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.communityItemTitle4);
            body = itemView.findViewById(R.id.communityItemBody4);
            heartImage = itemView.findViewById(R.id.communityHeartButton4);
            heartNumber = itemView.findViewById(R.id.communityHeartNumber4);
            commentNumber = itemView.findViewById(R.id.communityCommentNumber4);

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
