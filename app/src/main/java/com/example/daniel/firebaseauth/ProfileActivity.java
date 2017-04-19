package com.example.daniel.firebaseauth;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Environment;
import java.util.Random;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.io.*;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    //view objects
    private TextView textViewUserEmail;
    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTelephone;
    private EditText DateBirth;
    private Button buttonSave;
    private Button buttonLogout;
    private String mFileName = null;
    private Button btnControl, btnClear;
    private WavAudioRecorder mRecorder;
    private static final String mRcordFilePath = Environment.getExternalStorageDirectory() + "/testwave.wav";
    //defining a database reference
    private DatabaseReference databaseReference;
    private TextView textDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //getting the views from xml resource
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTelephone = (EditText) findViewById(R.id.editTelephone);
        DateBirth = (EditText) findViewById(R.id.editDateBirth);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        //displaying logged in user name
        textViewUserEmail.setText("Benvenutto "+user.getEmail() );
        //adding listener to button
        buttonLogout.setOnClickListener(this);
        btnControl = (Button) this.findViewById(R.id.btnControl);
        btnControl.setText("Start");
        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(mRcordFilePath);
        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
                    mRecorder.prepare();
                    mRecorder.start();
                    btnControl.setText("Stop");
                } else if (WavAudioRecorder.State.ERROR == mRecorder.getState()) {
                    mRecorder.release();
                    mRecorder = WavAudioRecorder.getInstanse();
                    mRecorder.setOutputFile(mRcordFilePath);
                    btnControl.setText("Start");
                } else {
                    mRecorder.stop();
                    mRecorder.reset();
                    btnControl.setText("Start");
                }
            }
        });
        btnClear = (Button) this.findViewById(R.id.btnClear);
        btnClear.setText("Clear");
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File pcmFile = new File(mRcordFilePath);
                if (pcmFile.exists()) {
                    pcmFile.delete();
                }
            }
        });
        textDisplay = (TextView) this.findViewById(R.id.Textdisplay);
        textDisplay.setText("recording saved to: " + mRcordFilePath);

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
        Uri file = Uri.fromFile(new File("storage/emulated/0/testwave.wav"));
        StorageReference riversRef = mStorageRef.child("audio").child(user.getUid()).child("testwave.wav");
        riversRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }


    private void saveUserInformation() {
        //Getting values from database
        String name = editTextName.getText().toString().trim();
        String add = editTextAddress.getText().toString().trim();
        String tel = editTelephone.getText().toString().trim();
        String dby = DateBirth.getText().toString().trim();

        //creating a userinformation object
        UserInformation userInformation = new UserInformation(name, add, tel, dby);

        //getting the current logged in user
        FirebaseUser user = firebaseAuth.getCurrentUser();


        //saving data to firebase database
        /*
        * first we are creating a new child in firebase with the
        * unique id of logged in user
        * and then for that user under the unique id we are saving data
        * for saving data we are using setvalue method this method takes a normal java object
        * */
        databaseReference.child(user.getUid()).setValue(userInformation);

        //displaying a success toast
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
    }

    //@Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
        if(view == buttonSave){
            saveUserInformation();
            uploadAudio();
        }


    }
}