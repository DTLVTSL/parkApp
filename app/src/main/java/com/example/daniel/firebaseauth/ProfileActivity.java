package com.example.daniel.firebaseauth;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;






public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    //view objects

    private TextView editCodicFisc;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText DateBirth;
    private EditText gender;
    private EditText CodiceMedico;
    private Bundle b;
    private Button buttonSave;
    public String codiceMedico=new String();
   // private String name;

    //defining a database reference
    private DatabaseReference databaseReference;
    //private FirebaseListAdapter <Medici> FirebaseListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        setTitle("Registrazione");
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
        editCodicFisc = (EditText) findViewById(R.id.editCodicFisc);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSurname = (EditText) findViewById(R.id.editTextSurname);
        DateBirth = (EditText) findViewById(R.id.editDateBirth);
        gender = (EditText) findViewById(R.id.editTextgender);
        CodiceMedico = (EditText) findViewById(R.id.editCodiceMedico);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();}
        //initializing views


    private void saveUserInformation() {
        //Getting values from database
        String name = editTextName.getText().toString().trim();
        String sur = editTextSurname.getText().toString().trim();
        String cod = editCodicFisc.getText().toString().trim();
        String dby = DateBirth.getText().toString().trim();
        String gen = gender.getText().toString().trim();
        String codmed = CodiceMedico.getText().toString().trim();

       // String userId = "userId";
        //creating a userinformation object
        UserInformation userInformation = new UserInformation(name, sur, cod, dby,gen,codmed);

        // codiceMedico=userInformation.getCodMedico().toString();
        codiceMedico=new String(userInformation.getCodMedico());
        //getting the current logged in user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //saving data to firebase database
        /*
        * first we are creating a new child in firebase with the
        * unique id of logged in user
        * and then for that user under the unique id we are saving data
        * for saving data we are using setvalue method this method takes a normal java object
        * */
        databaseReference.child("Medici").child(codmed).child("Pazienti").child(user.getUid()).child("profile").setValue(userInformation);


        //cosi era prima
        //databaseReference.child("users").child(user.getUid()).child("profile").setValue(userInformation);

        //displaying a success toast
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
    }


    //@Override
    public void onClick(View view) {

        if(view == buttonSave){
            saveUserInformation();
            finish();
            Intent intent = new Intent(getApplicationContext(),NavigationActivity.class);
            //intent.putExtra("Name",editTextName);
            intent.putExtra("CodiceMedd",codiceMedico);
            startActivity(intent);

        }
    }
}