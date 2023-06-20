package com.xbetvsfonbet.competition;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

abstract class BallActivity extends AppCompatActivity {

    int m_fieldWidth;
    int m_fieldHeight;
    int m_ballSize;
    Ball[] m_balls;
    MediaPlayer m_player = null;
    int m_currentPos = 0;
    Handler m_handler;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        m_ballSize = getResources().getDimensionPixelSize(R.dimen.dimenBall);
        m_handler = new Handler();

        RelativeLayout rlField = findViewById(R.id.rl_field);
        rlField.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                m_fieldWidth = right - left - m_ballSize;
                m_fieldHeight = bottom - top - m_ballSize;
                alignBalls();
            }
        });

        m_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (m_player != null) {
            m_player.seekTo(m_currentPos);
            m_player.start();
        }
    }

    @Override
    protected void onPause() {
        if (m_player != null && m_player.isPlaying()) {
            m_currentPos = m_player.getCurrentPosition();
            m_player.pause();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        stop(false);

        super.onBackPressed();
    }

    abstract void alignBalls();

    abstract int getSoundResId();

    protected abstract int getFieldTop(Ball ball);

    protected abstract int getFieldBottom(Ball ball);

    protected abstract double getBallSpeed(Ball ball);

    private void start() {
        m_player = MediaPlayer.create(this, getSoundResId());
        m_player.start();

        for (Ball ball : m_balls) {
            animateBall(ball);
        }

        m_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stop(true);
            }
        }, 30000);
    }

    private void stop(boolean finish) {
        m_handler.removeCallbacksAndMessages(null);
        if (m_player != null) {
            m_player.stop();
            m_player.release();
            m_player = null;
        }
        if (finish) {
            TeamType teamType = null;
            switch (AppApplication.getRandom(2)) {
                case 0:
                    teamType = AppApplication.getFirstTeam();
                    break;
                case 1:
                    teamType = AppApplication.getSecondTeam();
                    break;
            }
            Intent intent = WinActivity.getStartIntent(this, teamType);
            startActivity(intent);
        }
    }

    private void animateBall(Ball ball) {
        float x0 = Math.round(ball.view.getX());
        float y0 = Math.round(ball.view.getY());
        float x1 = x0 + 100 * ball.dX;
        float y1 = y0 + 100 * ball.dY;

        PointF crossH;
        PointF crossV;
        int fieldTop = getFieldTop(ball);
        int fieldBottom = getFieldBottom(ball);
        if (ball.dX > 0) {
            crossV = calculateCross(x0, y0, x1, y1, m_fieldWidth, fieldTop, m_fieldWidth, fieldBottom);
        } else {
            crossV = calculateCross(x0, y0, x1, y1, 0, fieldTop, 0, fieldBottom);
        }
        if (ball.dY > 0) {
            crossH = calculateCross(x0, y0, x1, y1, 0, fieldBottom, m_fieldWidth, fieldBottom);
        } else {
            crossH = calculateCross(x0, y0, x1, y1, 0, fieldTop, m_fieldWidth, fieldTop);
        }

        double speed = getBallSpeed(ball);
        long duration;
        float dX = crossV.x - x1;
        float dY = crossV.y - y1;
        float distV = dX * dX + dY * dY;
        dX = crossH.x - x1;
        dY = crossH.y - y1;
        float distH = dX * dX + dY * dY;
        if (distH < distV) {
            ball.dY = -ball.dY;
            x1 = crossH.x;
            y1 = crossH.y;
            duration = Math.round(Math.sqrt(distH) / speed);
        } else {
            ball.dX = -ball.dX;
            x1 = crossV.x;
            y1 = crossV.y;
            duration = Math.round(Math.sqrt(distV) / speed);
        }

        if (x1 < 0) {
            x1 = m_fieldWidth;
        } else if (x1 > m_fieldWidth) {
            x1 = 0;
        }
        if (y1 < fieldTop) {
            y1 = fieldBottom;
        } else if (y1 > fieldBottom) {
            y1 = fieldTop;
        }
        ball.view.animate()
                .x(Math.round(x1))
                .y(Math.round(y1))
                .setDuration(duration)
                .setInterpolator(new LinearInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        animateBall(ball);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
    }

    private PointF calculateCross(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float n;
        if (y2 - y1 != 0) {
            float q = (x2 - x1) / (y1 - y2);
            float sn = (x3 - x4) + (y3 - y4) * q;
            if (sn == 0) {
                return new PointF(0, 0);
            }
            float fn = (x3 - x1) + (y3 - y1) * q;
            n = fn / sn;
        }
        else {
            if ((y3 - y4) == 0) {
                return new PointF(0, 0);
            }
            n = (y3 - y1) / (y3 - y4);
        }
        return new PointF(x3 + (x4 - x3) * n, y3 + (y4 - y3) * n);
    }
}
