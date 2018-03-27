package com.android.db.multirecycleviewadapter.ientity

import com.android.db.multirecycleviewadapter.adapter.ExpandableItemAdapter
import com.android.db.multirecycleviewadapter.entity.AbstractExpandable
import com.android.db.multirecycleviewadapter.entity.MultiType

class Level1Item(var title: String,
                 var subTitle: String) : AbstractExpandable<Person>(), MultiType {

    override var itemType: Int = 0
        get() = ExpandableItemAdapter.TYPE_LEVEL_1

    override var level: Int = 1

}
