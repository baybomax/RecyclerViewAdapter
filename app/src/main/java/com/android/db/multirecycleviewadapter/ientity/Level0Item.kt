package com.android.db.multirecycleviewadapter.ientity

import com.android.db.multirecycleviewadapter.adapter.ExpandableItemAdapter
import com.android.db.multirecycleviewadapter.entity.AbstractExpandable
import com.android.db.multirecycleviewadapter.entity.MultiType

class Level0Item(var title: String,
                 var subTitle: String) : AbstractExpandable<Level1Item>(), MultiType {

    override var itemType: Int = 0
        get() = ExpandableItemAdapter.TYPE_LEVEL_0

    override var level: Int = 0

}
