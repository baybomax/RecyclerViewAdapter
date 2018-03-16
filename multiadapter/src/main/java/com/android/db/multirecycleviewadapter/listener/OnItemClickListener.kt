package com.android.db.multirecycleviewadapter.listener

import android.view.View
import android.widget.SimpleAdapter

/**
 * Item click listener
 *
 * <p>
 * <p>
 * A convenience class to extend when you only want to OnItemClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link SimpleClickListener}
 * Created by DengBo on 16/03/2018.
 */
abstract class OnItemClickListener: SimpleClickListener() {
    fun onItemClick(adapter: SimpleAdapter, view: View, position: Int) {
        onSimpleItemClick(adapter, view, position)
    }

    fun onItemLongClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    fun onItemChildClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    fun onItemChildLongClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    abstract fun onSimpleItemClick(adapter: SimpleAdapter, view: View, position: Int)
}
