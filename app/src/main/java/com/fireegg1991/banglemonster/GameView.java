package com.fireegg1991.banglemonster;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.currentThread;

/**
 * Created by alfo6-2 on 2017-06-26.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    SurfaceHolder holder;
    Context context;
    Resources res;
    int width,height;
    GameThread gameThread;
    boolean isBomb;
    int score;
    int coin;


    public void gameStop(){
        gameThread.stopThread();
    }
    public void gamePause(){
        Log.i("쓰래드",currentThread().toString());
        gameThread.pauseThread();
    }
    public void gameResume(){
        gameThread.resumeThread();
    }

    public GameView(Context context) {
        super(context);
        holder=getHolder();
        holder.addCallback(this);

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder=getHolder();
        holder.addCallback(this);
        this.context=context;
        res=getResources();

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(gameThread==null){
            this.width=width;
            this.height=height;
            gameThread=new GameThread();
            gameThread.start();
        }else {
            gameResume();
        }


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int act=event.getAction();
        int x=0,y=0;
        switch (act){
            case MotionEvent.ACTION_DOWN:
                x=(int) event.getX();
                y=(int) event.getY();
                gameThread.touchDown(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                x=(int) event.getX();
                y=(int) event.getY();
                gameThread.touchMove(x,y);
                break;
            case MotionEvent.ACTION_UP:
                x=(int) event.getX();
                y=(int) event.getY();
                gameThread.touchUp(x,y);
                break;
        }

        return true;
    }

    class GameThread extends Thread{
        Vibrator vibrator;
        ArrayList<Bullet> bullets=new ArrayList<>();
        ArrayList<Enumy> enumies=new ArrayList<>();
        ArrayList<Dust> dusts=new ArrayList<>();
        ArrayList<Item> items=new ArrayList<>();
        Bitmap[][]cHar=new Bitmap[3][4];
        Bitmap[][]enemy=new Bitmap[3][4];
        Bitmap[][]gauge=new Bitmap[2][];
        Bitmap[] imgDust=new Bitmap[6];
        Bitmap[] itemimg=new Bitmap[7];
        Bitmap imgprotect;
        Bitmap backimg;
        Bitmap imgStrong;
        Bitmap imgBomb;
        Bitmap[] imgMissile=new Bitmap[3];
        Rect recBomb;
        int bomb=3;
        int fastTime=0;
        int protectTime=0;
        int magnetTime=0;
        int strongTime=0;
        int level=5;
        Random rnd=new Random();
        Bitmap imgjoy;
        int backpos=0;
        int x,y,w,h;

        int jpx,jpy,jpr;
        Paint alphaPaint=new Paint();
        boolean isJoy=false;
        int bulletGap=3;
        int bulletTime=bulletGap;
        Player me;
        int protRad;
        int protAngle;
        Boolean isRun=true,iswait=false;
        ///////////////////////////////////////
        int skip;
        final static int FPS=45;
        int frameTime=1000/FPS;
        long sleepTime;
        long loopTime;
        long lastTime,currentTime;
        ////////////////////////////////////////
        SoundPool sp;
        int sd_chdie,sd_bomb,sd_fire,sd_coin,sd_gem,sd_protect,sd_item,sd_mondie;





        void adjustFPS() {
            currentTime=System.currentTimeMillis();
            loopTime=currentTime-lastTime;
            lastTime=currentTime;
            sleepTime=frameTime-loopTime;

            if(sleepTime>0){
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }skip=0;
            while (sleepTime<0&&skip<5){
                makeAll();
                moveAll();
                check();
                sleepTime+=frameTime;
                skip++;
            }
        }
        void checkItem(){
            if(fastTime>0){
                fastTime--;
                if(fastTime==0){
                    me.da=2;
                    bulletGap=3;
                }
            }
            if(protectTime>0){
                protectTime--;
            }
            if(magnetTime>0){
                magnetTime--;
            }
            if(strongTime>0){
                strongTime--;
            }

        }
        public void actionItem(int kind){
            switch (kind){
                case Item.COIN:
                    if(G.isSound)  sp.play(sd_coin,1,1,2,0,1);
                    coin++;
                    setTextValue();
                    break;
                case Item.GEM:
                    if(G.isSound)  sp.play(sd_gem,1,1,3,0,1);
                    G.gem++;
                    setTextValue();
                    break;
                case Item.FAST:
                    if(G.isSound)  sp.play(sd_item,1,1,3,0,1);
                    fastTime=FPS*6;
                    me.da=8;
                    bulletGap=1;
                    break;
                case Item.PROTECT:
                    if(G.isSound)  sp.play(sd_protect,1,1,4,0,1);
                    protectTime=FPS*6;
                    break;
                case Item.MAGNET:
                    if(G.isSound)  sp.play(sd_item,1,1,3,0,1);
                    magnetTime=FPS*6;
                    break;
                case Item.BOMB:
                    if(G.isSound)  sp.play(sd_item,1,1,3,0,1);
                    bomb++;
                    setTextValue();
                    break;
                case Item.STRONG:
                    if(G.isSound)  sp.play(sd_item,1,1,3,0,1);
                    strongTime=FPS*6;
                    break;

            }
        }


        @Override
        public void run() {
            Canvas canvas=null;
            createBitmaps();
            init();

            lastTime=System.currentTimeMillis();
            while (isRun){


                canvas=holder.lockCanvas();

                try {
                    synchronized (holder){


                        makeAll();

                        moveAll();

                        check();

                        adjustFPS();

                        drawAll(canvas);

                        Log.i("쓰래드",currentThread().toString());



                    }
                } finally {
                    if(canvas!=null){
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
                synchronized (this){
                if(iswait) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                }

            }
            removeResources();



        }
        public void touchDown(int x,int y){

            if(Math.pow(x-jpx,2)+Math.pow(y-jpy,2)<=Math.pow(jpr,2)){
                isJoy=true;
                me.canmove=true;
            }
            if(recBomb.contains(x,y)) {
                isBomb = true;
                if (bomb > 0) {
                    bomb--;
                    if(G.isSound)  sp.play(sd_bomb,1,1,5,0,1);
                    setTextValue();
                    for (Enumy t : enumies) {
                        if (t.wasShow) {
                            t.isDead = true;
                        }
                    }
                }
            }

        }
        public void touchMove(int x,int y){
            if(Math.pow(x-jpx,2)+Math.pow(y-jpy,2)<=Math.pow(jpr,2)){
                me.radian=Math.atan2(jpy-y,x-jpx);
            }

        }
        public void touchUp(int x,int y){
            isJoy=false;
            me.canmove=false;
            isBomb=false;

        }
        public void makeAll(){
            bulletTime--;
            if(bulletTime<=0){
                if(G.isSound)  sp.play(sd_fire,0.2f,0.2f,0,0,1);
                bullets.add(new Bullet(me.x,me.y,width,height,imgMissile,me.angle,me.kind));
                bulletTime=bulletGap;
            }
            //적군 객체 만들기
            int p= rnd.nextInt(11-level);
            if(p==0){
                enumies.add(new Enumy(width,height,me.x,me.y,enemy,gauge));
            }


        }
        public void drawAll(Canvas canvas){

            canvas.drawBitmap(backimg,backpos,0,null);
            canvas.drawBitmap(backimg,backpos-width,0,null); //백그라운드 그리기

            for(Item t: items){
                canvas.drawBitmap(t.img,t.x-t.w,t.y-t.h,null);
            }

            for(Enumy t : enumies){
                canvas.save();
                canvas.rotate(t.angle,t.x,t.y);
                canvas.drawBitmap(t.img,t.x-t.w,t.y-t.h,null);
                canvas.restore();
                if(t.kind>0) canvas.drawBitmap(t.imgG,t.x-t.w,t.y+t.h,null);
            }



            for(Bullet t: bullets){
                canvas.save();
                canvas.rotate(t.angle,t.x,t.y);
                canvas.drawBitmap(strongTime>0?imgStrong:t.bm,t.x-t.w,t.y-t.h,null);
                canvas.restore();
            }


            canvas.save();
            canvas.rotate(me.angle, me.x, me.y);
            canvas.drawBitmap(me.img,me.x-me.w,me.y-me.h,null);
            canvas.restore();

            if(protectTime>0){
                protAngle+=30;
                canvas.save();
                canvas.rotate(protAngle,me.x,me.y);
                canvas.drawBitmap(imgprotect,me.x-protRad,me.y-protRad,null);
                canvas.restore();
            }

            for(Dust t: dusts){
                for(int i=0;i<t.imgs.length;i++){
                    canvas.drawBitmap(t.imgs[i],t.x[i]-t.rad[i],t.y[i]-t.rad[i],null);
                }
            }


            alphaPaint.setAlpha(isJoy?70:0);
            canvas.drawBitmap(imgjoy,jpx-jpr,jpy-jpr,alphaPaint);
            alphaPaint.setAlpha(isBomb?70:250);
            canvas.drawBitmap(imgBomb,recBomb.left,recBomb.top,alphaPaint);


        }
        public void createBitmaps(){
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize=2;


            Bitmap img=null;
            img=BitmapFactory.decodeResource(res,R.drawable.back_01+rnd.nextInt(6));
            backimg=Bitmap.createScaledBitmap(img,width,height,true);
            img.recycle();img=null;
            int size=height/5;
            img=BitmapFactory.decodeResource(res,R.drawable.btn_bomb,options);
            imgBomb=Bitmap.createScaledBitmap(img,size,size,true);
            recBomb=new Rect(width-size,height-size,width,height);
            img.recycle();





            img=BitmapFactory.decodeResource(res,R.drawable.bullet_04,options);
            imgStrong=Bitmap.createScaledBitmap(img,height/10,height/10,true);
            img.recycle();

            img=BitmapFactory.decodeResource(res,R.drawable.effect_protect,options);
            imgprotect=Bitmap.createScaledBitmap(img,height/4,height/4,true);
            protRad=imgprotect.getWidth()/2;



            for(int i=0;i<itemimg.length;i++){
                img=BitmapFactory.decodeResource(res,R.drawable.item_0_coin+i,options);
                itemimg[i]=Bitmap.createScaledBitmap(img,height/16,height/16,true);
                img.recycle();img=null;
            }

            float[] r=new float[]{0.5f,0.7f,1.0f,1.4f,1.8f,2.2f};
            for(int i =0;i<imgDust.length;i++){
                img=BitmapFactory.decodeResource(res,R.drawable.dust,options);
                imgDust[i]=Bitmap.createScaledBitmap(img,(int)(height/9*r[i]),(int)(height/9*r[i]),true);
            }


            gauge[0]=new Bitmap[5];
            for(int i=0;i<5;i++){
                img=BitmapFactory.decodeResource(res,R.drawable.gauge_step5_01+i,options);
                gauge[0][i]=Bitmap.createScaledBitmap(img,height/9,height/36,true);
                img.recycle();
            }
            gauge[1]=new Bitmap[3];
            for(int i=0;i<3;i++){
                img=BitmapFactory.decodeResource(res,R.drawable.gauge_step3_01+i,options);
                gauge[1][i]=Bitmap.createScaledBitmap(img,height/9,height/36,true);
                img.recycle();
            }

            for(int k=0;k<3;k++){
                for(int i=0;i<3;i++){
                    img=BitmapFactory.decodeResource(res,R.drawable.enemy_a_01+i+k*3,options);
                    enemy[k][i]=Bitmap.createScaledBitmap(img,height/9,height/9,true);
                    img.recycle();img=null;
                }
                enemy[0][3]=enemy[0][1];
                enemy[1][3]=enemy[1][1];
                enemy[2][3]=enemy[2][1];
            }
            for(int i=0;i<imgMissile.length;i++){
                img=BitmapFactory.decodeResource(res,R.drawable.bullet_01+i,options);
                imgMissile[i]=Bitmap.createScaledBitmap(img,height/10,height/10,true);
                img.recycle();img=null;
            }

            for(int j=0;j<3;j++){
                for(int i=0;i<3;i++){
                    img=BitmapFactory.decodeResource(res,R.drawable.char_a_01+i+j*3,options);
                    cHar[j][i]=Bitmap.createScaledBitmap(img,height/8,height/8,true);
                    img.recycle();img=null;
                }
            }
            cHar[0][3]=cHar[0][1];
            cHar[1][3]=cHar[1][1];
            cHar[2][3]=cHar[2][1];

            img=BitmapFactory.decodeResource(res,R.drawable.img_joypad,options);
            imgjoy=Bitmap.createScaledBitmap(img,height/2,height/2,true);
            img.recycle();img=null;


            x=width/2;
            y=height/2;
            w=cHar[0][0].getWidth()/2;
            h=cHar[0][0].getHeight()/2;



        }
        public void moveAll(){


            checkItem();
            //배경움직이기
            backpos+=9 ;
            if(backpos>=width){
                backpos-=width;
            }
            for(int i=items.size()-1;i>=0;i--){
                Item t =items.get(i);
                if(magnetTime>0&&t.kind<2) t.move(me.x,me.y);
                else t.move();
                if(t.isDead) items.remove(t);
            }
            me.move();
            for(int i=dusts.size()-1;i>=0;i--){
                Dust t = dusts.get(i);
                t.move();
                if(t.isDead) dusts.remove(t);
            }

            for(int i=bullets.size()-1;i>=0;i--){
                Bullet t = bullets.get(i);
                t.move();
                if(t.isDead) bullets.remove(t);
            }
            for(int i=enumies.size()-1;i>=0;i--){
                Enumy t =enumies.get(i);
                t.move(me.x,me.y);
                if(t.isOut) enumies.remove(t);
                else if(t.isDead) {
                    if(G.isSound)  sp.play(sd_mondie,0.2f,0.2f,1,0,1);
                    score+=10*(t.kind+1);
                    setTextValue();
                    enumies.remove(t);
                    dusts.add(new Dust(imgDust,t.x,t.y));
                    items.add(new Item(width,height,itemimg,t.x,t.y));
                }
            }

        }
        public void check(){

            for(Enumy t: enumies){
                if(protectTime>0){
                    if(Math.pow(t.x-me.x,2)+Math.pow(t.y-me.y,2)<=Math.pow(t.w+protRad,2)){
                        t.isDead=true;
                    }
                }else {
                    if(Math.pow(t.x-me.x,2)+Math.pow(t.y-me.y,2)<=Math.pow(t.w+me.w,2)) {
                        t.isDead=true;
                        me.hp--;
                        if(G.isVibrate)vibrator.vibrate(500);
                        if(me.hp<=0){
                            if(G.isSound){
                                sp.play(sd_chdie,1,1,6,0,1);
                            }
                            Message msg=new Message();
                            Bundle data=new Bundle();
                            data.putInt("Score",score);
                            data.putInt("Coin",coin);
                            msg.setData(data);
                            ((GameActivity)context).handler.sendMessage(msg);
                        }
                        break;
                    }
                }

            }

            for(Bullet t: bullets){
                for(Enumy et:enumies){
                    if(Math.pow(t.x-et.x,2)+Math.pow(t.y-et.y,2)<=Math.pow(t.w+et.w,2)){
                        et.damaged(t.kind+1);
                        if(strongTime==0)  t.isDead = true;

                    }
                }
            }
            for(Item t : items){
                if(Math.pow(t.x-me.x,2)+Math.pow(t.y-me.y,2)<=Math.pow(t.w+me.w,2)){
                    score+=5;
                    setTextValue();
                    actionItem(t.kind);
                    t.isDead=true;

                }
            }


        }
        public void removeResources(){

            imgprotect.recycle();imgprotect=null;

            imgStrong.recycle();
            imgStrong=null;
            for(int i=0;i<itemimg.length;i++){
                itemimg[i].recycle();
                itemimg[i]=null;
            }


            for(int i=0;i<imgDust.length;i++){
                imgDust[i].recycle();
                imgDust[i]=null;
            }

            for(int i=0;i<imgMissile.length;i++){
                imgMissile[i].recycle();
                imgMissile[i]=null;
            }
            for(int i=0;i<gauge.length;i++){
                for(int j=0;j<gauge[i].length;j++){
                    gauge[i][j].recycle();
                    gauge[i][j]=null;

                }
            }

            backimg.recycle();
            for(int i=0;i<3;i++){
                for(int k=0;k<3;k++){
                    cHar[i][k].recycle();
                    cHar[i][k]=null;
                }
                cHar[i][0]=null;
            }
            for(int i=0;i<3;i++){
                for(int k=0;k<3;k++){
                    enemy[i][k].recycle();
                    enemy[i][k]=null;
                }
                enemy[i][0]=null;
            }
        }
        public void init(){
            me=new Player(width,height,G.kind,cHar);
            jpr=imgjoy.getWidth()/2;
            jpx=jpr;
            jpy=height-jpr;
            setTextValue();
            sp=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
            sd_chdie=sp.load(context,R.raw.ch_die,1);
            sd_bomb=sp.load(context,R.raw.explosion_bomb,1);
            sd_fire=sp.load(context,R.raw.fireball,1);
            sd_coin=sp.load(context,R.raw.get_coin,1);
            sd_gem=sp.load(context,R.raw.get_gem,1);
            sd_protect=sp.load(context,R.raw.get_invincible,1);
            sd_item=sp.load(context,R.raw.get_item,1);
            sd_mondie=sp.load(context,R.raw.mon_die,1);
            vibrator=(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        public void setTextValue(){



            post(new Runnable() {
                @Override
                public void run() {
                    GameActivity ac=((GameActivity)context);
                    String s=String.format("%07d",score);
                    ac.tvScore.setText(s);

                    s=String.format("%04d",coin);
                    ac.tvCoin.setText(s);

                    s=String.format("%04d",G.gem);
                    ac.tvGem.setText(s);

                    s=String.format("%04d",bomb);
                    ac.tvBomb.setText(s);

                    s=String.format("%07d",G.champion);
                    ac.tvChampion.setText(s);
                }
            });




        }
        public void stopThread(){
            isRun=false;
            synchronized (this){
                this.notify();
            }

        }
        public void pauseThread(){
            iswait=true;
        }
        public void resumeThread(){
            synchronized (this){
                this.notify();
            }
            iswait=false;

        }

    }

}
