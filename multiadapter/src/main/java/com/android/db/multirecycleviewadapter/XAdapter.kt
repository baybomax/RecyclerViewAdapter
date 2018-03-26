package com.android.db.multirecycleviewadapter

import android.support.v7.widget.RecyclerView

/**
 * Base adapter
 *
 * Created by DengBo on 15/03/2018.
 */

abstract class XAdapter<T, R: XViewHolder>: RecyclerView.Adapter<R>() {

    open var dataSrc = mutableListOf<T>()

    /**
     * NotifyDataSetChanged when load more data
     *
     * @param _data more data
     */
    protected open fun notifyDataSetLoadMore(_data: List<T>) {
        dataSrc.addAll(_data)
        notifyDataSetChanged()
    }

    /**
     * NotifyDataSetChanged when refresh data
     *
     * @param _data refresh data
     */
    protected open fun notifyDataSetRefresh(_data: List<T>) {
        dataSrc.clear()
        dataSrc.addAll(_data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataSrc.size
    }

}
