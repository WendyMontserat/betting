package com.xbetvsfonbet.competition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class VolleyBallActivity extends BallActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_volleyball);

        m_balls = new Ball[12];
        m_balls[0] = new Ball(findViewById(R.id.iv_ball_0), 0, 1, -5);
        m_balls[1] = new Ball(findViewById(R.id.iv_ball_1), 1, -2, 4);
        m_balls[2] = new Ball(findViewById(R.id.iv_ball_2), 2, 3, -3);
        m_balls[3] = new Ball(findViewById(R.id.iv_ball_3), 3, -4, 3);
        m_balls[4] = new Ball(findViewById(R.id.iv_ball_4), 4, 4, -2);
        m_balls[5] = new Ball(findViewById(R.id.iv_ball_5), 5, -5, 1);
        m_balls[6] = new Ball(findViewById(R.id.iv_ball_6), 6, 1, 5);
        m_balls[7] = new Ball(findViewById(R.id.iv_ball_7), 7, 2, 4);
        m_balls[8] = new Ball(findViewById(R.id.iv_ball_8), 8, -3, 3);
        m_balls[9] = new Ball(findViewById(R.id.iv_ball_9), 9, 4, -3);
        m_balls[10] = new Ball(findViewById(R.id.iv_ball_10), 10, -4, -2);
        m_balls[11] = new Ball(findViewById(R.id.iv_ball_11), 11, 5, -1);
    }

    @Override
    void alignBalls() {
        int halfWidth = m_fieldWidth / 2;
        int halfHeight = m_fieldHeight / 2;
        int step = (halfHeight - 2 * m_ballSize) / 3;

        m_balls[0].view.setX(halfWidth - m_ballSize - step / 2);
        m_balls[0].view.setY(step);

        m_balls[1].view.setX(halfWidth);
        m_balls[1].view.setY(step);

        m_balls[2].view.setX(halfWidth + m_ballSize + step / 2);
        m_balls[2].view.setY(step);

        m_balls[3].view.setX(halfWidth - m_ballSize - step / 2);
        m_balls[3].view.setY(halfHeight - m_ballSize / 2 - step);

        m_balls[4].view.setX(halfWidth);
        m_balls[4].view.setY(halfHeight - m_ballSize / 2 - step);

        m_balls[5].view.setX(halfWidth + m_ballSize + step / 2);
        m_balls[5].view.setY(halfHeight - m_ballSize / 2 - step);


        m_balls[6].view.setX(halfWidth - m_ballSize - step / 2);
        m_balls[6].view.setY(halfHeight + m_ballSize / 2 + step);

        m_balls[7].view.setX(halfWidth);
        m_balls[7].view.setY(halfHeight + m_ballSize / 2 + step);

        m_balls[8].view.setX(halfWidth + m_ballSize + step / 2);
        m_balls[8].view.setY(halfHeight + m_ballSize / 2 + step);

        m_balls[9].view.setX(halfWidth - m_ballSize - step / 2);
        m_balls[9].view.setY(m_fieldHeight - step);

        m_balls[10].view.setX(halfWidth);
        m_balls[10].view.setY(m_fieldHeight - step);

        m_balls[11].view.setX(halfWidth + m_ballSize + step / 2);
        m_balls[11].view.setY(m_fieldHeight - step);
    }

    @Override
    int getSoundResId() {
        return getResources().getIdentifier("volleyball", "raw", getPackageName());
    }

    protected int getFieldTop(Ball ball) {
        return ball.team < (m_balls.length / 2) ? 0 : (m_fieldHeight + m_ballSize) / 2;
    }

    protected int getFieldBottom(Ball ball) {
        return ball.team < (m_balls.length / 2) ? (m_fieldHeight - m_ballSize) / 2 : m_fieldHeight;
    }

    protected double getBallSpeed(Ball ball) {
        double speed = Math.sqrt(ball.dX * ball.dX + ball.dY * ball.dY) / 8;
        Log.d("Volley", Double.toString(speed));
        return speed;
    }
}