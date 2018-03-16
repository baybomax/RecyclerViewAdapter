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
        val STATUS_DEFAULT = 1
        val STATUS_LOADING = 2
        val STATUS_FAIL = 3
        val STATUS_END = 4
    }

    private var mLoadMoreStatus = STATUS_DEFAULT
    private var mLoadMoreEndGone = false

    fun setLoadMoreStatus(loadMoreStatus: Int) {
        this.mLoadMoreStatus = loadMoreStatus
    }

    fun getLoadMoreStatus(): Int {
        return mLoadMoreStatus
    }

    fun convert(holder: BaseViewHolder) {
        when (mLoadMoreStatus) {
            STATUS_LOADING -> {
                visibleLoading(holder, true)
                visibleLoadFail(holder, false)
                visibleLoadEnd(holder, false)
            }
            STATUS_FAIL -> {
                visibleLoading(holder, false)
                visibleLoadFail(holder, true)
                visibleLoadEnd(holder, false)
            }
            STATUS_END -> {
                visibleLoading(holder, false)
                visibleLoadFail(holder, false)
                visibleLoadEnd(holder, true)
            }
            STATUS_DEFAULT -> {
                visibleLoading(holder, false)
                visibleLoadFail(holder, false)
                visibleLoadEnd(holder, false)
            }
        }
    }

    private fun visibleLoading(holder: BaseViewHolder, visible: Boolean) {
        holder.setViewVisibleOrGone(getLoadingViewId(), visible)
    }

    private fun visibleLoadFail(holder: BaseViewHolder, visible: Boolean) {
        holder.setViewVisibleOrGone(getLoadFailViewId(), visible)
    }

    private fun visibleLoadEnd(holder: BaseViewHolder, visible: Boolean) {
        val loadEndViewId = getLoadEndViewId()
        if (loadEndViewId != 0) {
            holder.setViewVisibleOrGone(loadEndViewId, visible)
        }
    }

    fun setLoadMoreEndGone(loadMoreEndGone: Boolean) {
        this.mLoadMoreEndGone = loadMoreEndGone
    }

    fun isLoadEndMoreGone(): Boolean {
        return if (getLoadEndViewId() == 0) {
            true
        } else mLoadMoreEndGone
    }

    /**
     * No more data is hidden
     *
     * @return true for no more data hidden load more
     */
    @Deprecated("Use {@link BaseAdapter#loadMoreEnd(boolean)} instead.")
    fun isLoadEndGone(): Boolean {
        return mLoadMoreEndGone
    }

    /**
     * load more layout
     *
     * @return
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * loading view
     *
     * @return
     */
    @IdRes
    protected abstract fun getLoadingViewId(): Int

    /**
     * load fail view
     *
     * @return
     */
    @IdRes
    protected abstract fun getLoadFailViewId(): Int

    /**
     * load end view, you can return 0
     *
     * @return
     */
    @IdRes
    protected abstract fun getLoadEndViewId(): Int
}
