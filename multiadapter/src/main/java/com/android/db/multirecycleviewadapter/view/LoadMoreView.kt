package com.android.db.multirecycleviewadapter.view

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import com.android.db.multirecycleviewadapter.BaseViewHolder

/**
 * Load more view
 *
 * Created by DengBo on 15/03/2018.
 */

abstract class LoadMoreView {

    companion object {
        const val STATUS_DEFAULT = 1
        const val STATUS_LOADING = 2
        const val STATUS_FAIL    = 3
        const val STATUS_END     = 4
    }

    var loadMoreStatus = STATUS_DEFAULT

    var loadFailGone = false
        get() = if (getLoadFailViewId() <= 0) true else field

    var loadEndGone = false
        get() = if (getLoadEndViewId() <= 0) true else field

    /**
     * The method to convert view with this instance state.
     * @param holder
     */
    fun convert(holder: BaseViewHolder) {
        when (loadMoreStatus) {
            STATUS_LOADING -> {
                setViewVisibleOrGone(holder, true, false, false)
            }
            STATUS_FAIL -> {
                setViewVisibleOrGone(holder, false, true, false)
            }
            STATUS_END -> {
                setViewVisibleOrGone(holder, false, false, true)
            }
            STATUS_DEFAULT -> {
                setViewVisibleOrGone(holder, false, false, false)
            }
        }
    }

    /**
     * Set the visibility of load more view.
     * ###
     * @param holder [BaseViewHolder]
     * @param loadingVisible TRUE/FALSE, The visibility of loading view.
     * @param loadFailVisible TRUE/FALSE, The visibility of loading fail view.
     * @param loadEndVisible TRUE/FALSE, The visibility of loading end view.
     */
    private fun setViewVisibleOrGone(holder: BaseViewHolder,
                               loadingVisible: Boolean,
                               loadFailVisible: Boolean,
                               loadEndVisible: Boolean) {
        getLoadingViewId().let {
            if (it > 0) {
                holder.setViewVisibleOrGone(it, loadingVisible)
            }
        }
        getLoadFailViewId().let {
            if (it > 0) {
                holder.setViewVisibleOrGone(it, loadFailVisible)
            }
        }
        getLoadEndViewId().let {
            if (it > 0) {
                holder.setViewVisibleOrGone(it, loadEndVisible)
            }
        }
    }

    /**
     * load more layout
     *
     * @return the load more layout id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * loading view
     *
     * should be view id.
     *
     * @return the loading view id
     */
    @IdRes
    protected abstract fun getLoadingViewId(): Int

    /**
     * load fail view
     *
     * if you not need a load fail view, you can give
     * a negative value or zero
     *
     * @return negative/zero value or view id.
     */
    @IdRes
    protected abstract fun getLoadFailViewId(): Int

    /**
     * load end view
     *
     * if you not need a load end view, you can give
     * a negative value or zero
     *
     * @return negative/zero value or view id.
     */
    @IdRes
    protected abstract fun getLoadEndViewId(): Int
}
