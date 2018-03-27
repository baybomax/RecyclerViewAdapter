package com.android.db.multirecycleviewadapter.adapter

import com.android.db.multirecycleviewadapter.BaseMultiTypeAdapter
import com.android.db.multirecycleviewadapter.BaseViewHolder
import com.android.db.multirecycleviewadapter.R
import com.android.db.multirecycleviewadapter.entity.MultiType
import com.android.db.multirecycleviewadapter.ientity.Level0Item
import com.android.db.multirecycleviewadapter.ientity.Level1Item
import com.android.db.multirecycleviewadapter.ientity.Person

class ExpandableItemAdapter
/**
 * @param data A new list is created out of this one to avoid mutable list
 */
(data: List<MultiType>) : BaseMultiTypeAdapter<MultiType, BaseViewHolder>(data) {

    init {
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0)
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1)
        addItemType(TYPE_PERSON, R.layout.item_expandable_lv2)
    }

    override fun convert(helper: BaseViewHolder, item: MultiType?) {
        when (helper.itemViewType) {
            TYPE_LEVEL_0 -> {
                when (helper.layoutPosition % 3) {
                    0 -> helper.setImageResource(R.id.iv_head, R.mipmap.head_img0)
                    1 -> helper.setImageResource(R.id.iv_head, R.mipmap.head_img1)
                    2 -> helper.setImageResource(R.id.iv_head, R.mipmap.head_img2)
                }
                (item as Level0Item?)?.apply {
                    helper.setText(R.id.title, title)
                            .setText(R.id.sub_title, subTitle)
                            .setImageResource(R.id.iv, if (expandable) R.mipmap.arrow_b else R.mipmap.arrow_r)
                    helper.itemView.setOnClickListener {
                        val pos = helper.adapterPosition
                        if (expandable) {
                            collapse(pos)
                        } else {
                            expand(pos)
                        }
                    }
                }
            }
            TYPE_LEVEL_1 -> {
                (item as Level1Item?)?.apply {
                    helper.setText(R.id.title, title)
                            .setText(R.id.sub_title, subTitle)
                            .setImageResource(R.id.iv, if (expandable) R.mipmap.arrow_b else R.mipmap.arrow_r)
                    helper.itemView.setOnClickListener {
                        val pos = helper.adapterPosition
                        if (expandable) {
                            collapse(pos, false)
                        } else {
                            expand(pos, false)
                        }
                    }
                    helper.itemView.setOnLongClickListener {
                        val pos = helper.adapterPosition
                        remove(pos)
                        true
                    }
                }
            }
            TYPE_PERSON -> {
                (item as Person?)?.apply {
                    helper.setText(R.id.tv, name + " parent pos: " + getParentPosition(this))
                    helper.itemView.setOnClickListener {
                        val pos = helper.adapterPosition
                        remove(pos)
                    }
                }
            }
        }
    }

    companion object {
        const val TYPE_LEVEL_0 = 0
        const val TYPE_LEVEL_1 = 1
        const val TYPE_PERSON  = 2
    }
}
