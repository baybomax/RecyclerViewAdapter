package com.android.db.multirecycleviewadapter.listener

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView

/**
 * Swipe listener
 *
 * Created by DengBo on 16/03/2018.
 */
interface OnItemSwipeListener {
    /**
     * Called when the swipe action start.
     */
    abstract fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder, pos: Int)

    /**
     * Called when the swipe action is over.
     * If you change the view on the start, you should reset is here, no matter the item has swiped or not.
     *
     * @param pos If the view is swiped, pos will be negative.
     */
    abstract fun clearView(viewHolder: RecyclerView.ViewHolder, pos: Int)

    /**
     * Called when item is swiped, the view is going to be removed from the adapter.
     */
    abstract fun onItemSwiped(viewHolder: RecyclerView.ViewHolder, pos: Int)

    /**
     * Draw on the empty edge when swipe moving
     *
     * @param canvas            the empty edge's canvas
     * @param viewHolder        The ViewHolder which is being interacted by the User or it was
     * interacted and simply animating to its original position
     * @param dX                The amount of horizontal displacement caused by user's action
     * @param dY                The amount of vertical displacement caused by user's action
     * @param isCurrentlyActive True if this view is currently being controlled by the user or
     * false it is simply animating back to its original state.
     */
    abstract fun onItemSwipeMoving(canvas: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, isCurrentlyActive: Boolean)
}
