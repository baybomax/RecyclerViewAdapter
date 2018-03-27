package com.android.db.multirecycleviewadapter.ientity

import com.android.db.multirecycleviewadapter.entity.MultiType

class MultipleItem : MultiType {
    override var itemType: Int
        get() = type
        set(value) {}
    private var type = 0
    var spanSize: Int = 0
    var content: String? = null

    constructor(itemType: Int, spanSize: Int, content: String) {
        type = itemType
        this.spanSize = spanSize
        this.content = content
    }

    constructor(itemType: Int, spanSize: Int) {
        type = itemType
        this.spanSize = spanSize
    }

    companion object {
        const val TEXT = 1
        const val IMG = 2
        const val IMG_TEXT = 3
        const val TEXT_SPAN_SIZE = 3
        const val IMG_SPAN_SIZE = 1
        const val IMG_TEXT_SPAN_SIZE = 4
        const val IMG_TEXT_SPAN_SIZE_MIN = 2
    }
}
