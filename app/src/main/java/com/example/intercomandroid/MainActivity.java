package com.example.intercomandroid;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ImageView MyFaceHomeScreen;
    private Button ButtonCall;
    private Button ButtonSendData;
    public Uri file;
    public Bitmap b;
    Member member;
    DatabaseReference reff;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");


        MyFaceHomeScreen = (ImageView)findViewById(R.id.MyFaceHomeScreen);
        ButtonCall = (Button)findViewById(R.id.ButtonCall);
        ButtonSendData = (Button) findViewById(R.id.ButtonSendData);
        member = new Member();

        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        ButtonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri file = getImageUri(null,b);
                StorageReference Ref = mStorageRef.child(member.getDate()+'.'+getExtension(file));

                Ref.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });

            }
        });

        ButtonCall.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              takePicture();



           }
       });
    }

    private void takePicture() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            b = (Bitmap)data.getExtras().get("data");
            member.setImage(b);
            member.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            MyFaceHomeScreen.setImageBitmap(b);
            reff.push().setValue(member.getDate());
            Toast.makeText(MainActivity.this,"Succesfuly added data to firebase!",Toast.LENGTH_LONG).show();

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

}
