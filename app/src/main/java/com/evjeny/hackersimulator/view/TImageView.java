package com.evjeny.hackersimulator.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
            if (width < canvasWidth || height < canvasHeight) {
                double kWidth = (double) width / canvasWidth;
                double kHeight = (double) height / canvasHeight;
                double kScale = Math.min(kWidth, kHeight);
                width *= kScale;
                height *= kScale;
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            }
            float startLeft = (canvas.getWidth() - bitmap.getWidth()) / 2.0f;
            float startTop = (canvas.getHeight() - bitmap.getHeight()) / 2.0f;

            canvas.drawBitmap(bitmap, startLeft, startTop, paint);
            for (IText text : image.texts) {
                textPaint.setColor(text.color);
                textPaint.setStyle(Paint.Style.FILL);
                textPaint.setTextSize(text.textSize);
                canvas.drawText(text.text, startLeft + text.posX, startTop + text.posY,
                        textPaint);
            }
        }
    }

    public void generateImage(Image image) {
        this.image = image;
        invalidate();
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
}
