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

public class CommentItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<CommentItemData> itemList = new ArrayList<>();
    private CommentItemAdapter.OnListItemSelected mListener;

    public interface OnListItemSelected {
        void onItemSelected(View v, int position);
    }

    CommentItemAdapter(ArrayList<CommentItemData> list, Context context){
        this.itemList = list;
        this.context = context;
    }

    public void setOnItemClickListener(CommentItemAdapter.OnListItemSelected listener){
        this.mListener = listener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        v = inflater.inflate(R.layout.comment_item_layout, parent, false);
        return new CommentItemAdapter.CommentItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentItemAdapter.CommentItemViewHolder) {
            CommentItemData item = itemList.get(position);

            ((CommentItemAdapter.CommentItemViewHolder) holder).name.setText(item.getName());
            ((CommentItemAdapter.CommentItemViewHolder) holder).body.setText(item.getBody());
            ((CommentItemAdapter.CommentItemViewHolder) holder).timeStamp.setText(item.getTimeStamp());

        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class CommentItemViewHolder  extends  RecyclerView.ViewHolder{
        TextView name;
        TextView body;
        TextView timeStamp;

        public CommentItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.communityCommentItemTitle);
            body = itemView.findViewById(R.id.communityCommentItemBody);
            timeStamp = itemView.findViewById(R.id.commentTimestamp);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemSelected(view, position);
                        }
                    }
                }
            });

        }
    }


    @Override
    public int getItemViewType(int position){
        return 0;
    }
}
