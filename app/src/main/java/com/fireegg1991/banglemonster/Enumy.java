package com.fireegg1991.banglemonster;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by alfo6-2 on 2017-06-27.
 */

public class Enumy {
    int x, y, w, h;
    int width, height;
    int kind;
    int index = 0;
    int loop = 0;
    int hp;
    double radian;
    int angle;
    int speed = w / 8;
    boolean isDead = false, isOut = false;
    boolean wasShow=false;
    Bitmap[] imgGs;
    Bitmap imgG;
    Rect sRect;

    int cx, cy;
    Bitmap img;
    Bitmap[][] imgs;
    Random rnd = new Random();

    public Enumy(int width, int height, int cx, int cy, Bitmap[][] imgs,Bitmap[][] imgGs) {

        this.width = width;
        this.height = height;


        sRect =new Rect(0,0,width,height);
        this.cx = cx;
        this.cy = cy;

        int n = rnd.nextInt(10);
        kind = n < 6 ? 0 : n < 9 ? 1 : 2;

        this.imgs = imgs;
        img = this.imgs[kind][index];

        h = img.getHeight() / 2;
        w = img.getWidth() / 2;
        hp = kind == 0 ? 1 : kind == 1 ? 5 : 3;
        speed = kind == 0 ? w / 10 : kind == 1 ? w / 12 : w / 14;

//        x = rnd.nextInt(width);
//        y = rnd.nextInt(height);
//        x = x >= width / 2 ? x + (width / 2 + w) : x - (width / 2 - w);
//        y = y >= height / 2 ? y + (height / 2 + h) : y - (height / 2 - h);
        int a = rnd.nextInt(360);
        x = (int) (width / 2 + Math.cos(Math.toRadians(a)) * width );
        y = (int) (height / 2 - Math.sin(Math.toRadians(a)) * width );

        calAngle(cx, cy);


        if(kind>0){
            this.imgGs=imgGs[kind-1];
            imgG=this.imgGs[0];
        }



    }
    void calAngle(int cx,int cy){
        radian = Math.atan2(y - cy, cx - x);
        angle=(int)(270-Math.toDegrees(radian));
    }
    void damaged(int n){
        hp-=n;
        if(hp<=0) {
            isDead=true;
            return;
        }

        imgG=imgGs[imgGs.length-hp];
    }

    public void move(int cx,int cy) {

        if(kind==2) calAngle(cx, cy);
        loop++;
        if (loop % 5 == 0) {
            index++;
            if (index == 4) index = 0;
            img = imgs[kind][index];
        }


        x = (int) (x + Math.cos(radian) * speed);
        y = (int) (y - Math.sin(radian) * speed);

        if(sRect.contains(x,y)) wasShow=true;
        if(wasShow) {
            if (x < -w || x > width + w || y < -h || y > height + h) isOut = true;
        }



        //만약 내가 받은 랜덤 좌표값이 width/2보다 크면 width/2를 더한값을 가지고
        //작으면 width/2 뺀값을가진다   추가적을로 캐릭터의 w만큼 더뺀다


    }
}
