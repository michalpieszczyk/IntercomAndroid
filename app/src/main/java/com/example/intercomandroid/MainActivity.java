package com.example.intercomandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ImageView MyFaceHomeScreen;
    private Button ButtonCall;
    private Button ButtonSendData;
    Member member;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);



        MyFaceHomeScreen = (ImageView)findViewById(R.id.MyFaceHomeScreen);
        ButtonCall = (Button)findViewById(R.id.ButtonCall);
        ButtonSendData = (Button) findViewById(R.id.ButtonSendData);
        member = new Member();

        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        ButtonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                reff.push().setValue(member);
                Toast.makeText(MainActivity.this,"Succesfuly added data to firebase!",Toast.LENGTH_LONG).show();

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
            Bitmap b = (Bitmap)data.getExtras().get("data");
            //member.setImage(b);
            MyFaceHomeScreen.setImageBitmap(b);
        }
    }

}
