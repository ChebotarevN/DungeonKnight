package com.example.dungeonknight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.example.dungeonknight.DrawThread.*;
import static com.example.dungeonknight.GameStart.*;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    public static DrawThread drawThread;
    public static Player player;
    public static Sprite enemy;
    public static Sprite enemyRight;
    public static Sprite wallDown;
    public static Sprite wallUp;
    public static Sprite wallLeft;
    public static Sprite wallRight;
    public static Sprite torch;
    public static Sprite torch2;
    public static Sprite buttonLeft;
    public static Sprite buttonRight;
    public static Sprite buttonAttack;
    public static String stopGame = "";
    public static int points = 0;

    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Нижняя стенка и верхня стенка - начало
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.wall_down);
        b = Bitmap.createScaledBitmap(b, width, 150, false);
        int w = b.getWidth();
        int h = b.getHeight();
        Rect firstFrame = new Rect(0, 0, w, h);
        wallDown = new Sprite(0, height - h, 0, 0, firstFrame, b);
        b = Bitmap.createScaledBitmap(b, width, 30, false);
        w = b.getWidth();
        h = b.getHeight();
        firstFrame = new Rect(0, 0, w, h);
        wallUp = new Sprite(0, 0, 0, 0, firstFrame, b);
        // Нижняя стенка и верхня стенка - конец

        // Левая стенка и правая стенка - начало
        b = BitmapFactory.decodeResource(getResources(), R.drawable.wall_left);
        b = Bitmap.createScaledBitmap(b, 30, height, false);
        w = b.getWidth();
        h = b.getHeight();
        firstFrame = new Rect(0, 0, w, h);
        wallLeft = new Sprite(0, -270, 0, 0, firstFrame, b);
        wallRight = new Sprite(width - w, -270, 0, 0, firstFrame, b);
        // Левая стенка и правая стенка - конец

        // Игрок - начало
        player = new Player(width / 2, wallDown.getY() - rightPlayerIMG.getHeight());
        // Игрок - конец

        // Факел - начало
        b = BitmapFactory.decodeResource(getResources(), R.drawable.torch);
        w = b.getWidth() / 9;
        h = b.getHeight();
        firstFrame = new Rect(8 * w, 0, 9 * w, h);
        torch = new Sprite(300, 300, 0, 0, firstFrame, b);
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 8; j++) {
                torch.addFrame(new Rect(j * w, 0, j * w + w, h));
            }
        }
        firstFrame = new Rect(0, 0, w, h);
        torch2 = new Sprite(width - 300, 300, 0, 0, firstFrame, b);
        for (int j = 0; j < 8; j++) {
            torch2.addFrame(new Rect(j * w, 0, j * w + w, h));
        }
        // Факел - конец

        //Кнопки - начало
        b = BitmapFactory.decodeResource(getResources(), R.drawable.button_left);
        w = b.getWidth();
        h = b.getHeight();
        firstFrame = new Rect(0, 0, w, h);
        buttonLeft = new Sprite(20, height - h - 10, 0, 0, firstFrame, b);

        b = BitmapFactory.decodeResource(getResources(), R.drawable.button_right);
        buttonRight = new Sprite(40 + w, height - h - 10, 0, 0, firstFrame, b);

        b = BitmapFactory.decodeResource(getResources(), R.drawable.button_attack);
        w = b.getWidth();
        h = b.getHeight();
        firstFrame = new Rect(0, 0, w, h);
        buttonAttack = new Sprite(width - 20 - w, height - h - 10, 0, 0, firstFrame, b);
        //Кнопки - конец

        // Противник - начало
        b = BitmapFactory.decodeResource(getResources(), R.drawable.skelet_right);
        w = b.getWidth() / 7;
        h = b.getHeight();
        firstFrame = new Rect(0, 0, w, h);
        enemy = new Sprite(-100, wallDown.getY() - b.getHeight(), speed, 0, firstFrame, b);

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 6; j++) {
                enemy.addFrame(new Rect(j * w, 0, j * w + w, h));
            }
        }
        b = BitmapFactory.decodeResource(getResources(), R.drawable.skelet_left);
        w = b.getWidth() / 7;
        h = b.getHeight();
        enemyRight = new Sprite(width, wallDown.getY() - b.getHeight(), -speed, 0, firstFrame, b);

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 6; j++) {
                enemyRight.addFrame(new Rect(j * w, 0, j * w + w, h));
            }
        }
        //Противник - конец

        drawThread = new DrawThread(getContext(), getHolder());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                //
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_DOWN) {
            if (event.getY() < 70 & event.getX() < 150) {
                stopGame = "Pause";
                pause = true;
            } else if (pause) {
                stopGame = "";
                timerInterval = 30;
                pause = false;
            } else if (gameOver) {
            }
            if (event.getX() >= buttonLeft.getX() &
                    event.getX() <= buttonLeft.getX() + buttonLeft.getFrameWidth() &
                    event.getY() >= buttonLeft.getY() &
                    event.getY() <= buttonLeft.getY() + buttonLeft.getFrameHeight() &
                    !player.isAttacked()) {
                player.setRight(false);
                player.setSpeed(-4);
            } else if (event.getX() >= buttonRight.getX() &
                    event.getX() <= buttonRight.getX() + buttonRight.getFrameWidth() &
                    event.getY() >= buttonRight.getY() &
                    event.getY() <= buttonRight.getY() + buttonRight.getFrameHeight() &
                    !player.isAttacked()) {
                player.setRight(true);
                player.setSpeed(4);
            } else if (event.getX() >= buttonAttack.getX() &
                    event.getX() <= buttonAttack.getX() + buttonAttack.getFrameWidth() &
                    event.getY() >= buttonAttack.getY() &
                    event.getY() <= buttonAttack.getY() + buttonAttack.getFrameWidth()) {
                player.attack();
            }
        }
        if (eventAction == MotionEvent.ACTION_UP) {
            player.setCurrentFrame(0);
            player.setSpeed(0);
        }
        return true;
    }
}