package com.example.musinity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class PianoRollView extends View {

    Paint paint;
    Paint paintHighlight;
    private float [][] notes;
    private double threshold;
    private int measureIndex;
    private int lastPlayedIndex;
    private SharedPreferences sharedPreferences;

    public PianoRollView(Context context) {
        super(context);
        init(context);
    }

    public PianoRollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PianoRollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PianoRollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paintHighlight = new Paint();
        paintHighlight.setColor(Color.BLACK);

        sharedPreferences = context.getSharedPreferences(SettingsFragment.SETTINGS_NAME, 0);
    }

    public void update(float[][] notes, double threshold, int measureIndex) {
        this.notes = notes;
        this.threshold = threshold;
        this.measureIndex = measureIndex;
        postInvalidate();
    }

    public void drawPianoRoll(Canvas canvas) {

        if (!sharedPreferences.getBoolean(SettingsFragment.PIANO_ROLL_KEY, true)) return;

        if (notes == null) return;

        float width = (float) getWidth() / notes[0].length;
        float height = (float) getHeight() / notes.length;

        for (int y = 0; y < notes.length; y++) {
            for (int x = 0; x < notes[y].length; x++) {
                if (notes[y][x] > threshold &&
                        (x == notes[y].length - 1 || notes[y][x+1] <= threshold)) {
                    float left = width * x;
                    float right = left + width;
                    float top = height * y;
                    float bottom = top + height + 5;
                    if (y == measureIndex) {
                        lastPlayedIndex = measureIndex;
                    }
                    if (y == lastPlayedIndex) {
                        canvas.drawRect(left, top, right, bottom, paintHighlight);
                    }
                    else
                        canvas.drawRect(left, top, right, bottom, paint);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("PianoRollView", "onDraw");
        super.onDraw(canvas);
        drawPianoRoll(canvas);
    }


}
