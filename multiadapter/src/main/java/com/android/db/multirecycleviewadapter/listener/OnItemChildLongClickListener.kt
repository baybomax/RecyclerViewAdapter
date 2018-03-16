package com.android.db.multirecycleviewadapter.listener

import android.view.View
import android.widget.SimpleAdapter

/**
 *
 * Created by DengBo on 16/03/2018.
 */
abstract class OnItemChildLongClickListener: SimpleClickListener() {
    fun onItemClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    fun onItemLongClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    fun onItemChildClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    fun onItemChildLongClick(adapter: SimpleAdapter, view: View, position: Int) {
        onSimpleItemChildLongClick(adapter, view, position)
    }

    abstract fun onSimpleItemChildLongClick(adapter: SimpleAdapter, view: View, position: Int)
}
