package com.example.dinosnowrun;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DinoSprite {
    private Bitmap bitmap;
    private int x,y;
    private boolean isAlive=true;
    private boolean up = false,down=false;


    private List<Rect> frames;
    private int frameWidth;
    private int jump_y,start_y;
    private int frameHeight;
    private int currentFrame;
    private double timeForCurrentFrame;
    private double frameTime;
    private int jump_speed;
    private Paint p = new Paint();

    public DinoSprite(Bitmap b, int x, int y, Rect initialFrame){
        this.bitmap=b;
        this.x=x;
        this.jump_speed=60;
        y=y-20;

        this.y=y-(y%jump_speed);
        this.start_y=this.y;

        this.jump_y=y/12-(y/12%jump_speed)-jump_speed;

        this.frames = new ArrayList<Rect>();
        this.frames.add(initialFrame);

        this.timeForCurrentFrame = 0.0;
        this.frameTime = 0.1;
        this.currentFrame = 0;
        this.frameWidth = initialFrame.width();
        this.frameHeight = initialFrame.height();
        Log.d("YST",""+start_y+" "+jump_y);

    }

    public boolean IsDown() {
        return down;
    }

    public void SetDown(boolean down) {
        this.down = down;
    }

    public boolean IsUp() {
        return up;
    }

    public void SetUp(boolean up) {
        this.up = up;
    }


    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }


    public void addFrame (Rect frame) {
        frames.add(frame);
    }


    public void update(int ms){
        timeForCurrentFrame += ms;

        //реализация прыжка
        if ((IsUp())&&(this.y!=this.jump_y))this.y-=jump_speed;
        else {SetDown(true);SetUp(false);}
        if ((this.y!=this.start_y)&&(IsDown())){this.y+=jump_speed;}
        else {SetDown(false);Log.d("JUMP"," y "+y+"start y"+ start_y+" "+Integer.toString(262-262%40));}


        if (timeForCurrentFrame >= frameTime&&isAlive()) {
            currentFrame = (currentFrame + 1) % (frames.size()-2);
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
        }


    }
    public boolean intersect(ChristmasTreeSprite s){//проврека на столкновения с елкой

        if (getBoundingBoxRect().intersect(s.getBoundingBoxRect())) {setAlive(false);
            currentFrame=frames.size()-1;


            Log.d("DINNO",  "X dino"+x+" y dino" +y+ "s X "+s.getX()+" S y"+ s.getY()); return true;}

        return false;
    }
    public Rect getBoundingBoxRect(){
        return new Rect(this.x,this.y,this.x+frameWidth,this.y+frameHeight);
    }
    public void draw (Canvas canvas) {

        Rect destination = new Rect((int)x, (int)y, (int)(x + frameWidth), (int)(y + frameHeight));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination,  p);
    }
}
