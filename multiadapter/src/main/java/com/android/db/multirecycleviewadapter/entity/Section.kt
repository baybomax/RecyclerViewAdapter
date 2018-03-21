package com.android.db.multirecycleviewadapter.entity

import java.io.Serializable

/**
 * Section
 *
 * Created by DengBo on 15/03/2018.
 */

abstract class Section<out T, out H>(val header: H? = null,
                                     val entity: T? = null): Serializable {

    var isHeader: Boolean = false
        private set
        get() = null != header

}
