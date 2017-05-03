package com.example.daniel.firebaseauth;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSave;
    private Button mSelectImage;
    private Button mSelectImageCamera;
    private ImageView imageView2;
    private EditText editTextUserId;
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_INTENT = 2;
    //defining firebaseauth object
    public FirebaseAuth firebaseAuth;
    DatabaseReference databaseUser;
    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    private Bundle b;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Mio profilo");
        setContentView(R.layout.activity_perfil);
        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        databaseUser = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        b = getIntent().getExtras();

        buttonSave = (Button) findViewById(R.id.buttonSave);


        mSelectImage = (Button) findViewById(R.id.selectImage);
        mSelectImageCamera = (Button) findViewById(R.id.selectImageCamera);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        editTextUserId = (EditText) findViewById(R.id.editTextUserId);
        buttonSave.setOnClickListener(this);
        mSelectImage.setOnClickListener(this);
        mSelectImageCamera.setOnClickListener(this);

    }


    private void addUserId() {
        //FirebaseAuth group = firebaseAuth.getInstance();
        String id = firebaseAuth.getCurrentUser().getUid();
        String userId = editTextUserId.getText().toString().trim();

        if (!TextUtils.isEmpty( userId)) {
            if(b != null){
                name = b.getString("Name");
                databaseUser.child(id).child(name).child("userId").setValue(userId);
                Toast.makeText(this, "Informazione Adjunta", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "You should enter  a name of your Group", Toast.LENGTH_LONG).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            imageView2.setImageURI(imageUri);
            StorageReference filepath = mStorage.child("photosProfile").child(user.getUid()).child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                public void onSuccess(  UploadTask.TaskSnapshot taskSnapshot){
                    Toast.makeText(PerfilActivity.this, "Upload Done ",Toast.LENGTH_LONG).show();
                    //progressDialog.setMessage("Uploading, please wait...");
                }
            });

        }
        else if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            imageView2.setImageURI(imageUri);
            FirebaseUser user = firebaseAuth.getCurrentUser();
            StorageReference filepath = mStorage.child("photosProfile").child(user.getUid()).child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                public void onSuccess(  UploadTask.TaskSnapshot taskSnapshot){
                    Toast.makeText(PerfilActivity.this, "Upload Done ",Toast.LENGTH_LONG).show();
                    //progressDialog.setMessage("Uploading, please wait...");
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        if(view == buttonSave){
            //closing activity
            addUserId();
            finish();
            //starting login activity getApplicationContext()
            Intent myIntent =new Intent(PerfilActivity.this,NavigationActivity.class);
            startActivity(myIntent);
        }
        if (view == mSelectImage){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);

        }
        if (view == mSelectImageCamera){
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }
}
