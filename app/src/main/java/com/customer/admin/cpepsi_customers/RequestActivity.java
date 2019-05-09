package com.customer.admin.cpepsi_customers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class RequestActivity extends AppCompatActivity {
    ImageView success;
    Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        menu = (Button)findViewById(R.id.menu);

        //***********animation Bounce
        final Animation bounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        success=(ImageView) findViewById(R.id.success);
        success.startAnimation(bounce);
        //***********************

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestActivity.this,Main_Provider.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
