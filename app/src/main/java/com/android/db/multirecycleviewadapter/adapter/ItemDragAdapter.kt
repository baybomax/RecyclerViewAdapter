package com.android.db.multirecycleviewadapter.adapter

import com.android.db.multirecycleviewadapter.BaseItemDraggableAdapter
import com.android.db.multirecycleviewadapter.BaseViewHolder
import com.android.db.multirecycleviewadapter.R

class ItemDragAdapter(data: List<String>)
    : BaseItemDraggableAdapter<String, BaseViewHolder>(R.layout.item_draggable_view, data) {

    override fun convert(helper: BaseViewHolder, item: String?) {
        when (helper.layoutPosition % 3) {
            0 -> helper.setImageResource(R.id.iv_head, R.mipmap.head_img0)
            1 -> helper.setImageResource(R.id.iv_head, R.mipmap.head_img1)
            2 -> helper.setImageResource(R.id.iv_head, R.mipmap.head_img2)
        }
        if (item != null) {
            helper.setText(R.id.tv, item)
        }
    }
}
