package com.example.dungeonknight;

import android.graphics.Canvas;
import android.graphics.Rect;

import static com.example.dungeonknight.DrawView.player;
import static com.example.dungeonknight.DrawView.wallDown;
import static com.example.dungeonknight.GameStart.*;

public class Player extends Sprite {
    private boolean right = true;
    private Sprite playerRight;
    private Sprite playerLeft;
    private Sprite attackLeft;
    private Sprite attackRight;
    private int speed;
    private boolean attacked;

    public Player(double x, double y) {
        int w = rightPlayerIMG.getWidth() / 3;
        int h = rightPlayerIMG.getHeight();
        Rect firstFrame = new Rect(0, 0, w, h);
        playerRight = new Sprite(x, y, 0, 0, firstFrame, rightPlayerIMG);
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 2; j++) {
                playerRight.addFrame(new Rect(j * w, 0, j * w + w, h));
            }
        }


        w = leftPlayerIMG.getWidth() / 3;
        h = leftPlayerIMG.getHeight();
        firstFrame = new Rect(0, 0, w, h);
        playerLeft = new Sprite(x, y, 0, 0, firstFrame, leftPlayerIMG);
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 2; j++) {
                playerLeft.addFrame(new Rect(j * w, 0, j * w + w, h));
            }
        }
        w = attackRightIMG.getWidth() / 5;
        h = attackRightIMG.getHeight();
        firstFrame = new Rect(0, 0, w, h);
        attackLeft = new Sprite(x, y, 0, 0, firstFrame, attackLeftIMG);
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {
                attackLeft.addFrame(new Rect(j * w, 0, j * w + w, h));
            }
        }
        attackRight = new Sprite(x, y, 0, 0, firstFrame, attackRightIMG);
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {
                attackRight.addFrame(new Rect(j * w, 0, j * w + w, h));
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (right) {
            if (attacked) {
                attackRight.draw(canvas);
            } else playerRight.draw(canvas);
        } else {
            if (attacked) {
                attackLeft.draw(canvas);
            } else playerLeft.draw(canvas);
        }
    }

    public void attack() {
        attacked = true;
        if (right) {
            attackRight.setY(wallDown.getY() - attackRight.getFrameHeight());
            attackRight.setX(playerRight.getX());
        } else {
            attackLeft.setY(wallDown.getY() - attackLeft.getFrameHeight());
            attackLeft.setX(playerLeft.getX());
        }
    }

    @Override
    public void update(int ms) {
        if (right) {
            if (attacked) {
                attackRight.update(10);
                if (attackRight.getCount() >= 5) {
                    attacked = false;
                    attackRight.setCount(0);
                }
            } else {
                playerRight.setVx(speed);
                playerRight.update(ms);
            }
        } else {
            if (attacked) {
                attackLeft.update(10);
                if (attackLeft.getCount() >= 5) {
                    attacked = false;
                    attackLeft.setCount(0);
                }
            } else {
                playerLeft.setVx(speed);
                playerLeft.update(ms);
            }
        }
    }

    public void setRight(boolean right) {
        if (this.right != right) {
            if (this.right == true) {
                playerLeft.setX(playerRight.getX() - 20);
            } else {
                playerRight.setX(playerLeft.getX() + 20);
            }
        }
        this.right = right;
    }

    public boolean getRight() {
        return right;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public double getX() {
        if (right) {
            return playerRight.getX();
        }
        return playerLeft.getX();
    }

    public double intersectLeftX() {
        if (!right) return playerLeft.getX() + playerLeft.getFrameWidth()/3;
        return playerRight.getX();
    }

    public double intersectRightX() {
        if (right) return playerRight.getX() + playerRight.getFrameWidth() - playerRight.getFrameWidth()/3;
        return playerLeft.getX() + playerLeft.getFrameWidth();
    }

    @Override
    public int getFrameWidth() {
        if (right) {
            return playerRight.getFrameWidth();
        }
        return playerLeft.getFrameWidth();
    }

    @Override
    public void setCurrentFrame(int currentFrame) {
        if (right) {
            playerRight.setCurrentFrame(0);
        } else {
            playerLeft.setCurrentFrame(0);
        }
    }

    @Override
    public boolean intersect(Sprite s) {
        if (right) {
            return playerRight.intersect(s);
        }
        return playerLeft.intersect(s);
    }

    public boolean isAttacked() {
        return attacked;
    }

    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }

    @Override
    public void setX(double x) {
        if (right) playerRight.setX(x);
        else playerLeft.setX(x);
    }
}
