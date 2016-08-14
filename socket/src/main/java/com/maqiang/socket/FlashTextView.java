package com.maqiang.socket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by maqiang on 16/7/20.
 * 颜色过渡的TextView
 */
public class FlashTextView extends TextView {

    private Matrix mMatrix;
    private int mViewWidth=0;
    private float mTranslate=0f;
    private Paint mPaint;
    private LinearGradient mLinearGradient;

    public FlashTextView(Context context) {
        this(context,null);
    }

    public FlashTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlashTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mViewWidth==0){
            //获取视图的高
            mViewWidth = getMeasuredWidth();
            if(mViewWidth>0){
                //获取画笔
                mPaint = getPaint();
                //颜色渐变器 颜色红蓝CP
                mLinearGradient = new LinearGradient(0,0,mViewWidth,0,new int[]{Color.BLUE,Color.RED,
                Color.BLUE},null, Shader.TileMode.CLAMP);
                //给画笔设置着色器
                mPaint.setShader(mLinearGradient);
                //初始化一个矩阵
                mMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mMatrix!=null){
            //过渡带设置为视图宽度的五分之一
            mTranslate+=mViewWidth/5;
            if(mTranslate>2*mViewWidth){
                //当过渡到最后的时候 让回到初始化
                mTranslate = -mViewWidth;
            }
            //设置矩阵平移
            mMatrix.setTranslate(mTranslate,0);
            //给渐变器设置平移矩阵
            mLinearGradient.setLocalMatrix(mMatrix);
            //0.1S的延时操作
            postInvalidateDelayed(100);
        }
    }
}
