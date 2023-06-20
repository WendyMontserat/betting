package com.xbetvsfonbet.competition;

import android.widget.ImageView;

public class Ball {

    ImageView view;
    int team;
    public int dX;
    public int dY;

    public Ball(ImageView view, int team, int dX, int dY) {
        this.view = view;
        this.team = team;
        this.dX = dX;
        this.dY = dY;
    }
}
