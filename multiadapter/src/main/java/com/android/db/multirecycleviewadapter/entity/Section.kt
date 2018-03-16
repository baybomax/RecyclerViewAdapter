package com.android.db.multirecycleviewadapter.entity

import java.io.Serializable

/**
 * Section
 *
 * Created by DengBo on 15/03/2018.
 */

abstract class Section<T, H>(): Serializable {

    var entity: T? = null

    var header: H? = null

    var isHeader: Boolean = false
        get() = null != header

    constructor(header: H? = null, entity: T? = null) : this() {
        this.header = header
        this.entity = entity
    }

}
