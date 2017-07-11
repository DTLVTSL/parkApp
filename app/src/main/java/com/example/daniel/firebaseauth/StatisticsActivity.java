package com.example.daniel.firebaseauth;

import java.net.URL;
import java.lang.String;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StreamDownloadTask;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Environment;
import android.widget.ListView;

import  com.google.firebase.storage.StorageTask;

import static android.R.id.list;


public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = StatisticsActivity.class.getSimpleName();
    private GraphView mGraph;
    private Button ButtonLogout;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;
    //private Uri myuri;
    private String generatedFilepath;
    //private Uri downloadUri;
    ListView listView;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;
    public String value;


    //private static final String PATH_TO_SERVER = "https://firebasestorage.googleapis.com/v0/b/parkinsonapp-7b987.appspot.com/o/statistics%2F9MhN2zJrf1P7Hs7A9PuonIixVR02%2Fteste.csv?alt=media&token=93c9fca8-0aef-4a9f-bc0a-de2f0dadb96f";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButtonLogout = (Button) findViewById(R.id.ButtonLogout);
        ButtonLogout.setOnClickListener(this);
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
        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        //
        setTitle("Mio risultato");

        FirebaseUser user = firebaseAuth.getCurrentUser();
        //getting current user
        storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("statistics").child(user.getUid()).child("teste.csv").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                generatedFilepath = uri.toString();
                Log.i(generatedFilepath,"URL link" );
            }
        });

        mGraph = (GraphView) findViewById(R.id.graph);
        Button loadTextButton = (Button)findViewById(R.id.load_file_from_server);
        loadTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
                downloadFilesTask.execute();
            }
        });
    }
    private List<String[]> readCVSFromAssetFolder(){
        List<String[]> csvLine = new ArrayList<>();
        String[] content = null;
        try {
            InputStream inputStream = getAssets().open("teste.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while((line = br.readLine()) != null){
                content = line.split(",");
                csvLine.add(content);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvLine;
    }
    private class DownloadFilesTask extends AsyncTask<URL, Void, List<String[]>> {
        protected List<String[]> doInBackground(URL... urls) {
            return downloadRemoteTextFileContent();
        }
        protected void onPostExecute(List<String[]> result) {
            if(result != null){
                createLineGraph(result);
            }
        }
    }
    private void createLineGraph(List<String[]> result){
        DataPoint[] dataPoints = new DataPoint[result.size()];
        for (int i = 0; i < result.size(); i++){
            String [] rows = result.get(i);
            Log.d(TAG, "Output " + Integer.parseInt(rows[0]) + " " + Integer.parseInt(rows[1]));
            dataPoints[i] = new DataPoint(Integer.parseInt(rows[0]), Integer.parseInt(rows[1]));
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
        mGraph.addSeries(series);
        // legend
        series.setTitle("UPDRS GRADE");
        mGraph.getLegendRenderer().setVisible(true);
        mGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference dbPrediccionff;
        dbPrediccionff = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("results");
        dbPrediccionff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                value = (String)dataSnapshot.getValue();
                list.add(value );
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createBarChartGraph(List<String[]> result){
        DataPoint[] dataPoints = new DataPoint[result.size()];
        for (int i = 0; i < result.size(); i++){
            String [] rows = result.get(i);
            Log.d(TAG, "Output " + Integer.parseInt(rows[0]) + " " + Integer.parseInt(rows[1]));
            dataPoints[i] = new DataPoint(Integer.parseInt(rows[0]), Integer.parseInt(rows[1]));
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
        mGraph.addSeries(series);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });
        series.setSpacing(50);
    }

    private List<String[]> downloadRemoteTextFileContent(){
        URL mUrl = null;
        //Uri myuri = null;
        List<String[]> csvLine = new ArrayList<>();
        String[] content = null;

        try {
            mUrl = new URL(generatedFilepath);
            // mUrl = new URL(myuri.toString());
            // mUrl = new URL("https://firebasestorage.googleapis.com/v0/b/parkinsonapp-7b987.appspot.com/o/statistics%2F9MhN2zJrf1P7Hs7A9PuonIixVR02%2Fteste.csv?alt=media&token=93c9fca8-0aef-4a9f-bc0a-de2f0dadb96f");mUrl = new URL(

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert mUrl != null;
            URLConnection connection = mUrl.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null){
                content = line.split(",");
                csvLine.add(content);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvLine;
    }

    //@Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == ButtonLogout){
            finish();
            //starting login activity
            Intent intent = new Intent(getApplicationContext(),NavigationActivity.class);
            startActivity(intent);

        }



    }
}