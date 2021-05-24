package com.example.dungeonknight;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import static com.example.dungeonknight.DrawView.player;

/**
 * Created by User on 10.11.2015.
 */
public class Sprite {
    private Bitmap bitmap;

    private List<Rect> frames;
    private int frameWidth;
    private int frameHeight;
    private int currentFrame = 0;
    private double frameTime = 25;
    private double timeForCurrentFrame = 0.0;
    private int count = 0;

    private double x;
    private double y;

    private double velocityX;
    private double velocityY;

    private int padding = 20;

    public Sprite(double x,
                  double y,
                  double velocityX,
                  double velocityY,
                  Rect initialFrame,
                  Bitmap bitmap)     {

        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;

        this.bitmap = bitmap;

        this.frames = new ArrayList<Rect>();
        this.frames.add(initialFrame);

        this.bitmap = bitmap;

        this.frameWidth = initialFrame.width();
        this.frameHeight = initialFrame.height();
    }

    public Sprite() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public double getVx() {
        return velocityX;
    }

    public void setVx(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVy() {
        return velocityY;
    }

    public void setVy(double velocityY) {
        this.velocityY = velocityY;
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
        this.frameTime = Math.abs(frameTime);;
    }

    public double getTimeForCurrentFrame() {
        return timeForCurrentFrame;
    }

    public void setTimeForCurrentFrame(double timeForCurrentFrame) {
        this.timeForCurrentFrame = Math.abs(timeForCurrentFrame);
    }

    public void addFrame (Rect frame) {
        frames.add(frame);
    }

    public int getCount() {
        return count;
    }

    public void setCount (int count) {
        this.count = count;
    }

    public int getFramesCount () {
        return frames.size();
    }


    public void update (int ms) {
        timeForCurrentFrame += ms;

        if (timeForCurrentFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % frames.size();
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
            count++;
        }

        x = x + velocityX;
    }
    public void draw (Canvas canvas) {
        Paint p = new Paint();

        Rect destination = new Rect((int)x, (int)y, (int)(x + frameWidth), (int)(y + frameHeight));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination,  p);
    }

    public Rect getBoundingBoxRect () {
        return new Rect((int)x+padding,
                (int)y+padding,
                (int)(x + frameWidth - 2 *padding),
                (int)(y + frameHeight - 2* padding));
    }


    public boolean intersect (com.example.dungeonknight.Sprite s) {
        return getBoundingBoxRect().intersect(s.getBoundingBoxRect());
    }

}