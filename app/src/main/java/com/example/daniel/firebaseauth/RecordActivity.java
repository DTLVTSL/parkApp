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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Environment;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Random;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.io.*;
import java.util.Date;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;
    private Button buttonSend;
   // private Button buttonLogout;
    private String mFileName = null;
    private WavAudioRecorder mRecorder;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    Date now = new Date();
    String fileName = formatter.format(now) + ".wav";
    private final String mRcordFilePath = Environment.getExternalStorageDirectory() + "/"+ fileName ;

    //defining a database reference
    private DatabaseReference databaseReference;

    private String generatedFilepath;
    private String generatedFilepathB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        setTitle("Mio Test");
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
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();


        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //initializing views
        // buttonLogout = (Button) findViewById(R.id.ButtonLogout);
        //adding listener to button
        //buttonLogout.setOnClickListener(this);
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
                    buttonSend.setText("inviando");
                    uploadAudio();
                    sendlink();
                    finish();
                    Intent myIntent =new Intent(getApplicationContext(),StatisticsActivity.class);
                    startActivity(myIntent);

                }
            }
        });
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




    private void sendlink(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String id =user.getUid();

        //storageRef.child("statistics").child(user.getUid()).child("teste.csv").getDownloadUrl().getResult();
        mStorageRef.child("audio").child(id).child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                generatedFilepath = uri.toString();
                Log.i(generatedFilepath,"URL link" );

            }
        });


        /*
        final String url = "https://requestb.in/1c6oc3c1";//https://requestb.in/1c6oc3c1"https://172.20.242.72:5000/
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
            }},new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }){
            //How to put generatedFilepath  link inside the map???????in the line 173 #daniel
            protected Map<String, String> getParams() {
                // Inner object
                Map<String, String> params = new HashMap<String, String>();

                FirebaseUser user = firebaseAuth.getCurrentUser();
                String id =user.getUid();

                String audio_pos = "gs://parkinsonapp-7b987.appspot.com/audio/" + user.getUid() + "/" + fileName;
                String myUrl = "https://firebasestorage.googleapis.com/v0/b/parkinsonapp-7b987.appspot.com/o/audio%2FRivbQO2CBsZsIiWPyKvMmL97rYU2%2F2017_07_05_14_29_24.wav?alt=media&token=e882ce89-5edc-4247-9e09-a516dd5bbf5d";
                Map<String, String> MyData = new HashMap<String, String>();
                String link ="audio_url";
                String idi = "codicePaziente";
                String idii = "audio_position";

                MyData.put(link, myUrl);
                MyData.put(idi,id);
                MyData.put(idii,audio_pos);
                JSONObject userJSON = new JSONObject(MyData);
                // Nest the object at "user"
               // params.put("data", userJSON.toString());

                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        MyRequestQueue.add(MyStringRequest);

        }  */

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            final String url = "http://172.20.242.72:5000/";//https://requestb.in/1c6oc3c1"https://172.20.242.72:5000/
            String audio_pos = "gs://parkinsonapp-7b987.appspot.com/audio/" + user.getUid() + "/" + fileName;
            String myUrl = "https://firebasestorage.googleapis.com/v0/b/parkinsonapp-7b987.appspot.com/o/audio%2FRivbQO2CBsZsIiWPyKvMmL97rYU2%2F2017_07_05_14_29_24.wav?alt=media&token=e882ce89-5edc-4247-9e09-a516dd5bbf5d";
            JSONObject jsonBody = new JSONObject();
            String link ="audio_url";
            String idi = "codicePaziente";
            String idii = "audio_position";

            jsonBody.put(link, myUrl);
            jsonBody.put(idi,id);
            jsonBody.put(idii,audio_pos);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }}




















    //@Override
    public void onClick(View view) {

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