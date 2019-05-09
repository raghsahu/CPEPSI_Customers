package com.customer.admin.cpepsi_customers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.customer.admin.cpepsi_customers.Fragments.Professional_Services;


public class MainActivity extends AppCompatActivity {
    //    private TextView mTextMessage;
    Button topup;
    ImageView ride;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:

//                    mTextMessage.setText(R.string.title_home);
                    return true;
//                case R.id.non_professionak:
////                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
                case R.id.professional_tools:
                    fragment = new Professional_Services();
                    LoadFragment(fragment);
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.non_professional:
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
            }
            return false;
        }
    };

    private void LoadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_provider, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        topup = (Button) findViewById(R.id.topup);
        ride = (ImageView) findViewById(R.id.ride);
        ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, After_service.class);
                startActivity(intent);
            }
        });


    }
}
