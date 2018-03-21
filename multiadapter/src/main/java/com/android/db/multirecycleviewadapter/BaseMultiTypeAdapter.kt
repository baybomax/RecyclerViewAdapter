package com.android.db.multirecycleviewadapter

import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.util.SparseIntArray
import android.view.ViewGroup
import com.android.db.multirecycleviewadapter.entity.IExpandable
import com.android.db.multirecycleviewadapter.entity.MultiType

/**
 * Base multi item adapter
 *
 * Created by DengBo on 16/03/2018.
 */

abstract class BaseMultiTypeAdapter<T: MultiType, K: BaseViewHolder>
/**
 * Same as QuickAdapter#QuickAdapter(Context,int) but with
 * some initialization data.
 *
 * @param data A new list is created out of this one to avoid mutable list
 */
(data: List<T>) : BaseAdapter<T, K>(0, data) {

    /**
     * layouts indexed with their types
     */
    private var layouts: SparseIntArray? = null

    companion object {
        private val DEFAULT_VIEW_TYPE = -0xff
        val TYPE_NOT_FOUND = -404
    }

    override fun getDefItemViewType(position: Int): Int {
        return if (position >= 0 && position < mData.size) {
            mData[position].itemType
        } else DEFAULT_VIEW_TYPE
    }

    protected fun setDefaultViewTypeLayout(@LayoutRes layoutResId: Int) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): K {
        return createBaseViewHolder(parent, getLayoutId(viewType))
    }

    private fun getLayoutId(viewType: Int): Int {
        return layouts?.get(viewType, TYPE_NOT_FOUND) ?: TYPE_NOT_FOUND
    }

    protected fun addItemType(type: Int, @LayoutRes layoutResId: Int) {
        if (layouts == null) {
            layouts = SparseIntArray()
        }
        layouts?.put(type, layoutResId)
    }


    override fun remove(@IntRange(from = 0L) position: Int) {
        if (position < 0 || position >= mData.size) return

        val entity = mData[position]
        if (entity is IExpandable<*>) {
            removeAllChild(entity as IExpandable<*>, position)
        }
        removeDataFromParent(entity)
        super.remove(position)
    }

    protected fun removeAllChild(parent: IExpandable<*>, parentPosition: Int) {
        if (parent.expandable) {
            val childChildren = parent.subItems
            if (childChildren.size == 0) return

            val childSize = childChildren.size
            for (i in 0 until childSize) {
                remove(parentPosition + 1)
            }
        }
    }

    protected fun removeDataFromParent(child: T) {
        val position = getParentPosition(child)
        if (position >= 0) {
            val parent = mData[position] as IExpandable<*>
            parent.subItems.remove(child)
        }
    }

}
