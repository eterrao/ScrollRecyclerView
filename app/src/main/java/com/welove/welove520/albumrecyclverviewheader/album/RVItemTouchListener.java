package com.welove.welove520.albumrecyclverviewheader.album;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * Created by Raomengyang on 17-9-11.
 * Email    : ericrao@welove-inc.com
 * Desc     :
 * Version  : 1.0
 */

public class RVItemTouchListener implements RecyclerView.OnItemTouchListener {

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
