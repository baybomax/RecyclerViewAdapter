package com.android.db.multirecycleviewadapter.view

import com.android.db.multirecycleviewadapter.R

/**
 * Simple load more view
 *
 * Created by DengBo on 15/03/2018.
 */

class SimpleLoadMoreView: LoadMoreView() {

    override fun getLayoutId(): Int {
        return R.layout.view_load_more
    }

    override fun getLoadingViewId(): Int {
        return R.id.load_more_loading_view
    }

    override fun getLoadFailViewId(): Int {
        return R.id.load_more_load_fail_view
    }

    override fun getLoadEndViewId(): Int {
        return R.id.load_more_load_end_view
    }
}
