package com.android.db.multirecycleviewadapter

import android.view.View

/**
 *
 * Created by DengBo on 15/03/2018.
 */

val Boolean.visibleOrInvisible: Int
    get() = if (this) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }

val Boolean.visibleOrGone: Int
    get() = if (this) {
        View.VISIBLE
    } else {
        View.GONE
    }
