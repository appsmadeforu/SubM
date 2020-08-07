package com.example.sofiy.subm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sofiy.subm.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private TextView userID;
    private TextView useremail;
    private DrawerLayout drawer;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View navHeaderView = navigationView.getHeaderView(0);
        userID = (TextView) navHeaderView.findViewById(R.id.userId);
        useremail = (TextView) navHeaderView.findViewById(R.id.userEmail);



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = mDatabase.getReference(mAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users userdetail = dataSnapshot.getValue(Users.class);
                useremail.setText(userdetail.getEmail());
                userID.setText(userdetail.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Home.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_Dashboard);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(Home.this, Login.class));
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_Dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
                break;
            case R.id.nav_Lent:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AssignmentsLentFragment()).commit();
                break;
            case R.id.nav_change_subject:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangeSubjectFragment()).commit();
                break;
            case R.id.nav_convert_to_pdf:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConvertToPdfFragment()).commit();
                break;
            case R.id.nav_Deadline:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DeadlinesFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_Logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, Login.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
