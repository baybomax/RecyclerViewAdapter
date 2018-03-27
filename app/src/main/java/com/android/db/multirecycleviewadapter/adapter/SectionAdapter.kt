package com.android.db.multirecycleviewadapter.adapter

import com.android.db.multirecycleviewadapter.BaseSectionAdapter
import com.android.db.multirecycleviewadapter.BaseViewHolder
import com.android.db.multirecycleviewadapter.R
import com.android.db.multirecycleviewadapter.ientity.MySection

class SectionAdapter
/**
 * @param sectionHeadResId The section head layout id for each item
 * @param layoutResId      The layout resource id of each item.
 * @param data             A new list is created out of this one to avoid mutable list
 */
(layoutResId: Int, sectionHeadResId: Int, data: List<MySection>) : BaseSectionAdapter<MySection, BaseViewHolder>(layoutResId, sectionHeadResId, data) {

    override fun convertHead(helper: BaseViewHolder, item: MySection?) {
        helper.setText(R.id.header, item!!.header!!)
        helper.setViewVisibleOrInVisible(R.id.more, item.isMore)
        helper.addOnClickListener(R.id.more)
    }

    override fun convert(helper: BaseViewHolder, item: MySection?) {
        val video = item!!.entity
        when (helper.layoutPosition % 2) {
            0 -> helper.setImageResource(R.id.iv, R.mipmap.m_img1)
            1 -> helper.setImageResource(R.id.iv, R.mipmap.m_img2)
        }
        helper.setText(R.id.tv, video!!.name)
    }
}
