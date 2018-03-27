package com.android.db.multirecycleviewadapter.entity

import java.io.Serializable

/**
 * Section
 *
 * Created by DengBo on 15/03/2018.
 */

abstract class Section<T, H>(var header: H? = null,
                                     var entity: T? = null): Serializable {

    var isHeader: Boolean = false
        protected set
        get() = null != header

}
