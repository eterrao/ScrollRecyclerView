package com.welove.welove520.albumrecyclverviewheader.album;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Raomengyang on 17-9-11.
 * Email    : ericrao@welove-inc.com
 * Desc     :
 * Version  : 1.0
 */

public class RVItemTouchHelper extends ItemTouchHelper {
    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #attachToRecyclerView(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    public RVItemTouchHelper(Callback callback) {
        super(callback);
    }


}
