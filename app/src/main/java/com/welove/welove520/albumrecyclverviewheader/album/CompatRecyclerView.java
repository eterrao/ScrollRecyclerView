package com.welove.welove520.albumrecyclverviewheader.album;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Raomengyang on 17-9-12.
 * Email    : ericrao@welove-inc.com
 * Desc     :
 * Version  : 1.0
 */

public class CompatRecyclerView extends RecyclerView {

    private int mMeasureHeight;
    private int headerHeight = 50;

    public CompatRecyclerView(Context context) {
        super(context);
    }

    public CompatRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
//        super.onMeasure(widthSpec, heightSpec);
//        if (Math.abs(mMeasureHeight) > 0) {
//            setMeasuredDimension(widthSpec, mMeasureHeight);
//        } else {
//            setMeasuredDimension(widthSpec, heightSpec);
//        }


//        int height = getHeight() + headerHeight;
//        height = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, heightSpec);
        if (mMeasureHeight > 0) {
            setMeasuredDimension(widthSpec, mMeasureHeight);
        } else {
            setMeasuredDimension(widthSpec, heightSpec);
        }
        WeloveLog.debug("onMeasure ==> width spec = " + widthSpec + " , height = " + heightSpec
                + " ,this.getWidth() = " + this.getWidth() + " getHeight = " + this.getHeight() + " measure height = " + mMeasureHeight);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        WeloveLog.debug("onDraw ===> ");
    }

    public void setMeasureHeight(int measureHeight) {
        this.mMeasureHeight = measureHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }
}
