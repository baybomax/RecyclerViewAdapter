package com.android.db.multirecycleviewadapter.listener

import android.view.View
import com.android.db.multirecycleviewadapter.BaseAdapter

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

    override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        onSimpleItemClick(adapter, view, position)
    }

    override fun onItemLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemChildClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemChildLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {

    }

    abstract fun onSimpleItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int)
}
