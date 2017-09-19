package com.fireegg1991.banglemonster;

import android.graphics.Bitmap;

/**
 * Created by alfo6-2 on 2017-06-26.
 */

public class Bullet {
    int x,y,h,w;
    int width,height;
    int angle;
    Bitmap bm;
    boolean isDead=false;
    int kind;
    int speed;
    double radian;

    //999





    public Bullet(int x, int y, int width, int height,Bitmap[] imgMissile,int angle,int kind) {
        this.x = x;
        this.y = y;
        this.kind=kind;
        this.angle=angle;
        this.width = width;
        this.height = height;
        bm=imgMissile[kind];
        w=bm.getWidth()/2;
        h=bm.getHeight()/2;
        speed=w/5;
        radian=Math.toRadians(270-angle);
    }
    void move(){
        x=(int)(x+Math.cos(radian)*speed);
        y=(int)(y-Math.sin(radian)*speed);
        if(x<-w||x>width+w || y<-h||y>height+h) isDead=true;

    }



}
