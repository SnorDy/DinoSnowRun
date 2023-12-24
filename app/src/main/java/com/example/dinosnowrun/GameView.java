package com.example.dinosnowrun;

import java.util.Random;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.VolumeShaper;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.core.content.res.ResourcesCompat;

public class GameView extends View {
    private int viewWidth, viewHeight;
    private double points;
    private int speed = 70,restart_width,restart_height,restart_x,restart_y;
    private DinoSprite Dino;
    private Bitmap background_bitmap, tree_bitmap, dino_bitmap,restart_bitmap;
    private int background_x = 0;
    private int  jump_length=1200;//длина прыжка для контроля спавна елок
    private final int timerInterval = 30;
    private ChristmasTreeSprite[] trees = new ChristmasTreeSprite[2];
    Paint paint = new Paint();


    public GameView(Context context) {
        super(context);
        //утсановка шрифта
        Typeface tf = ResourcesCompat.getFont(getContext(), R.font.pixel_font);
        paint.setTextSize(120);
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
                R.drawable.background), viewWidth + 50, viewHeight - viewHeight / 3, false);
        tree_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.tree1), viewWidth / 14, viewHeight / 4, false);

        dino_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.dino3), viewWidth / 2 + viewWidth / 40, viewHeight / 3, false);
        int dino_w = dino_bitmap.getWidth() / 6;//деление одной картнки на кадры
        Rect firstFrame = new Rect(0, 0, dino_w, dino_bitmap.getHeight());//первый кадр с началом в 0 0
        Dino = new DinoSprite(dino_bitmap, 40, viewHeight - viewHeight / 3 - viewHeight / 7, firstFrame);
        //добавление прямоугольников в массив
        for (int i = 0; i < 6; i++) {
            Dino.addFrame(new Rect(i * dino_w, 0, i * dino_w + dino_w, dino_bitmap.getHeight()));
        }
        //добавление елок в массив
        GenerateTrees();
        //вычисление размеров и положения кнопки рестарт
        restart_width=viewWidth/11;
        restart_height=viewHeight/5;
        restart_x=viewWidth/2-restart_width/2;
        restart_y=viewHeight/2-restart_height/2;
        //проверка на ночной режим и изменение цвета текста и изображенмия кнопки рестарт в соотвветствии с темой
        int currentNightMode = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                paint.setColor(R.style.Theme_DinoSnowRun);
                restart_bitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.restart_light),restart_width,restart_height,false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                paint.setColor(getResources().getColor(R.color.dark_theme_color));
                restart_bitmap=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.restart_dark),restart_width,restart_height,false);break;

        }


    }
    //функция для начальной генерации елочек
    public void GenerateTrees(){
        int last_coords = 0;
        Random random=new Random();
        for (int i = 0; i < 2; i++) {
            int coords = random.nextInt(viewWidth/2) + last_coords+jump_length+viewWidth;
            trees[i] = new ChristmasTreeSprite(tree_bitmap,coords , viewHeight - viewHeight / 2 + viewHeight / 17, this.speed, viewWidth);
            last_coords=coords;
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //отрисовка зднего фона (двумя частями, так как он двигается) чтобы не оставалось пустого пространства
        canvas.drawBitmap(background_bitmap, -background_x, viewHeight - viewHeight / 3, paint);
        canvas.drawBitmap(background_bitmap, -background_x + viewWidth + 50, viewHeight - viewHeight / 3, paint);


        canvas.drawText((int) points + "", viewWidth - 150, 120, paint);
        Random random = new Random();
        //если елка за пределами экрана,то генерируем новую
        int last_coords;
        for (int i = 0; i < 2; i++) {
            if (!(trees[i].isActive())){//создаем новую елку если она за пределами экрана, т е не активна
                if (i==0)last_coords=trees[1].getX();
                else last_coords=trees[0].getX();
                int coords = random.nextInt(viewWidth / 2) + last_coords+jump_length;


                trees[i] = new ChristmasTreeSprite(tree_bitmap,coords , viewHeight - viewHeight / 2 + viewHeight / 17, this.speed, viewWidth);}
            else {
                trees[i].draw(canvas);
                Log.d("DRAW", "its must be draw");
            }
        }
        Dino.draw(canvas);
        //проверка на конец игры
        if (!Dino.isAlive()) {
            canvas.drawText("GAME OVER", viewWidth / 2 - 375, (int) viewHeight / 4, paint);
            canvas.drawBitmap(restart_bitmap,restart_x,restart_y,paint);

        }
    }

    protected void update() {
        //проверка столкновений с елками
        for (ChristmasTreeSprite tree : trees) {
            Dino.intersect(tree);
            Log.d("RECT", Dino.getBoundingBoxRect() + " " + tree.getBoundingBoxRect() + "");
        }
        //если врезался, обнуляем скорость движения
        if (!Dino.isAlive()) {
            this.speed = 0;
        } else points += 0.2;
        //проверка на то, что дино живой
        if (Dino.isAlive()) {
            background_x = (background_x + this.speed) % (viewWidth + 50);
            for (ChristmasTreeSprite tree : trees) {
                tree.update();

            }
            Dino.update(timerInterval);
        }


        invalidate();
        Log.d("LEN", viewHeight + " " + viewWidth);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        if (!Dino.isAlive())
        //проверка на нажатие кнопки рестарт
        {if ((restart_x<=event.getX()&&event.getX()<=restart_x+restart_width&&restart_y<=event.getY()&&event.getY()<=restart_y+restart_height)){
            Dino.setAlive(true);
            this.speed=70;
            points=0;
            GenerateTrees();

        }}
        else if (eventAction == MotionEvent.ACTION_DOWN && !Dino.IsDown() && !Dino.IsUp()) {
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
