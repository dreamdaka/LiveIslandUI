package com.example.liveislandui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class LiveIslandView extends View {
    private Paint islandPaint;
    private Paint textPaint;
    private boolean[] isExpanded = new boolean[4];
    private int expandedIsland = -1;
    private int islandWidth;
    private int islandHeight;
    private int expandedWidth;
    private int expandedHeight;
    private Context context;

    public LiveIslandView(Context context) {
        super(context);
        init(context);
    }

    public LiveIslandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;


        islandPaint = new Paint();
        islandPaint.setColor(ContextCompat.getColor(context, R.color.islandColor));
        islandPaint.setStyle(Paint.Style.FILL);
        islandPaint.setAntiAlias(true);


        textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(context, android.R.color.white));
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);


        islandWidth = 200;
        islandHeight = 100;
        expandedWidth = 600;
        expandedHeight = 400;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        for (int i = 0; i < 4; i++) {
            float left = 100 + i * (islandWidth + 50);
            float top = 100;
            float right = left + islandWidth;
            float bottom = top + islandHeight;


            canvas.drawRoundRect(new RectF(left, top, right, bottom), 50, 50, islandPaint);


            canvas.drawText("按钮 " + (i+1), left + islandWidth/2, top + islandHeight/2 + 15, textPaint);


            if (isExpanded[i]) {
                drawExpandedView(canvas, left, bottom + 20, i);
            }
        }
    }

    private void drawExpandedView(Canvas canvas, float left, float top, int islandIndex) {

        canvas.drawRoundRect(new RectF(left, top, left + expandedWidth, top + expandedHeight), 30, 30, islandPaint);


        float itemHeight = expandedHeight / 5;
        for (int i = 0; i < 5; i++) {
            float textY = top + (i * itemHeight) + itemHeight/2 + 15;
            canvas.drawText("测试 " + (i+1), left + 150, textY, textPaint);
            canvas.drawText("开关", left + 400, textY, textPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 1000;
        int desiredHeight = 800;

        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    public boolean handleTouch(float x, float y) {

        for (int i = 0; i < 4; i++) {
            float left = 100 + i * (islandWidth + 50);
            float top = 100;
            float right = left + islandWidth;
            float bottom = top + islandHeight;

            if (x >= left && x <= right && y >= top && y <= bottom) {
                toggleIsland(i);
                return true;
            }


            if (isExpanded[i]) {
                float expLeft = left;
                float expTop = bottom + 20;
                float expRight = expLeft + expandedWidth;
                float expBottom = expTop + expandedHeight;

                if (x >= expLeft && x <= expRight && y >= expTop && y <= expBottom) {

                    if (x >= expLeft + 350 && x <= expLeft + 450) {
                        int itemIndex = (int) ((y - expTop) / (expandedHeight / 5));
                        Toast.makeText(context, "切换开关 " + (itemIndex+1) + " 在按钮 " + (i+1),
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        }

        boolean wasExpanded = false;
        for (int i = 0; i < 4; i++) {
            if (isExpanded[i]) {
                isExpanded[i] = false;
                wasExpanded = true;
            }
        }
        if (wasExpanded) {
            invalidate();
            return true;
        }

        return false;
    }

    private void toggleIsland(int index) {

        if (isExpanded[index]) {
            isExpanded[index] = false;
        } else {

            for (int i = 0; i < 4; i++) {
                isExpanded[i] = false;
            }

            isExpanded[index] = true;
        }
        invalidate();
    }
}