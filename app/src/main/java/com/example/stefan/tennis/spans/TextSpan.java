package com.example.stefan.tennis.spans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.style.LineBackgroundSpan;

public class TextSpan implements LineBackgroundSpan {

    private final String text;

    public TextSpan(String text) {
        this.text = text;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence txt, int start, int end, int lnum) {
        Paint paint2 = new Paint();
        paint2.setColor(Color.BLUE);
        paint2.setTextSize(p.getTextSize() * 0.75f);
        paint2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        float w = paint2.measureText(text) / 2;
        float textSize = paint2.getTextSize();
        paint2.setTextAlign(Paint.Align.CENTER);
        c.drawRect(0, 50, 50, 50, p);
        c.drawText(String.valueOf(text), (left + right) / 2, bottom + 30, paint2);
    }
}
