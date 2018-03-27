package com.android.db.multirecycleviewadapter.ientity

import com.android.db.multirecycleviewadapter.entity.Section

class MySection : Section<Video, String> {
    var isMore: Boolean = false

    constructor(isHeader: Boolean, header: String, isMore: Boolean) {
        this.isHeader = isHeader
        this.header = header
        this.isMore = isMore
    }

    constructor(t: Video) : super(null, t) {}
}
