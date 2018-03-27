package com.android.db.multirecycleviewadapter.loadmore


import com.android.db.multirecycleviewadapter.R
import com.android.db.multirecycleviewadapter.view.LoadMoreView

class CustomLoadMoreView : LoadMoreView() {

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
