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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth firebaseAuth;
    private Bundle b;
    private String name;
    private TextView profileName;
    private String uName = "";
    private UserInformation user = null;


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
        profileName.setText(userInfo.getEmail());
        //username =(TextView) headerView.findViewById(R.id.username);
        b = getIntent().getExtras();
        //profileName.setText(u.getDisplayName());
        /*if(b != null){
            name = b.getString("Name");
            //profileName.setText(name);

        }*/

    }
    /*@Override
    public void onStart() {
        super.onStart();

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dru = mDatabase.child(userInfo.getUid());

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation us1 = dataSnapshot.getValue(UserInformation.class);
                user = us1;


                //String u=UserInformation.getUserName();
                String u=us1.getUserName()+" "+us1.getUserSurname();
                uName = u;
                profileName.setText(us1.getUserName());

                //email.setText(us1.getEmail());

                //listDataChild.get(listDataHeader.get(1)).clear();
                //for(com.example.sanliu.Database.User.Group g:us1.getGroups().values())
                 //   listDataChild.get(listDataHeader.get(1)).add(g);

                //((ExpandableListAdapter)expListView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        dru.addValueEventListener(userListener);

    }*/

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
