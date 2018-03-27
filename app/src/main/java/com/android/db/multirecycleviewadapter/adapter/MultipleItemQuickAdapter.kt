package com.android.db.multirecycleviewadapter.adapter

import android.content.Context

import com.android.db.multirecycleviewadapter.BaseMultiTypeAdapter
import com.android.db.multirecycleviewadapter.BaseViewHolder
import com.android.db.multirecycleviewadapter.R
import com.android.db.multirecycleviewadapter.ientity.MultipleItem

class MultipleItemQuickAdapter(context: Context, data: List<MultipleItem>)
    : BaseMultiTypeAdapter<MultipleItem, BaseViewHolder>(data) {

    init {
        addItemType(MultipleItem.TEXT, R.layout.item_text_view)
        addItemType(MultipleItem.IMG, R.layout.item_image_view)
        addItemType(MultipleItem.IMG_TEXT, R.layout.item_img_text_view)
    }

    override fun convert(helper: BaseViewHolder, item: MultipleItem?) {
        when (helper.itemViewType) {
            MultipleItem.TEXT -> helper.setText(R.id.tv, item!!.content!!)
            MultipleItem.IMG_TEXT -> when (helper.layoutPosition % 2) {
                0 -> helper.setImageResource(R.id.iv, R.mipmap.animation_img1)
                1 -> helper.setImageResource(R.id.iv, R.mipmap.animation_img2)
            }
        }
    }

}
