package com.android.db.multirecycleviewadapter.util

import android.support.annotation.LayoutRes
import android.util.SparseIntArray

/**
 * Multi type delegate
 *
 * Created by DengBo on 15/03/2018.
 */

abstract class MultiTypeDelegate<T>() {

    companion object {
        private val DEFAULT_VIEW_TYPE = -0xff
        private val TYPE_NOT_FOUND = -404
    }
    private lateinit var layouts: SparseIntArray
    private var autoMode: Boolean = false
    private var selfMode: Boolean = false

    constructor(layouts: SparseIntArray): this() {
        this.layouts = layouts
    }

    fun getDefItemViewType(data: List<T>, position: Int): Int {
        val item = if (position >= 0 && position < data.size) data[position] else null
        return if (item != null) getItemType(item) else DEFAULT_VIEW_TYPE
    }

    /**
     * get the item type from specific entity.
     *
     * @param t entity
     * @return item type
     */
    protected abstract fun getItemType(t: T): Int

    fun getLayoutId(viewType: Int): Int {
        return layouts.get(viewType, TYPE_NOT_FOUND)
    }

    private fun addItemType(type: Int, @LayoutRes layoutResId: Int) {
        layouts.put(type, layoutResId)
    }

    /**
     * auto increase type vale, start from 0.
     *
     * @param layoutResIds layout id arrays
     * @return MultiTypeDelegate
     */
    fun registerItemTypeAutoIncrease(@LayoutRes vararg layoutResIds: Int): MultiTypeDelegate<*> {
        autoMode = true
        checkMode(selfMode)
        for (i in layoutResIds.indices) {
            addItemType(i, layoutResIds[i])
        }
        return this
    }

    /**
     * set your own type one by one.
     *
     * @param type        type value
     * @param layoutResId layout id
     * @return MultiTypeDelegate
     */
    fun registerItemType(type: Int, @LayoutRes layoutResId: Int): MultiTypeDelegate<*> {
        selfMode = true
        checkMode(autoMode)
        addItemType(type, layoutResId)
        return this
    }

    private fun checkMode(mode: Boolean) {
        if (mode) {
            throw RuntimeException("Don't mess two register mode")
        }
    }

}
