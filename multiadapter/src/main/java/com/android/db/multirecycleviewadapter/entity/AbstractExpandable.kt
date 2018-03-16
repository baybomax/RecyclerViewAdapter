package com.android.db.multirecycleviewadapter.entity

/**
 * Abstract expandable
 *
 * Created by DengBo on 14/03/2018.
 */

abstract class AbstractExpandable<T>: IExpandable<T> {

    override var expandable: Boolean = false

    override var subItems: MutableList<T> = mutableListOf()

    fun hasSubItem(): Boolean {
        return subItems.isNotEmpty()
    }

    fun getSubItem(position: Int): T? {
        return if (hasSubItem() && subItems.size > position) {
            subItems[position]
        } else
            null
    }

    fun getSubItemPosition(subItem: T): Int {
        return subItems.indexOf(subItem)
    }

    fun addSubItem(subItem: T) {
        subItems.add(subItem)
    }

    fun addSubItem(position: Int, subItem: T) {
        if (position >= 0 && position < subItems.size) {
            subItems.add(position, subItem)
        } else {
            addSubItem(subItem)
        }
    }

    fun removeSubItem(subItem: T): Boolean {
        return subItems.remove(subItem)
    }

    fun removeSubItem(position: Int): T? {
        return if (position >= 0 && position < subItems.size) {
            subItems.removeAt(position)
        } else
            null
    }

    fun contains(subItem: T): Boolean {
        return subItems.contains(subItem)
    }
}
