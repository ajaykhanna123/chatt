package com.example.ajaykhanna.chatt;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolbar;
    private ViewPager mainTabPager;
    private SectionPagerAdapter mSectionPagerAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.main_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chatt");

        mAuth = FirebaseAuth.getInstance();
        //tabs
        mainTabPager=(ViewPager)findViewById(R.id.mainTabPager);
        mSectionPagerAdapter=new SectionPagerAdapter(getSupportFragmentManager());
        mainTabPager.setAdapter(mSectionPagerAdapter);
        mTabLayout=(TabLayout)findViewById(R.id.mainTabs);
        mTabLayout.setupWithViewPager(mainTabPager);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null)
        {
           sendToStart();
        }
    }

    private void sendToStart() {
        Intent startIntent=new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.main_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId()==R.id.main_logOutBtn)
         {
             FirebaseAuth.getInstance().signOut();
             sendToStart();
         }
         if(item.getItemId()==R.id.main_settingsBtn)
         {
             Intent settingsIntent=new Intent(MainActivity.this,SettingsActivity.class);
             startActivity(settingsIntent);

         }
         if(item.getItemId()==R.id.mainAllUsersBtn)
         {
             Intent usersIntent=new Intent(MainActivity.this,UsersActivity.class);
             startActivity(usersIntent);
         }
         return true;
    }

}
