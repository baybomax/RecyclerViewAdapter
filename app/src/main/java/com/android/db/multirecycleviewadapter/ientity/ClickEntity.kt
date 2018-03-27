package com.android.db.multirecycleviewadapter.ientity

import com.android.db.multirecycleviewadapter.entity.MultiType

class ClickEntity(var Type: Int): MultiType {

    override var itemType: Int = 0
        get() = Type

    companion object {
        const val CLICK_ITEM_VIEW = 1
        const val CLICK_ITEM_CHILD_VIEW = 2
        const val LONG_CLICK_ITEM_VIEW = 3
        const val LONG_CLICK_ITEM_CHILD_VIEW = 4
        const val NEST_CLICK_ITEM_CHILD_VIEW = 5
    }
}
