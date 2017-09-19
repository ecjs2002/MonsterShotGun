package com.fireegg1991.banglemonster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    ImageView imageView;
    Animation ani;
    Intent intent;
    Timer timer=new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imageView=(ImageView) findViewById(R.id.img);
        ani=AnimationUtils.loadAnimation(this,R.anim.appear_logo);
        imageView.startAnimation(ani);
        intent=new Intent(this,MainActivity.class);



        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 3500);



    }

}
