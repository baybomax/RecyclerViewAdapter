package com.android.db.multirecycleviewadapter

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.db.multirecycleviewadapter.adapter.PullToRefreshAdapter
import com.android.db.multirecycleviewadapter.animation.SlideInLeftAnimation
import com.android.db.multirecycleviewadapter.base.BaseActivity
import com.android.db.multirecycleviewadapter.data.DataServer
import com.android.db.multirecycleviewadapter.ientity.Status
import com.android.db.multirecycleviewadapter.listener.LoadMoreRequestListener
import com.android.db.multirecycleviewadapter.listener.OnItemClickListener
import com.android.db.multirecycleviewadapter.loadmore.CustomLoadMoreView


internal interface RequestCallBack {
    fun success(data: List<Status>)

    fun fail(e: Exception)
}

internal class Request(private val mPage: Int, private val mCallBack: RequestCallBack) : Thread() {
    private val mHandler: Handler

    init {
        mHandler = Handler(Looper.getMainLooper())
    }

    override fun run() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
        }

        if (mPage == 2 && mFirstError) {
            mFirstError = false
            mHandler.post { mCallBack.fail(RuntimeException("fail")) }
        } else {
            var size = PAGE_SIZE
            if (mPage == 1) {
                if (mFirstPageNoMore) {
                    size = 1
                }
                mFirstPageNoMore = !mFirstPageNoMore
                if (!mFirstError) {
                    mFirstError = true
                }
            } else if (mPage == 4) {
                size = 1
            }

            val dataSize = size
            mHandler.post { mCallBack.success(DataServer.getSampleData(dataSize)) }
        }
    }

    companion object {
        private val PAGE_SIZE = 6

        private var mFirstPageNoMore: Boolean = false
        private var mFirstError = true
    }
}

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class PullToRefreshUseActivity : BaseActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mAdapter: PullToRefreshAdapter? = null

    private var mNextRequestPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRecyclerView = findViewById(R.id.rv_list)
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout)
        mSwipeRefreshLayout!!.setColorSchemeColors(Color.rgb(47, 223, 189))
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        setTitle("Pull TO Refresh Use")
        setBackBtn()
        initAdapter()
        addHeadView()
        initRefreshLayout()
        mSwipeRefreshLayout!!.isRefreshing = true
        refresh()
    }

    private fun initAdapter() {
        mAdapter = PullToRefreshAdapter()
        mAdapter?.setLoadMoreRequestListener(mRecyclerView!!, object : LoadMoreRequestListener {
            override fun onLoadMoreRequest() {
                loadMore()
            }
        })
        mAdapter!!.animation = SlideInLeftAnimation()
        //        mAdapter.setPreLoadNumber(3);
        mRecyclerView!!.adapter = mAdapter

        mRecyclerView!!.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
                Toast.makeText(this@PullToRefreshUseActivity, Integer.toString(position), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun addHeadView() {
        val headView = layoutInflater.inflate(R.layout.head_view, mRecyclerView!!.parent as ViewGroup, false)
        headView.findViewById<View>(R.id.iv).visibility = View.GONE
        (headView.findViewById(R.id.tv) as TextView).text = "change load view"
        headView.setOnClickListener(View.OnClickListener {
            mAdapter!!.notify(listOf(), true)
            mAdapter?.loadMoreView = CustomLoadMoreView()
            mRecyclerView!!.adapter = mAdapter
            Toast.makeText(this@PullToRefreshUseActivity, "change complete", Toast.LENGTH_LONG).show()

            mSwipeRefreshLayout!!.isRefreshing = true
            refresh()
        })
        mAdapter!!.addHeaderView(headView)
    }

    private fun initRefreshLayout() {
        mSwipeRefreshLayout!!.setOnRefreshListener { refresh() }
    }

    private fun refresh() {
        mNextRequestPage = 1
        mAdapter?.isLoadMoreViewEnable = false//这里的作用是防止下拉刷新的时候还可以上拉加载
        Request(mNextRequestPage, object : RequestCallBack {
            override fun success(data: List<Status>) {
                setData(true, data)
                mAdapter?.isLoadMoreViewEnable = true
                mSwipeRefreshLayout!!.isRefreshing = false
            }

            override fun fail(e: Exception) {
                Toast.makeText(this@PullToRefreshUseActivity, R.string.network_err, Toast.LENGTH_LONG).show()
                mAdapter?.isLoadMoreViewEnable = true
                mSwipeRefreshLayout!!.isRefreshing = false
            }
        }).start()
    }

    private fun loadMore() {
        Request(mNextRequestPage, object : RequestCallBack {
            override fun success(data: List<Status>) {
                setData(false, data)
            }

            override fun fail(e: Exception) {
                mAdapter!!.loadMoreFail()
                Toast.makeText(this@PullToRefreshUseActivity, R.string.network_err, Toast.LENGTH_LONG).show()
            }
        }).start()
    }

    private fun setData(isRefresh: Boolean, data: List<Status>?) {
        mNextRequestPage++
        val size = data?.size ?: 0
        if (isRefresh) {
            if (data != null) {
                mAdapter?.notify(data, true)
            }
        } else {
            if (size > 0) {
                if (data != null) {
                    mAdapter?.notify(data)
                }
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter!!.loadMoreEnd(isRefresh)
            Toast.makeText(this, "no more data", Toast.LENGTH_SHORT).show()
        } else {
            mAdapter!!.loadMoreComplete()
        }
    }

    companion object {

        private val PAGE_SIZE = 6
    }
}
