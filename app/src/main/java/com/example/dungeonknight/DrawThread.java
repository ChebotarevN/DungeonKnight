package com.example.dungeonknight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import static com.example.dungeonknight.DrawView.*;
import static com.example.dungeonknight.GameStart.*;

public class DrawThread extends Thread {
    public static int timerInterval = 30;
    public static int speed = 5;
    public static boolean gameOver = false;
    public static boolean pause = false;
    private Timer t;
    public static SurfaceHolder surfaceHolder;
    private ImageView backgroundImg;
    private volatile boolean running = true;//флаг для остановки потока

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        t = new Timer();
        t.start();
        backgroundImg = new ImageView(context);
    }

    public void requestStop() {
        t.cancel();
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    // рисование на canvas
                    Paint p = new Paint();
                    p.setAntiAlias(true);
                    p.setTextSize(45.0f);
                    p.setColor(Color.WHITE);

                    // Фон - начало
                    background = Bitmap.createScaledBitmap(background, width, height, false);
                    backgroundImg.setImageBitmap(background);
                    backgroundImg.layout(0, 0, width, height);
                    backgroundImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    backgroundImg.draw(canvas);
                    // Фон - конец

                    // Объекты - начало
                    player.draw(canvas);
                    enemy.draw(canvas);
                    enemyRight.draw(canvas);
                    torch.draw(canvas);
                    torch2.draw(canvas);
                    wallDown.draw(canvas);
                    wallUp.draw(canvas);
                    wallLeft.draw(canvas);
                    wallRight.draw(canvas);
                    // Объекты - конец

                    // Интерфейс - начало
                    buttonLeft.draw(canvas);
                    buttonRight.draw(canvas);
                    buttonAttack.draw(canvas);
                    canvas.drawText(points + "", width - 300, 70, p);
                    canvas.drawText(stopGame + "", (width / 2) - 150, height / 2, p);
                    canvas.drawText("Pause", 0, 40, p);
                    // Интерфейс - конец

                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }


    protected void update() {
        if (!pause) {
            if (points <= -100) {
                stopGame = "Game Over";
                points = 0;
                enemy.setX(0 - enemy.getFrameWidth());
                enemyRight.setX(width);
                player.setX(width / 2);
                pause = true;
            }
            if (player.getX() <= wallLeft.getFrameWidth() & player.getSpeed() < 0) {
                player.setCurrentFrame(0);
                player.setSpeed(0);
            } else if (player.getX() + player.getFrameWidth() >= wallRight.getX() & player.getSpeed() > 0) {
                player.setCurrentFrame(0);
                player.setSpeed(0);
            }
            if (enemy.getX() + enemy.getFrameWidth() >= player.intersectLeftX()) {
                points -= 10;
                teleportEnemyLeft();
            }
            if (enemyRight.getX() <= player.intersectRightX()) {
                points -= 10;
                teleportEnemyRight();
            }
            if (player.getSpeed() != 0 | player.isAttacked()) player.update(speed);
            torch.update(10);
            torch2.update(10);
            enemy.update(speed);
            enemyRight.update(speed);
            if ((enemy.getX() + enemy.getFrameWidth() >= player.getX() & !player.getRight()) & player.isAttacked()) {
                points += 10;
                teleportEnemyLeft();
            }
            if ((enemyRight.getX() <= player.getX() + player.getFrameWidth() & player.getRight()) & player.isAttacked()) {
                points += 10;
                teleportEnemyRight();
            }
        }
    }

    public static void teleportEnemyLeft() {
        enemy.setX(-Math.random() * 500);
    }

    public static void teleportEnemyRight() {
        enemyRight.setX(width + Math.random() * 500);
    }

    class Timer extends CountDownTimer {

        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            update();
        }

        @Override
        public void onFinish() {
        }
    }
}