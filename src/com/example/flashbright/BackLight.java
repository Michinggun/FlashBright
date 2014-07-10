package com.example.flashbright;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class BackLight extends View{
	Paint paint = new Paint();

    public BackLight(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas) {
            //canvas.drawLine(0, 0, 20, 20, paint);
            //canvas.drawLine(20, 0, 0, 20, paint);
    	
    }
}