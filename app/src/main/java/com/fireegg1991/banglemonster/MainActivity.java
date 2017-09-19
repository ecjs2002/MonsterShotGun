package com.fireegg1991.banglemonster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView startImg,exitImg;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startImg=(ImageView) findViewById(R.id.gamestart_img);
        startImg.setTag("start");
        exitImg=(ImageView)findViewById(R.id.exit_img);
        exitImg.setTag("exit");
        loadData();
        mp=MediaPlayer.create(this,R.raw.dragon_flight);
        mp.setLooping(true);
    }

    @Override
    protected void onResume() {
        if(mp!=null&&!mp.isPlaying()) {
            if(G.isMusic) mp.setVolume(0.5f, 0.5f);
            else mp.setVolume(0,0);
            mp.start();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(mp!=null&&mp.isPlaying()){
            mp.pause();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(mp!=null){
            mp.stop();
        }
        mp.release();
        mp=null;
        super.onDestroy();
    }

    public void loadData(){
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        G.champion=pref.getInt("champion",0);
        G.gem=pref.getInt("gem",0);
        G.kind=pref.getInt("kind",0);
        G.uri=pref.getString("uri",null);
        G.isMusic=pref.getBoolean("isMusic",true);
        G.isSound=pref.getBoolean("isSound",true);
        G.isVibrate=pref.getBoolean("isVibrate",true);
    }
    public void clickstart(View v){

        Intent intent =new Intent(this,GameActivity.class);
        overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
        startActivity(intent);
    }
    public void clickexit(View v){

        finish();
    }


}
