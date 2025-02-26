package com.fireegg1991.banglemonster;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by alfo6-2 on 2017-06-28.
 */

public class Dust {
    Bitmap[] imgs;
    int[] x=new int[6];
    int[] y=new int[6];
    int[] rad=new int[6];
    int[] dx=new int[6];
    int[] dy=new int[6];

    boolean isDead=false;
    int life=15;
    Random rnd=new Random();

    public Dust(Bitmap[] imgs,int ex,int ey) {
        this.imgs = imgs;
        for(int i=0;i<6;i++){
            x[i]=ex;
            y[i]=ey;
            rad[i]=imgs[i].getWidth()/2;
            int k=rnd.nextBoolean()?1:-1;
            dx[i]=(rad[0]/6)*k;
            k=rnd.nextBoolean()?1:-1;
            dy[i]=(rad[0]/6)*k;
        }
    }

    void move(){
        for(int i=0;i<6;i++){
            x[i]+=dx[i];
            y[i]+=dy[i];
        }
        life--;
        if(life<=0){
            isDead=true;
        }
    }
}
