package com.example.dinosnowrun;
import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

public class GameView extends View {
    private int viewWidth, viewHeight;
    private double points;
    private int speed=70;
    private DinoSprite Dino;
    private Bitmap background_bitmap,tree_bitmap,dino_bitmap;
    private int background_x = 0;
    private final int timerInterval = 30;
    private ChristmasTreeSprite [] trees=new  ChristmasTreeSprite[2];
    Paint paint = new Paint();


    public GameView(Context context) {
        super(context);
        //утсановка шрифта
        Typeface tf= ResourcesCompat.getFont(getContext(),R.font.pixel_font);
        paint.setTextSize(120);
        paint.setColor(Color.parseColor("#666666"));
        paint.setTypeface(tf);


        Timer t = new Timer();
        t.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        //создание генератора рнадом для генерации координат елок
        Random random = new Random();
        //получение изображений заднего фона, елок и дино
        background_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.background2), viewWidth +50, viewHeight - viewHeight / 3, false);
        tree_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.tree), viewWidth/5, viewHeight/3, false);

        dino_bitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.dino), viewWidth/2+viewWidth/42, viewHeight/3, false);
        int dino_w =dino_bitmap.getWidth()/6;//деление одной картнки на кадры
        Rect firstFrame = new Rect(0,0,dino_w,dino_bitmap.getHeight());//первый кадр с началом в 0 0
        Dino=new DinoSprite(dino_bitmap,40,viewHeight - viewHeight / 3-viewHeight/5,firstFrame);
        //добавление прямоугольников в массив
        for(int i=0;i<6;i++){Dino.addFrame(new Rect(i*dino_w,0,i*dino_w+dino_w,dino_bitmap.getHeight()));}

        for(int i=0;i<2;i++){
            trees[i]=new ChristmasTreeSprite(tree_bitmap,random.nextInt(viewWidth/2)+viewWidth*(2+(i)),viewHeight - viewHeight / 3-viewHeight/5,this.speed,viewWidth);
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(background_bitmap,-background_x,viewHeight - viewHeight / 3,paint);
        canvas.drawBitmap(background_bitmap,-background_x+viewWidth+50,viewHeight - viewHeight / 3,paint);


        canvas.drawText((int)points+"",viewWidth-150,120,paint);
        Random random = new Random();

        for(int i=0;i<2;i++){
                if (!(trees[i].isActive()))trees[i]=new ChristmasTreeSprite(tree_bitmap,random.nextInt(viewWidth/2)+viewWidth*(2+(i)),viewHeight - viewHeight / 3-viewHeight/5,this.speed,viewWidth);
                else{trees[i].draw(canvas);Log.d("DRAW", "its must be draw");}
        }
        Dino.draw(canvas);
    }

    protected void update() {
        for(ChristmasTreeSprite tree:trees){
            Dino.intersect(tree);
            Log.d("RECT",Dino.getBoundingBoxRect()+" " + tree.getBoundingBoxRect()+"");
        }
        if (!Dino.isAlive()){this.speed=0;}
        else points+=0.2;
        //проверка на то, что дино живой
        if (Dino.isAlive()){
        background_x = (background_x + this.speed) % ( viewWidth+50);
        for(ChristmasTreeSprite tree:trees){
            tree.update();

        }}
        Dino.update(timerInterval);

        invalidate();
        Log.d("LEN", viewHeight + " " + viewWidth);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int eventAction=event.getAction();
        if (eventAction== MotionEvent.ACTION_DOWN&&!Dino.IsDown()&&!Dino.IsUp()){
            //прыжок
            Dino.SetUp(true);
        }
        return true;
    }

    public class Timer extends CountDownTimer {
        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            update();

            millisUntilFinished = millisUntilFinished / 10000;
        }

        public void onFinish() {
        }

    }

}
