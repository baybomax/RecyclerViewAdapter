package com.android.db.multirecycleviewadapter.listener

import android.view.View
import com.android.db.multirecycleviewadapter.BaseAdapter

/**
 *
 * Created by DengBo on 16/03/2018.
 */

abstract class OnItemLongClickListener: SimpleClickListener() {
    override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        onSimpleItemLongClick(adapter, view, position)
    }

    override fun onItemChildClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemChildLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {}

    abstract fun onSimpleItemLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int)
}
