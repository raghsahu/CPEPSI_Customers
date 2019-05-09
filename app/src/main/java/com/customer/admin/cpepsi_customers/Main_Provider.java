package com.customer.admin.cpepsi_customers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer.admin.cpepsi_customers.Fragments.Free_Services;
import com.customer.admin.cpepsi_customers.Fragments.Non_Professional_Services;
import com.customer.admin.cpepsi_customers.Fragments.Professional_Services;
import com.customer.admin.cpepsi_customers.util.AppPreference;
import com.customer.admin.cpepsi_customers.util.SessionManager;


public class Main_Provider extends AppCompatActivity {

    private TextView mTextMessage;
    android.support.v4.widget.NestedScrollView scro_pro;
    Button topup;
    Toolbar toolbarHome;
    CardView card1,card2,card3;
    LinearLayout profService,freeService,techService;
    SessionManager manager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent to_home = new Intent(Main_Provider.this, Main_Provider.class);
                    startActivity(to_home);
                    finish();
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.free_services:
                    Free_Services free_services = new Free_Services();
                    Load_Free_Services(free_services);
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.non_professional:
                    Non_Professional_Services non_professional_services = new Non_Professional_Services();
                    Load_Non_Professional_Services(non_professional_services);
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.professional_tools:
                    Professional_Services professional_services = new Professional_Services();
                    LoadProfessional_Services(professional_services);
                    // mTextMessage.setText(R.string.title_notifications);
                    return true;

            }
            return false;
        }
    };

    private void Load_Free_Services(Free_Services free_services) {
//        scro_pro.setVisibility(View.GONE);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        ExampleFragments fragment = new ExampleFragments();
        fragmentTransaction.replace(R.id.provider_frame, new Free_Services());
        fragmentTransaction.commit();
    }

    private void Load_Non_Professional_Services(Non_Professional_Services non_professional_services) {
//        scro_pro.setVisibility(View.GONE);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        ExampleFragments fragment = new ExampleFragments();
        fragmentTransaction.replace(R.id.provider_frame, new Non_Professional_Services());
        fragmentTransaction.commit();
    }

    private void LoadProfessional_Services(Professional_Services professional_services) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        ExampleFragments fragment = new ExampleFragments();
        fragmentTransaction.replace(R.id.provider_frame, new Professional_Services());
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__provider);

        if (ActivityCompat.checkSelfPermission(Main_Provider.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(Main_Provider.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {

        }
        manager = new SessionManager(this);

//         toolbarHome = (Toolbar) findViewById(R.id.toolbarHome);
//        setSupportActionBar(toolbarHome);

       // scro_pro = (android.support.v4.widget.NestedScrollView) findViewById(R.id.scro_pro);
   //     mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //   topup = (Button)findViewById(R.id.topup);

        final Animation right_to_left = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_to_left);
    //    final Animation left_to_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_to_right);

        profService = (LinearLayout)findViewById(R.id.profService);
        profService.startAnimation(right_to_left);

        freeService = (LinearLayout)findViewById(R.id.freeService);
        freeService.startAnimation(right_to_left);

        techService = (LinearLayout)findViewById(R.id.techService);
        techService.startAnimation(right_to_left);

        card1 = (CardView)findViewById(R.id.card1);
        card2 = (CardView)findViewById(R.id.card2);
        card3 = (CardView)findViewById(R.id.card3);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                Professional_Services fragment = new Professional_Services();
                fm.beginTransaction().add(R.id.provider_frame,fragment).commit();
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Free_Services fragment = new Free_Services();
                fm.beginTransaction().add(R.id.provider_frame,fragment).commit();
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Non_Professional_Services fragment = new Non_Professional_Services();
                fm.beginTransaction().add(R.id.provider_frame,fragment).commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        MenuItem item1 =menu.findItem(R.id.action_prof);
//        loginout = menu.
        if (manager.isLoggedIn()) {
            item.setTitle("Logout");
            item1.setVisible(true);
        }else{
            item.setTitle("Login");
            item1.setVisible(false);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomePrev/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            Intent intent = new Intent(Main_Provider.this, Notification.class);
            startActivity(intent);
            //finish();
            return true;
//        }
// else if (id==R.id.action_feedback){
//            Intent intent = new Intent(Main_Provider.this, FeedbackForm.class);
//            startActivity(intent);
           // finish();
        } else if (id == R.id.action_settings) {
            manager.logoutUser();
            manager.setAfterName(null);
             AppPreference.setAfterId(getApplicationContext(),"null");
            Intent intent = new Intent(Main_Provider.this, Login_Constomer.class);
            startActivity(intent);
            finish();
            return true;
        }else if (id == R.id.action_prof){
            Intent intent = new Intent(Main_Provider.this, ProfileActivity.class);
            startActivity(intent);
          return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
