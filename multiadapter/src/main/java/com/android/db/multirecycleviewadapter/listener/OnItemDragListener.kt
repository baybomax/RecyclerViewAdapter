package com.android.db.multirecycleviewadapter.listener

import android.support.v7.widget.RecyclerView

/**
 * Drag listener
 *
 * Created by DengBo on 16/03/2018.
 */
interface OnItemDragListener {
    abstract fun onItemDragStart(viewHolder: RecyclerView.ViewHolder, pos: Int)

    abstract fun onItemDragMoving(source: RecyclerView.ViewHolder, from: Int, target: RecyclerView.ViewHolder, to: Int)

    abstract fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int)
}
