package com.android.db.multirecycleviewadapter.listener

import android.view.View
import com.android.db.multirecycleviewadapter.BaseAdapter

/**
 *
 * Created by DengBo on 16/03/2018.
 */
abstract class OnItemChildLongClickListener: SimpleClickListener() {
    override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemChildClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemChildLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        onSimpleItemChildLongClick(adapter, view, position)
    }

    abstract fun onSimpleItemChildLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int)
}
