package com.hantama.climberschat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hantama.climberschat.Chat;
import com.hantama.climberschat.MessageActivity;
import com.hantama.climberschat.R;
import com.hantama.climberschat.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;
    private CircleImageView profile_image1;


    FirebaseUser fuser;
    DatabaseReference reference;
    User my;
    private String myImgUrl;
    public MessageAdapter (Context mContext,List<Chat> mChat,String imageurl,String myImageUrl){
        this.mContext=mContext;
        this.mChat=mChat;
        this.imageurl=imageurl;
        this.myImgUrl=myImageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);

            /*
            profile_image1=view.findViewById(R.id.profile_image1);
            if (myImgUrl.equals("default")){
                profile_image1.setImageResource(R.mipmap.ic_launcher);
            }
            else{
                //viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
                Glide.with(mContext).load(myImgUrl).into(profile_image1);
            }*/
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);
            profile_image1=view.findViewById(R.id.profile_image1);
            if (imageurl.equals("default")){
                profile_image1.setImageResource(R.mipmap.ic_launcher);
            }
            else{
                //viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
                Glide.with(mContext).load(imageurl).into(profile_image1);
            }
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chat chat = mChat.get(i);
        viewHolder.show_message.setText(chat.getMessage());
        if (imageurl.equals("default")){
            //viewHolder.profile_image1.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            //viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
            //Glide.with(mContext).load(imageurl).into(viewHolder.profile_image1);
        }
        viewHolder.time.setText(chat.getDate());
        if(chat.getSender().equals(fuser.getUid()) && chat.isRead()){
            viewHolder.read.setVisibility(View.VISIBLE);
        }
        else if(chat.getSender().equals(fuser.getUid()) && !chat.isRead()) {viewHolder.read.setVisibility(View.GONE);}



    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image1;
        public TextView read;
        public TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_image1=itemView.findViewById(R.id.profile_image1);
            read=itemView.findViewById(R.id.read);
            time=itemView.findViewById(R.id.date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            //mChat.set(position,null);
            return MSG_TYPE_RIGHT;
        }
        else {
            //mChat.set(position,null);
            return MSG_TYPE_LEFT;

        }

    }
}
