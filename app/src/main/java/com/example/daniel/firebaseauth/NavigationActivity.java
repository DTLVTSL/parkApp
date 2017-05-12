package com.example.daniel.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import java.lang.String;

import com.bumptech.glide.Glide;
import android.graphics.Bitmap;
import android.graphics.Paint;
import com.bumptech.glide.load.Transformation;
import android.graphics.Canvas;
import android.graphics.BitmapShader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnSuccessListener;


public class NavigationActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;
    private Bundle b;
    private String name;
    private TextView profileName;
    private String uName = "";
    private UserInformation user = null;
    private ImageView photoProfile;
    private String url;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Menu");
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser userInfo = firebaseAuth.getCurrentUser();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        profileName = (TextView) headerView.findViewById(R.id.nameProfile);
        photoProfile = (ImageView) headerView.findViewById(R.id.photoProfile);
        profileName.setText(userInfo.getEmail());
        //username =(TextView) headerView.findViewById(R.id.username);
        b = getIntent().getExtras();
        storageRef = FirebaseStorage.getInstance().getReference();
        //url = "https://firebasestorage.googleapis.com/v0/b/parkinsonapp-7b987.appspot.com/o/photosProfile%2F9MhN2zJrf1P7Hs7A9PuonIixVR02%2F142212?alt=media&token=5e905df7-5ec2-468f-abd1-2de66dfc4fed";
        StorageReference newStorageRef = storageRef.child("photosProfile").child(userInfo.getUid()).child("imagineProfile.jpg");
        Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(newStorageRef).bitmapTransform(new CropCircleTransformation(context)).into(photoProfile);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent myIntent = new Intent(NavigationActivity.this, RecordActivity.class);
                startActivity(myIntent);
            }
        });
        /*storageRef.child("photosProfile").child(user.getUserId()).child("142212.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("Main", "File uri: " + uri.toString());
            }
        });/*

        //profileName.setText(u.getDisplayName());
        /*if(b != null){
            name = b.getString("Name");
            //profileName.setText(name);

        }*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void displaySelectedScreen(int id ){
        //Fragment fragment = null;
        //email.setText(us1.getEmail());

        //listDataChild.get(listDataHeader.get(1)).clear();
        Intent intent = null;

        switch (id){
            case R.id.nav_profile:
                Intent i = new Intent(NavigationActivity.this,PerfilActivity.class);
                startActivity(i);
                break;
            case R.id.nav_test:
                Intent j = new Intent(NavigationActivity.this,RecordActivity.class);
                startActivity(j);
                break;
            case R.id.nav_statistics:
                Intent k = new Intent(NavigationActivity.this,StatisticsActivity.class);
                startActivity(k);
                break;
            case R.id.nav_tools:
                break;
            case R.id.nav_share:
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody =  "Scrivi qua il tuo messagio";
                String shareSub = "Scrivi il objecto del messagio";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(myIntent,"Condividendo"));
                break;
            case R.id.nav_send:
                break;
            case R.id.nav_logout:
                //logging out the user
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        //if (fragment != null){
        //    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //    ft.replace(R.id.content_frame, fragment);
        //    ft.commit();

       // }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id );
        return true;
    }
}
