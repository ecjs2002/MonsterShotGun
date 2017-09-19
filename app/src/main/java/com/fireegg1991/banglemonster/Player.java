package com.fireegg1991.banglemonster;

import android.graphics.Bitmap;

/**
 * Created by alfo6-2 on 2017-06-27.
 */

public class Player {
    Bitmap img;
    boolean canmove =false;
    int x,y,w,h;
    int width,height;
    int angle,da=2;
    int kind;
    int index;
    Bitmap[][] imgs;
    int loop=0;
    int hp=3;
    int speed;
    double radian;

    public Player(int width, int height, int kind, Bitmap[][] imgs) {
        this.width = width;
        this.height = height;
        this.kind = kind;
        this.imgs = imgs;

        img=imgs[kind][index];

        w=img.getWidth()/2;
        h=img.getHeight()/2;

        x=width/2;
        y=height/2;
        speed=w/9;
    }

    void move(){
        //날개짓
        loop++;
        if(loop%5==0){
            index++;
            if(index>3)index=0;
            img=imgs[kind][index];
        }
        //회전
        angle+=da;
        //조이패드
        if(canmove){
            x=(int)(x+Math.cos(radian)*speed);
            y=(int)(y-Math.sin(radian)*speed);

            if(x<w) x=w;
            if(x>width-w) x=width-w;
            if(y<h)y=h;
            if(y>height-h)y=height-h;

        }

    }
}
