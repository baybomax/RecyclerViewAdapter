package com.android.db.multirecycleviewadapter.ientity

import com.android.db.multirecycleviewadapter.adapter.ExpandableItemAdapter
import com.android.db.multirecycleviewadapter.entity.MultiType

class Person(var name: String, var age: Int): MultiType {

    override var itemType: Int = 0
        get() = ExpandableItemAdapter.TYPE_PERSON

}
