package com.example.daniel.firebaseauth;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Environment;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.*;
import java.util.Date;



public class RecordActivity extends Fragment {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private Button buttonSend;
    private TextView ButtonLogout;
    private String mFileName = null;
    private WavAudioRecorder mRecorder;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    Date now = new Date();
    String fileName = formatter.format(now) + ".wav";
    private final String mRcordFilePath = Environment.getExternalStorageDirectory() + "/"+ fileName ;
    //defining a database reference
    private DatabaseReference databaseReference;


    //public void voidViewCreated(View view, @Nullable Bundle saveInstanceState){
      //  super.onViewCreated(view,saveInstanceState);
        //getActivity().setTitle("Mio profilo");

    //}
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_record,container,false);
        getActivity().setTitle("Prova");
        super.onCreate(savedInstanceState);
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            getActivity().finish();
            //starting login activity
            startActivity(new Intent(this.getActivity(), LoginActivity.class));
        }
        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //getting the views from xml resource
        buttonSend = (Button) getView().findViewById(R.id.buttonSend);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //initializing views
        ButtonLogout = (TextView) getView().findViewById(R.id.ButtonLogout);
        //adding listener to button
        ButtonLogout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //if logout is pressed
                if (view == ButtonLogout) {
                    //logging out the user
                    firebaseAuth.signOut();
                    //closing activity
                    getActivity().finish();
                    //starting login activity
                    Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(myIntent);

                }
            }

        });
        buttonSend.setText("Start");
        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(mRcordFilePath);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
                    mRecorder.prepare();
                    mRecorder.start();
                    buttonSend.setText("Stop");
                } else if (WavAudioRecorder.State.ERROR == mRecorder.getState()) {
                    mRecorder.release();
                    mRecorder = WavAudioRecorder.getInstanse();
                    mRecorder.setOutputFile(mRcordFilePath);
                    buttonSend.setText("Start");
                } else {
                    mRecorder.stop();
                    mRecorder.reset();
                    buttonSend.setText("Start");
                    uploadAudio();

                }
            }
        });
        return  view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mRecorder) {
            mRecorder.release();
        }
    }
    private void uploadAudio() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Uri file = Uri.fromFile(new File("storage/emulated/0/"+ fileName));
        StorageReference riversRef = mStorageRef.child("audio").child(user.getUid()).child(fileName);
        riversRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }



    //@Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == ButtonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            getActivity().finish();
            //starting login activity
            Intent myIntent = new Intent(this.getActivity(), LoginActivity.class);
            startActivity(myIntent);

        }
        if(view == buttonSend){
            if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
                mRecorder.prepare();
                mRecorder.start();
                buttonSend.setText("Stop");
            } else if (WavAudioRecorder.State.ERROR == mRecorder.getState()) {
                mRecorder.release();
                mRecorder = WavAudioRecorder.getInstanse();
                mRecorder.setOutputFile(mRcordFilePath);
                buttonSend.setText("Start");
            } else {
                mRecorder.stop();
                mRecorder.reset();
                buttonSend.setText("Start");
                uploadAudio();

            }

        }


    }
}

