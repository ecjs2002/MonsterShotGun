package com.fireegg1991.banglemonster;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

public class GameActivity extends AppCompatActivity {

    ToggleButton toggle_music,toggle_sound,toggle_vibrate;
    GameView gameView;
    TextView tvScore;
    TextView tvCoin;
    TextView tvGem;
    TextView tvChampion;
    TextView tvBomb;
    MediaPlayer mp;
    View dialog=null;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            gameView.gameStop();
            Intent intent=new Intent(GameActivity.this,GameoverActivity.class);
            intent.putExtra("bundle",msg.getData());
            startActivity(intent);
            finish();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        gameView=(GameView)findViewById(R.id.gameview);
        tvScore=(TextView)findViewById(R.id.tv_score);
        tvCoin=(TextView)findViewById(R.id.tv_coin);
        tvGem=(TextView)findViewById(R.id.tv_gem);
        tvChampion=(TextView)findViewById(R.id.tv_champion);
        tvBomb=(TextView)findViewById(R.id.tv_bomb);

        setView();
        mp=MediaPlayer.create(this,R.raw.my_friend_dragon);
        mp.setLooping(true);

        toggle_music=(ToggleButton)findViewById(R.id.toggle_music);
        toggle_sound=(ToggleButton)findViewById(R.id.toggle_sound);
        toggle_vibrate=(ToggleButton)findViewById(R.id.toggle_vibrate);

        toggle_music.setOnCheckedChangeListener(listener);
        toggle_sound.setOnCheckedChangeListener(listener);
        toggle_vibrate.setOnCheckedChangeListener(listener);

        toggle_music.setChecked(G.isMusic);
        toggle_vibrate.setChecked(G.isVibrate);
        toggle_sound.setChecked(G.isSound);

    }
    CompoundButton.OnCheckedChangeListener listener=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.toggle_music:
                    G.isMusic=!G.isMusic;
                    buttonView.setChecked(G.isMusic);
                    if(G.isMusic) mp.setVolume(0.7f,0.7f);
                    else mp.setVolume(0,0);
                    break;
                case R.id.toggle_sound:
                    G.isSound=!G.isSound;
                    buttonView.setChecked(G.isSound);
                    break;
                case R.id.toggle_vibrate:
                    G.isVibrate=!G.isVibrate;
                    buttonView.setChecked(G.isVibrate);
                    break;
            }


        }
    };

    @Override
    protected void onResume() {
        if(mp!=null&&!mp.isPlaying()){
            if(G.isMusic)mp.setVolume(0.5f,0.5f);
        }else mp.setVolume(0,0);
        mp.start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(mp!=null){
            mp.stop();
            mp.release();
            mp=null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        gameView.gamePause();
        if(mp!=null&mp.isPlaying()){
            mp.pause();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    public void clickPause(View v){

        showPauseDialog();

    }
    public void clickQuit(View v){
        showQuitDialog();
    }
    public void clickShop(View v){
        showMyDialog(R.id.dialog_shop);
    }
    public void clickSetting(View v){
        toggle_music.setChecked(G.isMusic);
        toggle_vibrate.setChecked(G.isVibrate);
        toggle_sound.setChecked(G.isSound);
        showMyDialog(R.id.dialog_setting);
    }
    void showMyDialog(int Viewid){
        if(dialog!=null){return;}
        gameView.gamePause();
        dialog=findViewById(Viewid);
        dialog.setVisibility(View.VISIBLE);
        Animation ani=AnimationUtils.loadAnimation(this,R.anim.appear_dialog);
        dialog.setAnimation(ani);
    }

    @Override
    public void onBackPressed() {
        showQuitDialog();

    }
    public void showQuitDialog(){
        if(dialog!=null) return;
        gameView.gamePause();
        dialog=findViewById(R.id.layout_dialog);
        dialog.setVisibility(View.VISIBLE);
        Animation ani= AnimationUtils.loadAnimation(this,R.anim.appear_quit_dialog);
        dialog.setAnimation(ani);
    }
    public void hide(){
        Animation ani=AnimationUtils.loadAnimation(this,R.anim.disappear_dialog);
        dialog.startAnimation(ani);
        dialog.setVisibility(View.GONE);
        dialog=null;
        gameView.gameResume();



    }
    public void clickbtn(View v){

        switch (v.getId()){
            case R.id.iv_quit_ok:
                gameView.gameStop();
                finish();
                break;
            case R.id.iv_quit_cancel:
                dialog.setVisibility(View.GONE);
                dialog=null;
                gameView.gameResume();
                break;
            case R.id.resume:
                Animation ani=AnimationUtils.loadAnimation(this,R.anim.disappear_dialog_pause);
                dialog.setAnimation(ani);
                dialog.setVisibility(View.GONE);
                dialog=null;
                gameView.gameResume();
                break;
            case R.id.shop_out:
                hide();
                break;
            case R.id.iv_shop:
                hide();
                break;
            case R.id.out_setting:
                dialog.setVisibility(View.GONE);
                dialog=null;
                gameView.gameResume();
                break;
        }
    }

    public void showPauseDialog(){
        if(dialog!=null) return;
        gameView.gamePause();
        dialog=findViewById(R.id.dialog_pause);
        dialog.setVisibility(View.VISIBLE);
        Animation ani=AnimationUtils.loadAnimation(this,R.anim.appear_dialog_pause);
        dialog.setAnimation(ani);
    }
    public void setView(){

        Glide.with(this).load(R.drawable.dialog_quit).into((ImageView) findViewById(R.id.iv_quit));
        Glide.with(this).load(R.drawable.select_ok).into((ImageView)findViewById(R.id.iv_quit_ok));
        Glide.with(this).load(R.drawable.select_cancel).into((ImageView)findViewById(R.id.iv_quit_cancel));

        Glide.with(this).load(R.drawable.bg_pause).into((ImageView)findViewById(R.id.bbbb));
        Glide.with(this).load(R.drawable.btn_play_n).into((ImageView)findViewById(R.id.resume));

        Glide.with(this).load(R.drawable.dialog_shop).into((ImageView)findViewById(R.id.cccc));
        Glide.with(this).load(R.drawable.check).into((ImageView)findViewById(R.id.shop_out));

        Glide.with(this).load(R.drawable.ui_setting_label_music).into((ImageView)findViewById(R.id.setting_music));
        Glide.with(this).load(R.drawable.ui_setting_label_sound).into((ImageView)findViewById(R.id.setting_sound));
        Glide.with(this).load(R.drawable.ui_setting_label_vibrate).into((ImageView)findViewById(R.id.setting_vibrate));


    }
}
