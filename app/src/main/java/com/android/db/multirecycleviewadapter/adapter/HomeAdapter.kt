package com.android.db.multirecycleviewadapter.adapter

import com.android.db.multirecycleviewadapter.BaseAdapter
import com.android.db.multirecycleviewadapter.BaseViewHolder
import com.android.db.multirecycleviewadapter.R
import com.android.db.multirecycleviewadapter.ientity.HomeItem

class HomeAdapter(layoutResId: Int, data: List<HomeItem>)
    : BaseAdapter<HomeItem, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: HomeItem?) {
        item?.apply {
            title?.let { helper.setText(R.id.text, it) }
            helper.setImageResource(R.id.icon, item.imageResource)
        }
    }
}
