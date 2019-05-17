package com.hantama.climberschat;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.hantama.climberschat.Adapter.MessageAdapter;
import com.hantama.climberschat.Fragments.APIService;
import com.hantama.climberschat.Notification.Client;
import com.hantama.climberschat.Notification.Data;
import com.hantama.climberschat.Notification.MyResponse;
import com.hantama.climberschat.Notification.Sender;
import com.hantama.climberschat.Notification.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profil_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    //User me;
    String myImgUrl="default";

    TextView onText;
    CircleImageView onImg;

    String time ="12:22 PM";

    Intent intent;

    ValueEventListener seenListener;
    ValueEventListener countListener;


    APIService apiService;

    boolean notify= true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(MessageActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                //finish();
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profil_image =findViewById(R.id.profile_image);

        username=findViewById(R.id.username);
        btn_send=findViewById(R.id.btn_send);
        text_send=findViewById(R.id.text_send);

        onText = findViewById(R.id.on);
        onImg = findViewById(R.id.onImg);

        mChat=new ArrayList<>();

        intent=getIntent();
        final String userid=intent.getStringExtra("userid");



        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(),userid,msg);
                }
                text_send.setText("");
            }
        });

        fuser= FirebaseAuth.getInstance().getCurrentUser();


        //additional
        //reference = FirebaseDatabase.getInstance().getReference("Users").child(myID);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String enemyUrl;
                User user = dataSnapshot.getValue(User.class);
                enemyUrl= user.getImageURL();
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profil_image.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profil_image);
                }
                if (user.getStatus().equals("online")){onText.setVisibility(View.VISIBLE); onImg.setVisibility(View.VISIBLE);}
                else{onText.setVisibility(View.GONE); onImg.setVisibility(View.GONE);}
                readMessage(fuser.getUid(),userid,user.getImageURL(),myImgUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(userid);
    }
    private void sendMessage(String sender, final String receiver, String message){
        DatabaseReference reference;
        final DatabaseReference chatRef2;
        chatRef2=FirebaseDatabase.getInstance().getReference();
        reference=FirebaseDatabase.getInstance().getReference("date").child(fuser.getUid());
        HashMap<String,Object> map = new HashMap<>();
        map.put("time",ServerValue.TIMESTAMP);
        reference.setValue(map);

        final String sen,rec,mess;
        sen=sender;
        rec=receiver;
        mess=message;

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DateTime dt = dataSnapshot.getValue(DateTime.class);


                HashMap<String,Object> hashMap;
                hashMap = new HashMap<>();
                hashMap.put("sender",sen);
                hashMap.put("receiver", rec);
                hashMap.put("message", mess);
                hashMap.put("read",false);

                hashMap.put("date",dt.getJam());
                chatRef2.child("Chats").push().setValue(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid()).child(receiver);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastMess;
                Chatlist cl= dataSnapshot.getValue(Chatlist.class);

                if (mess.length()>=20){
                    lastMess=mess.substring(0,20)+"...";
                }
                else{
                    lastMess=mess;
                }
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(receiver);
                    chatRef.child("lastMessage").setValue(lastMess);
                    chatRef.child("countMessage").setValue(0);

                    //read for group
                    //chatRef.child("read").setValue(false);
                }
                else if(cl.getId().equals("")){
                    chatRef.child("id").setValue(receiver);
                    chatRef.child("lastMessage").setValue(lastMess);
                    chatRef.child("countMessage").setValue(0);
                }
                else {
                    chatRef.child("lastMessage").setValue(lastMess);
                    chatRef.child("countMessage").setValue(0);
                    //read for group
                    //chatRef.child("read").setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist").child(receiver).child(fuser.getUid());
        chatRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastMess;
                Chatlist cl=dataSnapshot.getValue(Chatlist.class);
                if (mess.length()>=20){
                    lastMess=mess.substring(0,20)+"...";
                }
                else{
                    lastMess=mess;
                }
                if (!dataSnapshot.exists()){
                    chatRef1.child("id").setValue(fuser.getUid());
                    chatRef1.child("lastMessage").setValue(lastMess);
                    chatRef1.child("countMessage").setValue(1);

                }
                else {
                    chatRef1.child("lastMessage").setValue(lastMess);
                    chatRef1.child("countMessage").setValue(cl.getCountMessage()+1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                sendNotification(receiver,user.getUsername(),msg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotification(final String receiver, final String sender, final String msg){
        final DatabaseReference token = FirebaseDatabase.getInstance().getReference("Token");
        Query query =token.orderByKey().equalTo(receiver);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(),R.mipmap.ic_launcher,sender+" :"+msg,"Pesan Baru",receiver);

                    Sender send=new Sender(data,token.getToken());

                    apiService.sendNotification(send).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code()==200){
                                if(response.body().success!=1){
                                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private  void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("read",true);
                        snapshot.getRef().updateChildren(map);

                    }
                }
                FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid()).child(userid).child("countMessage").setValue(0);
               // Toast.makeText(getApplicationContext(), "Read", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessage(final String myid, final String userid, final String imageurl, final String myImgUrl){
        mChat =new ArrayList<>();
        reference =FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                DatabaseReference readRef;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        //to make the chat read
                        mChat.add(chat);
                    }
                    //start the adapter
                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageurl,myImgUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
    private void status(String status){
        reference =FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String,Object> map= new HashMap<>();
        map.put("status",status);
        reference.updateChildren(map);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //status("online");
    }

    @Override
    protected void onPause() {
        reference.removeEventListener(seenListener);
        super.onPause();
        //status("offline");
    }
}
