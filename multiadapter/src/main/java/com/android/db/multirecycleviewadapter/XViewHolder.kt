package com.android.db.multirecycleviewadapter

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Base view holder
 *
 * Created by DengBo on 15/03/2018.
 */

open class XViewHolder(private val root: View): RecyclerView.ViewHolder(root) {

    protected val views = SparseArray<View>()

    /**
     * Find view from 'views' or 'rootView'
     *
     * @param viewId the item view resource id
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: View> getView(@IdRes viewId: Int): T? {
        var child = views.get(viewId)
        if (null == child) {
            child = (root.findViewById<T>(viewId))?.apply {
                views.put(viewId, this)
            }
        }
        return child as T?
    }

    /**
     * Set specify view text content
     *
     * @param viewId the item view resource id
     * @param text the content sequence
     */
    open fun setText(@IdRes viewId: Int, text: CharSequence): XViewHolder {
        getView<TextView>(viewId)?.text = text
        return this
    }

    open fun setText(@IdRes viewId: Int, @StringRes strId: Int): XViewHolder {
        getView<TextView>(viewId)?.setText(strId)
        return this
    }

    /**
     * Set specify view image resource
     *
     * @param viewId the item view resource id
     * @param resId the image resource id
     */
    open fun setImageResource(@IdRes viewId: Int, resId: Int): XViewHolder {
        getView<ImageView>(viewId)?.setImageResource(resId)
        return this
    }

    /**
     * Set specify view visibility
     *
     * @param viewId the item view resource id
     * @param visible visibleOrInvisible
     */
    open fun setViewVisibleOrInVisible(@IdRes viewId: Int, visible: Boolean): XViewHolder {
        getView<View>(viewId)?.visibility = visible.visibleOrInvisible
        return this
    }

    /**
     * Set specify view visibility
     *
     * @param viewId the item view resource id
     * @param visible visibleOrGone
     */
    open fun setViewVisibleOrGone(@IdRes viewId: Int, visible: Boolean): XViewHolder {
        getView<View>(viewId)?.visibility = visible.visibleOrGone
        return this
    }

    /**
     * Set specify view onClickListener
     *
     * @param viewId the item view resource id
     * @param onClick
     */
    open fun setOnClickListener(@IdRes viewId: Int, onClick: (View)->Unit): XViewHolder {
        getView<View>(viewId)?.setOnClickListener {
            onClick(it)
        }
        return this
    }

    open fun setOnLongClickListener(@IdRes viewId: Int, onClick: (View)->Boolean): XViewHolder {
        getView<View>(viewId)?.setOnLongClickListener {
            onClick(it)
        }
        return this
    }

}
