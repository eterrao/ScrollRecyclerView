package com.welove.welove520.albumrecyclverviewheader.album;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Raomengyang on 17-9-7.
 * Email    : ericrao@welove-inc.com
 * Desc     :
 * Version  : 1.0
 */

public class RelativeLayoutCompat extends RelativeLayout {
    public RelativeLayoutCompat(Context context) {
        super(context);
    }

    public RelativeLayoutCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeLayoutCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
