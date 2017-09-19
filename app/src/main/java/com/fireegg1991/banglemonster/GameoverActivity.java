package com.fireegg1991.banglemonster;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class GameoverActivity extends AppCompatActivity {

    int coin;
    int score;
    int uScore;
    boolean isChampion=false;
    TextView tvChampion;
    TextView tvScore;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setView();
        tvChampion=(TextView)findViewById(R.id.text_champion);
        tvScore=(TextView)findViewById(R.id.text_score);
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("bundle");
        score=bundle.getInt("Score",-9999);
        coin=bundle.getInt("Coin",-9999);
        uScore=score+coin*10;
        String s=String.format("%07d",uScore);
        tvScore.setText(s);
        if(uScore>G.champion){
            G.champion=uScore;
            isChampion=true;
        }
        imageView=(ImageView)findViewById(R.id.img_champion);
        s=String.format("%07d",G.champion);
        tvChampion.setText(s);
        if(G.uri!=null){
            Uri uri=Uri.parse(G.uri);
            imageView.setImageURI(uri);
        }

    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }

    void saveData(){
        SharedPreferences pref= getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();

        editor.putInt("champion",G.champion);
        editor.putInt("gem",G.gem);
        editor.putInt("kind",G.kind);
        editor.putString("uri",G.uri);
        editor.putBoolean("isMusic",G.isMusic);
        editor.putBoolean("isSound",G.isSound);
        editor.putBoolean("isVibrate",G.isVibrate);
        editor.commit();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(resultCode== Activity.RESULT_OK) {
                    G.uri = data.getData().toString();
                    Uri uri= data.getData();
                    if (uri != null) imageView.setImageURI(uri);
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setView(){
        Glide.with(this).load(R.drawable.ui_gameover_back).into((ImageView) findViewById(R.id.img_gameover));
        Glide.with(this).load(R.drawable.ui_gameover_label_champion).into((ImageView) findViewById(R.id.img_label_champion));
        Glide.with(this).load(R.drawable.ui_gameover_label_yourscore).into((ImageView) findViewById(R.id.img_label_score));
        Glide.with(this).load(R.drawable.ui_gameover_btn_retry_c).into((ImageView) findViewById(R.id.img_retry));
        Glide.with(this).load(R.drawable.ui_gameover_btn_exit_c).into((ImageView) findViewById(R.id.img_exit));
    }
    public void clickChampion(View v){
        if(!isChampion)  return;

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
            Intent intent =new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivity(intent);
        }else {
            Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*").addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent,0);
        }

    }
    public void clickRetry(View v){
        Intent intent =new Intent(this,GameActivity.class);
        startActivity(intent);
        finish();

    }
    public void clickExit(View v){
        finish();
    }
}
