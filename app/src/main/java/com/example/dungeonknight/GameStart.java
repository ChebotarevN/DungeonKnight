package com.example.dungeonknight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import static com.example.dungeonknight.DrawView.*;
import static com.example.dungeonknight.DrawThread.*;

public class GameStart extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;
    private Toast stopToast;
    public static Bitmap background;
    public static Bitmap rightPlayerIMG;
    public static Bitmap leftPlayerIMG;
    public static Bitmap attackLeftIMG;
    public static Bitmap attackRightIMG;
    public static int width;
    public static int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.main_background);
        rightPlayerIMG = BitmapFactory.decodeResource(getResources(), R.drawable.right_player);
        leftPlayerIMG = BitmapFactory.decodeResource(getResources(), R.drawable.player);
        attackLeftIMG = BitmapFactory.decodeResource(getResources(), R.drawable.attack_left);
        attackRightIMG = BitmapFactory.decodeResource(getResources(), R.drawable.attack_right);

    }

    @Override
    public void onBackPressed() {
        stopToast = Toast.makeText(getBaseContext(), R.string.stop, Toast.LENGTH_SHORT);
        if (pause) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                stopToast.cancel();
                stopGame = "";
                points = 0;
                timerInterval = 30;
                pause = false;
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
                try {
                    Intent i = new Intent(GameStart.this, MainActivity.class);
                    startActivity(i);finish();
                } catch (Exception e) {
                }
                return;
            } else {
                backToast = Toast.makeText(getBaseContext(), R.string.back, Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        } else {
            stopToast.show();
        }
    }
}