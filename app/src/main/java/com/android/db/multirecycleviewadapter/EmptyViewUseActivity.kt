package com.android.db.multirecycleviewadapter

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

import com.android.db.multirecycleviewadapter.adapter.QuickAdapter
import com.android.db.multirecycleviewadapter.base.BaseActivity
import com.android.db.multirecycleviewadapter.data.DataServer

class EmptyViewUseActivity : BaseActivity(), View.OnClickListener {
    private var mRecyclerView: RecyclerView? = null
    private var mQuickAdapter: QuickAdapter? = null
    private var notDataView: View? = null
    private var errorView: View? = null

    private var mError = true
    private var mNoData = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackBtn()
        setTitle("EmptyView Use")
        setContentView(R.layout.activity_empty_view_use)
        findViewById<View>(R.id.btn_reset).setOnClickListener(this)
        mRecyclerView = findViewById(R.id.rv_list)
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)

        notDataView = layoutInflater.inflate(R.layout.empty_view, mRecyclerView!!.parent as ViewGroup, false)
        notDataView!!.setOnClickListener { onRefresh() }
        errorView = layoutInflater.inflate(R.layout.error_view, mRecyclerView!!.parent as ViewGroup, false)
        errorView!!.setOnClickListener { onRefresh() }
        initAdapter()
        onRefresh()
    }

    private fun initAdapter() {
        mQuickAdapter = QuickAdapter(0)
        mRecyclerView!!.adapter = mQuickAdapter
    }

    override fun onClick(v: View) {
        mError = true
        mNoData = true
        mQuickAdapter?.notify(listOf(), true)
        onRefresh()
    }

    private fun onRefresh() {
        mQuickAdapter?.setEmptyView(R.layout.loading_view, mRecyclerView!!.parent as ViewGroup)
        Handler().postDelayed({
            if (mError) {
                mQuickAdapter!!.setEmptyView(errorView!!)
                mError = false
            } else {
                if (mNoData) {
                    mQuickAdapter!!.setEmptyView(notDataView!!)
                    mNoData = false
                } else {
                    mQuickAdapter?.notify(DataServer.getSampleData(10))
                }
            }
        }, 1000)
    }
}
