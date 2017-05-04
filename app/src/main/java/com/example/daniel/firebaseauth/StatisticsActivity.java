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
import android.widget.Button;
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


public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = StatisticsActivity.class.getSimpleName();
    private GraphView mGraph;
    private Button ButtonLogout;
    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;
    private Uri myuri;


    private static final String PATH_TO_SERVER = "https://firebasestorage.googleapis.com/v0/b/parkinsonapp-7b987.appspot.com/o/audio%2FvaLWsTog6eeqPy83vWbzSEMVvOZ2%2Fteste.csv?alt=media&token=6db9c0a8-14a8-4180-9552-12c96a89a285";
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
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
        mGraph.addSeries(series);
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
        Uri myuri = null;
        List<String[]> csvLine = new ArrayList<>();
        String[] content = null;
        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        myuri = storageRef.child("storage").child("statistics").child(user.getUid()).child("teste.csv").getDownloadUrl().getResult();
        try {


            mUrl = new URL(myuri.toString());
            //System.out.println("a: " + mUrl);
            // mUrl = new URL("https://firebasestorage.googleapis.com/v0/b/parkinsonapp-7b987.appspot.com/o/audio%2FvaLWsTog6eeqPy83vWbzSEMVvOZ2%2Fteste.csv?alt=media&token=6db9c0a8-14a8-4180-9552-12c96a89a285");
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