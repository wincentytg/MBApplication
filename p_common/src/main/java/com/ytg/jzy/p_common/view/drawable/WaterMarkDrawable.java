package com.ytg.jzy.p_common.view.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by Gethin 2017/3/7 0007
 */

public class WaterMarkDrawable extends Drawable {

    private  int mDegree;
    private MarkDrawable mMarkDrawable;
    private RectF mBoundRect;
    private BitmapShader mShader;
    private Paint mPaint;
    private String mMarkStr;
    private int mTextSize ;
    private int mTextColor;

    private WaterMarkDrawable(WaterMarkBuilder waterMarkBuilder) {
        this.mTextColor = waterMarkBuilder.textColor;
        this.mTextSize = waterMarkBuilder.textSize;
        this.mMarkStr = waterMarkBuilder.markStr;
        this.mDegree = waterMarkBuilder.degree;
        this.mMarkDrawable = new MarkDrawable(mMarkStr,mTextSize,mTextColor,mDegree);
        mBoundRect = new RectF();
        final int width=mMarkDrawable.getIntrinsicWidth();
        final int height=mMarkDrawable.getIntrinsicHeight();
        if (width <= 0 || height <= 0) {
            throw new IllegalStateException("Please Set smallerDegree");
        }
        Bitmap bmp = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        mMarkDrawable.setBounds(0,0,width,height);
        mMarkDrawable.draw(canvas);
        mShader = new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(mShader);
    }

    public static class WaterMarkBuilder{
        private int textSize;
        private int textColor;
        private final String markStr;
        public int degree;

        public WaterMarkBuilder(String markStr) {
            this.markStr = markStr;
        }

        public WaterMarkBuilder setTextSize(int textSize) {
            this.textSize = textSize;
            return  this;
        }

        public WaterMarkBuilder setTextColor(int color) {
            this.textColor = color;
            return this;
        }

        public WaterMarkBuilder setDegree(int degree) {
            this.degree = degree;
            return  this;
        }

        public WaterMarkDrawable build() {
            WaterMarkDrawable waterMarkDrawable = new WaterMarkDrawable(this);
            if (waterMarkDrawable.mMarkStr == null) {
                throw new IllegalStateException("Please Set waterMarkContent");
            }
            return waterMarkDrawable;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, mBoundRect.right, mBoundRect.bottom, mPaint);
    }

    @Override
    public void setAlpha(int i) {
        mPaint.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        mBoundRect.set(left, top, right, bottom);
    }
}
