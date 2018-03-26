package com.android.db.multirecycleviewadapter

import android.view.ViewGroup
import com.android.db.multirecycleviewadapter.entity.Section

/**
 * Base section adapter
 *
 * Created by DengBo on 16/03/2018.
 */

abstract class BaseSectionAdapter<T: Section<*, *>, K: BaseViewHolder>
/**
 * Same as QuickAdapter#QuickAdapter(Context,int) but with
 * some initialization data.
 *
 * @param sectionHeadResId The section head layout id for each item
 * @param layoutResId      The layout resource id of each item.
 * @param data             A new list is created out of this one to avoid mutable list
 */
(layoutResId: Int, sectionHeadResId: Int, data: List<T>) : BaseAdapter<T, K>(layoutResId, data) {

    companion object {
        protected val SECTION_HEADER_VIEW = 0x00000444
    }

    protected var mSectionHeadResId: Int = sectionHeadResId

    override fun getDefItemViewType(position: Int): Int {
        return if (dataSrc[position].isHeader) SECTION_HEADER_VIEW else 0
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): K {
        return if (viewType == SECTION_HEADER_VIEW)
            createBaseViewHolder(getItemView(mSectionHeadResId, parent))
        else
            super.onCreateDefViewHolder(parent, viewType)
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || type == SECTION_HEADER_VIEW
    }

    override fun onBindViewHolder(holder: K, position: Int) {
        when (holder.itemViewType) {
            SECTION_HEADER_VIEW -> {
                setFullSpan(holder)
                convertHead(holder, get(position - headerLayoutCount))
            }
            else -> super.onBindViewHolder(holder, position)
        }
    }

    protected abstract fun convertHead(helper: K, item: T?)

}
