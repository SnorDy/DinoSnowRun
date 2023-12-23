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
        this.y=y-(y%jump_speed);
        this.start_y=this.y;

        this.jump_y=y/4-(y/4%jump_speed);

        this.frames = new ArrayList<Rect>();
        this.frames.add(initialFrame);
        this.timeForCurrentFrame = 0.0;
        this.frameTime = 12;
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int getCurrentFrame() {
        return currentFrame;
    }
    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame%frames.size();
    }
    public double getFrameTime() {
        return frameTime;
    }
    public void setFrameTime(double frameTime) {
        this.frameTime = Math.abs(frameTime);
    }
    public double getTimeForCurrentFrame() {
        return timeForCurrentFrame;
    }
    public void setTimeForCurrentFrame(double timeForCurrentFrame) {
        this.timeForCurrentFrame = Math.abs(timeForCurrentFrame);
    }
    public int getFramesCount () {
        return frames.size();
    }
    public void addFrame (Rect frame) {
        frames.add(frame);
    }

    public void update(int ms){
        timeForCurrentFrame += ms+100;
        //реализация прыжка
        if ((IsUp())&&(this.y!=this.jump_y))this.y-=jump_speed;
        else {SetDown(true);SetUp(false);}
        if ((this.y!=this.start_y)&&(IsDown())){this.y+=jump_speed;}
        else {SetDown(false);Log.d("JUMP"," y "+y+"start y"+ start_y+" "+Integer.toString(262-262%40));}


        if (timeForCurrentFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % (frames.size()-2);
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
        }


    }
    public void draw (Canvas canvas) {

        Rect destination = new Rect((int)x, (int)y, (int)(x + frameWidth), (int)(y + frameHeight));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination,  p);
    }
}
