package com.hantama.climberschat.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hantama.climberschat.DateTime;
import com.hantama.climberschat.R;
import com.hantama.climberschat.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static java.text.DateFormat.getDateTimeInstance;


public class ProfileFragment extends Fragment {
    private CircleImageView profile_image;
    private TextView username;
    DatabaseReference reference;
    FirebaseUser fuser;
    String newName;
    String newInfo;

    StorageReference storageReference;
    public static final int IMAGE_REQUEST=1;
    private Uri imageUri;
    private StorageTask uploadTask;

    private TextView info;
    private TextView luck;
    String asd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        profile_image=view.findViewById(R.id.profile_image);
        username=view.findViewById(R.id.username);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference("uploads");

        info=view.findViewById(R.id.info);
        luck=view.findViewById(R.id.luck);

        reference= FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User me =dataSnapshot.getValue(User.class);
                if(me.getImageURL().equals("default")){}
                else{
                    if(isAdded())
                    Glide.with(getContext()).load(me.getImageURL()).into(profile_image);
                }
                username.setText(me.getUsername());
                info.setText(me.getInfo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();

            }
        });
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Masukan Nama ");
                final EditText input_field= new EditText(getContext());

                builder.setView(input_field);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newName = input_field.getText().toString();

                        if ( (!newName.contains("\n")) && (!newName.isEmpty()) ){
                            HashMap<String, Object> hash1 = new HashMap<>();
                            hash1.put("username", newName);
                            reference.updateChildren(hash1);
                        }
                        else {
                            if (newName.isEmpty())
                                Toast.makeText(getContext(), "isi dulu dong Goblok", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Jangan isi ENTER", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();


            }
        });

        //luck
        luck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("time",ServerValue.TIMESTAMP);

                reference=FirebaseDatabase.getInstance().getReference("date").child(fuser.getUid());
                reference.setValue(hashMap);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DateTime d1=dataSnapshot.getValue(DateTime.class);
                        asd=d1.getJam();
                        luck.setText(asd);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                /*String a=d1.getTimeDate();
                Pattern p = Pattern.compile("\\d{2}\\:\\d{2}");
                Matcher m = p.matcher(d1.getTimeDate());

                if (m.find()) {
                    String b=m.group()+a.substring(a.length()-3);

                    luck.setText(b);
                }*/


                /*
                int a=rand.nextInt(10)+1;
                if (a>=1&&a <=5)
                    luck.setText("Kurang beruntung");
                else if (a>5&&a<8)
                    luck.setText("Lumayan beruntung");
                else luck.setText("Anda sangat beruntung");*/
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Masukan Status ");
                final EditText input_field= new EditText(getContext());
                builder.setView(input_field);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newInfo = input_field.getText().toString();
                        if ( (!newInfo.contains("\n")) && (!newInfo.isEmpty()) ) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("info", newInfo);
                            reference.updateChildren(hashMap);
                        }
                        else {
                            if((newInfo.contains("\n")) || (newInfo.isEmpty()))
                                Toast.makeText(getContext(), "Jangan isi ENTER/ketik dulu", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Kebanyakan coeg 50 karakter max", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


        return view;
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);


    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver =getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading!");
        pd.show();

        if(imageUri!=null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
            +"."+getFileExtension(imageUri));
            uploadTask =fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        String mUri = downloadUri.toString();

                        reference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

                        HashMap<String,Object> map= new HashMap<>();
                        map.put("imageURL",mUri);
                        reference.updateChildren(map);
                        pd.dismiss();
                        Toast.makeText(getContext(), "Berhasil Coeg, Sinyal kenceng nih yee", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Upload gagal, Sinyal lu kek tai :V", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else {
            Toast.makeText(getContext(), "halah Udah repot-repot malah ga jadi Tai lah :V", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!= null){
            imageUri=data.getData();
                if(isAdded())
                Toast.makeText(getContext(), "asdasdasd", Toast.LENGTH_SHORT).show();
            if (uploadTask!=null &&uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Lagi ngupload nih njing", Toast.LENGTH_SHORT).show();
            }
            else{
                uploadImage();
            }
        }
    }


}
