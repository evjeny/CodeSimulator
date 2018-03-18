package com.evjeny.hackersimulator.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.evjeny.hackersimulator.game.IText;
import com.evjeny.hackersimulator.game.Image;

/**
 * Created by evjeny on 08.03.2018 19:41.
 */

public class TImageView extends View {

    private Image image = null;
    private Paint paint, textPaint;

    public TImageView(Context context) {
        super(context);
        init();

    }

    public TImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.paint = new Paint();
        paint.setAntiAlias(true);
        this.textPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (image != null) {
            Bitmap bitmap = image.bitmap;
            int width = bitmap.getWidth(), height = bitmap.getHeight();
            int canvasWidth = canvas.getWidth(), canvasHeight = canvas.getHeight();
            double kScale = 1;
            if (width < canvasWidth || height < canvasHeight) {
                double kWidth = (double) width / canvasWidth;
                double kHeight = (double) height / canvasHeight;
                kScale = Math.min(kWidth, kHeight);
                width *= kScale;
                height *= kScale;
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            }
            float startLeft = (canvas.getWidth() - bitmap.getWidth()) / 2.0f;
            float startTop = (canvas.getHeight() - bitmap.getHeight()) / 2.0f;

            logd(", ", "width: " + width, "height: " + height,
                    "cWidth: " + canvasWidth, "cHeight: " + canvasHeight,
                    "startLeft: " + startLeft, "startTop: " + startTop);

            canvas.drawBitmap(bitmap, startLeft, startTop, paint);
            for (IText text : image.texts) {
                float textPosX = map(text.posX, 100, width);
                float textPosY = map(text.posY, 100, height);
                float textSize = (float) (text.textSize * kScale);

                textPaint.setColor(text.color);
                textPaint.setStyle(Paint.Style.FILL);
                textPaint.setTextSize(textSize);

                logd(" ", "Drawing text \'", text.text, "\' at",
                        "" + textPosX, "" + textPosY);

                canvas.drawText(text.text, startLeft + textPosX, startTop + textPosY,
                        textPaint);
            }
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    public void generateImage(Image image) {
        this.image = image;
        invalidate();
    }

    private float map(float value, float fromHigh, float toHigh) {
        return value * toHigh / fromHigh;
    }

    private void logd(String separator, String... args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]);
            if (i < args.length - 1) builder.append(separator);
        }
        Log.d("TImageView", builder.toString());
    }

}
