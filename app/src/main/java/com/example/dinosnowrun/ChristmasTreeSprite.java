package com.example.dinosnowrun;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class ChristmasTreeSprite {
    private Bitmap bitmap;
    private int x, y, Vx,viewWidth;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private boolean isActive=true;
    Paint paint = new Paint();

    public ChristmasTreeSprite(Bitmap b, int x, int y, int Vx,int viewW) {
        this.bitmap = b;
        this.x = x;
        this.y = y;
        this.Vx = Vx;
        this.viewWidth=viewW;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void update() {
        x -= Vx;
        if (x<-200){x= viewWidth+bitmap.getWidth();this.isActive=false;
            Log.d("UPDATE","Its update");}

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }
}
