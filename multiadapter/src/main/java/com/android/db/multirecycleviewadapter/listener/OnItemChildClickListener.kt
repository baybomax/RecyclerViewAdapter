package com.android.db.multirecycleviewadapter.listener

import android.view.View
import com.android.db.multirecycleviewadapter.BaseAdapter

/**
 * A convenience class to extend when you only want to OnItemChildClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * Created by DengBo on 16/03/2018.
 */
abstract class OnItemChildClickListener: SimpleClickListener() {
    override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemChildClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        onSimpleItemChildClick(adapter, view, position)
    }

    override fun onItemChildLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    abstract fun onSimpleItemChildClick(adapter: BaseAdapter<*, *>, view: View, position: Int)
}
