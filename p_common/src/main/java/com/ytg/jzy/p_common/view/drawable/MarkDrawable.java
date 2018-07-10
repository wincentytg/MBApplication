package com.ytg.jzy.p_common.view.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

import com.ytg.jzy.p_common.utils.DensityUtil;


/**
 * Created by Gethin 2017/3/7 0007
 */

public class MarkDrawable extends Drawable {
    private TextPaint mPaint;
    private int mTextColor= 0x77dbdbdb;
    public RectF mBoundRect;
    private String mMarkStr;
    private static final int inset = DensityUtil.dip2px(40);
    private int mTextSize=40;
    private int mDegree=30;

    public MarkDrawable(String mMarkStr, int textSize, int textColor, int degree) {

        this.mMarkStr = mMarkStr;
        this.mTextSize = textSize;
        this.mTextColor = textColor;
        this.mDegree = degree;
        if (textSize == 0) {
            this.mTextSize = DensityUtil.dip2px(15);
        }
        if (textColor == 0) {
            this.mTextColor = 0x7Fdbdbdb;
        }
        if (degree == 0) {
            this.mDegree = 30;
        }

        init();
    }


    private void init(){
//        mMarkStr = mMark[new Random().nextInt()+1];
        mPaint=new TextPaint();
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        final float width=mPaint.measureText(mMarkStr,0,mMarkStr.length());

        Rect rect=new Rect();
        mPaint.getTextBounds(mMarkStr,0,mMarkStr.length(),rect);
        mBoundRect=new RectF();
        mBoundRect.set(0,0,(float) (width* Math.cos(Math.toRadians(mDegree) ))+inset,(float)(width* Math.sin(Math.toRadians(mDegree)))+inset);
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.save();
        canvas.translate(mBoundRect.width()/2,mBoundRect.height()/2);
        canvas.rotate(-mDegree);
        canvas.drawText(mMarkStr,inset/2-mBoundRect.width()/2,0,mPaint);
        canvas.restore();

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
        return PixelFormat.TRANSLUCENT;

    }

    @Override
    public int getIntrinsicHeight() {
        return (int)mBoundRect.height();
    }

    @Override
    public int getIntrinsicWidth() {
        return (int)mBoundRect.width();
    }
}
