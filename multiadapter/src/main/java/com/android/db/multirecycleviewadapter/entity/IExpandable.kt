package com.android.db.multirecycleviewadapter.entity

/**
 * Expandable interface
 *
 * Created by DengBo on 15/03/2018.
 */

interface IExpandable<T> {

    /**
     * The expandable
     */
    var expandable: Boolean

    /**
     * The subItems list
     */
    var subItems: MutableList<T>


    /**
     * The item level
     */
    var level: Int
}
