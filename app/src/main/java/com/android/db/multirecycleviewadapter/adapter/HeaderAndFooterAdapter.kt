package com.android.db.multirecycleviewadapter.adapter

import com.android.db.multirecycleviewadapter.BaseAdapter
import com.android.db.multirecycleviewadapter.BaseViewHolder
import com.android.db.multirecycleviewadapter.R
import com.android.db.multirecycleviewadapter.data.DataServer
import com.android.db.multirecycleviewadapter.ientity.Status

class HeaderAndFooterAdapter(dataSize: Int)
    : BaseAdapter<Status, BaseViewHolder>(R.layout.item_header_and_footer, DataServer.getSampleData(dataSize)) {

    override fun convert(helper: BaseViewHolder, item: Status?) {
        when (helper.layoutPosition % 3) {
            0 -> helper.setImageResource(R.id.iv, R.mipmap.animation_img1)
            1 -> helper.setImageResource(R.id.iv, R.mipmap.animation_img2)
            2 -> helper.setImageResource(R.id.iv, R.mipmap.animation_img3)
        }
    }

}
