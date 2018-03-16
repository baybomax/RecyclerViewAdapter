package com.android.db.multirecycleviewadapter.listener

import android.view.View
import android.widget.SimpleAdapter

/**
 * A convenience class to extend when you only want to OnItemChildClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * Created by DengBo on 16/03/2018.
 */
abstract class OnItemChildClickListener: SimpleClickListener() {
    fun onItemClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    fun onItemLongClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    fun onItemChildClick(adapter: SimpleAdapter, view: View, position: Int) {
        onSimpleItemChildClick(adapter, view, position)
    }

    fun onItemChildLongClick(adapter: SimpleAdapter, view: View, position: Int) {

    }

    abstract fun onSimpleItemChildClick(adapter: SimpleAdapter, view: View, position: Int)
}
