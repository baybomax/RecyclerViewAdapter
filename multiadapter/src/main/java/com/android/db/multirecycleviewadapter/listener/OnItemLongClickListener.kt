package com.android.db.multirecycleviewadapter.listener

import android.view.View
import android.widget.SimpleAdapter

/**
 *
 * Created by DengBo on 16/03/2018.
 */

abstract class OnItemLongClickListener: SimpleClickListener() {
    fun onItemClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    fun onItemLongClick(adapter: SimpleAdapter, view: View, position: Int) {
        onSimpleItemLongClick(adapter, view, position)
    }

    fun onItemChildClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    fun onItemChildLongClick(adapter: SimpleAdapter, view: View, position: Int) {}

    abstract fun onSimpleItemLongClick(adapter: SimpleAdapter, view: View, position: Int)
}
