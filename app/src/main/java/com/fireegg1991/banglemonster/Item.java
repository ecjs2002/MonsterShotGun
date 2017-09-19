package com.fireegg1991.banglemonster;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by alfo6-2 on 2017-06-29.
 */

public class Item {
    Bitmap img;
    int x, y, w, h, width, height;
    boolean isDead = false;
    Random rnd = new Random();
    int dx, dy;
    Rect rect;
    int kind;
    int life = 10 * GameView.GameThread.FPS;
    final static int COIN = 0;
    final static int GEM = 1;
    final static int FAST = 2;
    final static int PROTECT = 3;
    final static int MAGNET = 4;
    final static int BOMB = 5;
    final static int STRONG = 6;


    public Item(int width, int height, Bitmap[] imgs, int ex, int ey) {
        this.width = width;
        this.height = height;
        int t = rnd.nextInt(100);
        x = ex;
        y = ey;
        kind = t < 70 ? 0 : t < 71 ? 1 : t < 79 ? 2 : t < 84 ? 3 : t < 92 ? 4 : t < 95 ? 5 : 6;
        img = imgs[kind];
        w=img.getWidth()/2;
        h=img.getHeight()/2;
        int k;
        k=rnd.nextBoolean()?1:-1;
        dx=(w/6)*k;
        k=rnd.nextBoolean()?1:-1;
        dy=(w/6)*k;
        rect=new Rect(0,0,width,height);

    }
    public void move(){
        x+=dx;
        y+=dy;
        if(life>0){
            if(x<=w){
                dx=-dx;
                x=w;
            }
            if(x>=width-w){
                dx=-dx;
                x=width-w;
            }
            if(y<=h){
                dy=-dy;
                y=h;
            }
            if(y>height-h){
                dy=-dy;
                y=height-h;
            }else {
                if(!rect.contains(x,y)) isDead=true;

            }

        }

        life--;
        if (life<=0)isDead=true;

    }
    public void move(int chx,int chy){
        double radin=Math.atan2(y-chy,chx-x);
        x=(int)(x+Math.cos(radin)*w/2);//바꾸기 나누기로
        y=(int)(y-Math.sin(radin)*w/2);

    }
}
