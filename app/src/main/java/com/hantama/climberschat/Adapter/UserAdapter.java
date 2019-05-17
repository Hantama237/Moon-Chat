package com.hantama.climberschat.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hantama.climberschat.MainActivity;
import com.hantama.climberschat.MessageActivity;
import com.hantama.climberschat.R;
import com.hantama.climberschat.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUser;
    private boolean isChat;


    public UserAdapter (Context mContext,List<User> mUser, boolean isChat){
        this.mContext=mContext;
        this.mUser=mUser;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user=mUser.get(i);
        if(user!=null) {
            viewHolder.username.setText(user.getUsername());
            if (user.getImageURL().equals("default")) {
                viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(mContext).load(user.getImageURL()).into(viewHolder.profile_image);
            }

        /*
        if(isChat){
            if(user.getStatus().equals("online")){
                viewHolder.on.setVisibility(View.VISIBLE);
                viewHolder.off.setVisibility(View.GONE);
            }
            else if(user.getStatus().equals("offline")){
                viewHolder.on.setVisibility(View.GONE);
                viewHolder.off.setVisibility(View.VISIBLE);
            }
        }
        */
            if (!isChat) {

                viewHolder.info.setText(user.getInfo());
            } else {
                int c = Integer.parseInt(user.getInfo().substring(0, 4).trim());
                if (c > 0) {
                    viewHolder.countMess.setVisibility(View.VISIBLE);
                    viewHolder.countMess.setText(user.getInfo().substring(0, 4).trim());
                    viewHolder.info.setTypeface(Typeface.DEFAULT_BOLD);
                }
                viewHolder.info.setText(user.getInfo().substring(4).trim());
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("userid", user.getId());
                    mContext.startActivity(intent);
                    //---to Read GROUP
                }
            });
            mUser.set(i, null);
        }
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        private CircleImageView on;
        private CircleImageView off;
        private TextView info;
        TextView countMess;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);
            on = itemView.findViewById(R.id.on);
            off = itemView.findViewById(R.id.off);
            info = itemView.findViewById(R.id.info);
            countMess=itemView.findViewById(R.id.countMessage);
        }
    }
}
