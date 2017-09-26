package com.sxu.commonlibrary.uiwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.sxu.commonlibrary.R;

/*******************************************************************************
 * FileName: CircleProgress
 * <p>
 * Description:
 * <p>
 * Author: juhg
 * <p>
 * Version: v1.0
 * <p>
 * Date: 16/9/23
 * <p>
 * Copyright: all rights reserved by zhinanmao.
 *******************************************************************************/
public class CircleProgressBar extends View {

    private int mOutBorderColor;
    private int mInBorderColor;
    private int mInCircleColor;
    private int mProgressColor;
    private int mProgressDefaultColor;
    private int mOutBorderWidth;
    private int mInBorderWidth;
    private int mProgressWidth;
    private String mDescText;
    private int mDescTextSize;
    private int mDescTextColor;
    private String mProgressText;
    private int mProgressTextSize;
    private int mProgressTextColor;
    private int mProgressTextStyle;
    private int mLineGap;


    private Paint innerCirclePaint;
    private Paint inBorderPaint;
    private Paint progressPaint;
    private Paint outBorderPaint;
    private Paint textPaint;

    private int progress;

    private final int TEXT_STYLE_NORMAL = 0;
    private final int TEXT_STYLE_BOLD = 1;
    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arrays = context.obtainStyledAttributes(attrs, R.styleable.cl_CircleProgressBar);
        mOutBorderColor = arrays.getColor(R.styleable.cl_CircleProgressBar_cl_outBorderColor, Color.WHITE);
        mInBorderColor = arrays.getColor(R.styleable.cl_CircleProgressBar_cl_inBorderColor, Color.WHITE);
        mInCircleColor = arrays.getColor(R.styleable.cl_CircleProgressBar_cl_inCircleColor, Color.WHITE);
        mProgressColor = arrays.getColor(R.styleable.cl_CircleProgressBar_cl_progressColor, Color.WHITE);
        mProgressDefaultColor = arrays.getColor(R.styleable.cl_CircleProgressBar_cl_progressDefaultColor, Color.WHITE);
        mOutBorderWidth = arrays.getDimensionPixelOffset(R.styleable.cl_CircleProgressBar_cl_outBorderWidth, 0);
        mInBorderWidth = arrays.getDimensionPixelOffset(R.styleable.cl_CircleProgressBar_cl_inBorderWidth, 0);
        mProgressWidth = arrays.getDimensionPixelOffset(R.styleable.cl_CircleProgressBar_cl_progressWidth, 0);
        mDescText = arrays.getString(R.styleable.cl_CircleProgressBar_cl_descText);
        mDescTextSize = arrays.getDimensionPixelOffset(R.styleable.cl_CircleProgressBar_cl_descTextSize, 0);
        mDescTextColor = arrays.getColor(R.styleable.cl_CircleProgressBar_cl_descTextColor, Color.WHITE);
        mProgressText = arrays.getString(R.styleable.cl_CircleProgressBar_cl_progressText);
        mProgressTextSize = arrays.getDimensionPixelOffset(R.styleable.cl_CircleProgressBar_cl_progressTextSize, 0);
        mProgressTextColor = arrays.getColor(R.styleable.cl_CircleProgressBar_cl_progressTextColor, Color.WHITE);
        mProgressTextStyle = arrays.getInt(R.styleable.cl_CircleProgressBar_cl_progressTextStyle, TEXT_STYLE_NORMAL);
        mLineGap = arrays.getDimensionPixelOffset(R.styleable.cl_CircleProgressBar_cl_lineGap, 60);
        arrays.recycle();

        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);
    }

    private void init() {

        innerCirclePaint = new Paint();
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setColor(mInCircleColor);

        if (mInBorderWidth != 0) {
            inBorderPaint = new Paint();
            inBorderPaint.setAntiAlias(true);
            inBorderPaint.setStyle(Paint.Style.STROKE);
            inBorderPaint.setColor(mInBorderColor);
            inBorderPaint.setStrokeWidth(mInBorderWidth);
        }
        if (mProgressWidth != 0) {
            progressPaint = new Paint();
            progressPaint.setAntiAlias(true);
            progressPaint.setColor(mProgressColor);
            progressPaint.setStyle(Paint.Style.STROKE);
            progressPaint.setStrokeWidth(mProgressWidth);
        }
        if (mOutBorderWidth != 0) {
            outBorderPaint = new Paint();
            outBorderPaint.setAntiAlias(true);
            outBorderPaint.setStyle(Paint.Style.STROKE);
            outBorderPaint.setColor(mOutBorderColor);
            outBorderPaint.setStrokeWidth(mOutBorderWidth);
        }

        if (!TextUtils.isEmpty(mProgressText) || !TextUtils.isEmpty(mDescText)) {
            textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = centerX > centerY ? centerY : centerX;
        if (outBorderPaint != null) {
            canvas.drawCircle(centerX, centerY, radius - mOutBorderWidth / 2, outBorderPaint);
        }
        if (progressPaint != null) {
            Paint defaultProgressPaint = progressPaint;
            defaultProgressPaint.setColor(mProgressDefaultColor);
            canvas.drawCircle(centerX, centerY, radius - mOutBorderWidth - mProgressWidth / 2, defaultProgressPaint);
            float angle = progress * 3.6f;
            int leftTop = mOutBorderWidth + mProgressWidth / 2;
            int rightBottom = radius * 2 - mOutBorderWidth - mProgressWidth / 2;
            RectF rect = new RectF(leftTop, leftTop, rightBottom, rightBottom);
            progressPaint.setColor(mProgressColor);
            canvas.drawArc(rect, -90, angle, false, progressPaint);
        }
        if (inBorderPaint != null) {
            canvas.drawCircle(centerX, centerY, radius - mOutBorderWidth - mProgressWidth - mInBorderWidth / 2, inBorderPaint);
        }
        if (innerCirclePaint != null) {
            canvas.drawCircle(centerX, centerY, radius - mOutBorderWidth - mProgressWidth - mInBorderWidth, innerCirclePaint);
        }

        if (textPaint != null) {
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            int height = getLayoutParams().height;
            int y = (height + fontMetrics.bottom - fontMetrics.top) / 2;
            if (!TextUtils.isEmpty(mProgressText)) {
                textPaint.setTextSize(mProgressTextSize);
                textPaint.setColor(mProgressTextColor);
                if (mProgressTextStyle == TEXT_STYLE_BOLD) {
                    textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                }
                canvas.drawText(mProgressText, centerX, y, textPaint);
            }
            if (!TextUtils.isEmpty(mDescText)) {
                textPaint.setTextSize(mDescTextSize);
                textPaint.setColor(mDescTextColor);
                //canvas.drawText(mDescText, centerX, y + mLineGap, textPaint);
                canvas.drawText(mDescText, centerX, centerY + mLineGap/2, textPaint);
            }
        }

    }

    public void setProgress(int progress) {
        if (progress >=0 && progress <= 100) {
            this.progress = progress;
            this.mProgressText = progress + "%";
            invalidate();
        }
    }

    public void setProgress(int progress,int text_color) {
        if (progress >=0 && progress <= 100) {
            this.progress = progress;
            this.mProgressText = progress + "%";
            //this.mDescTextColor = text_color;
            this.mProgressTextColor = text_color;
            invalidate();
            //LogUtil.i("out","设置进度==="+this.mProgressText+"_____"+this.progress+"______"+progress+"_字体颜色___"+text_color+"_____"+mProgressTextColor);
        }
    }

    public void setDescText(String desc) {
        this.mDescText = desc;
        invalidate();
    }

}