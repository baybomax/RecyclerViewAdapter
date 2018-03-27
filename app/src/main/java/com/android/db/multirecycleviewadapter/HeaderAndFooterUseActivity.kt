package com.android.db.multirecycleviewadapter

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast

import com.android.db.multirecycleviewadapter.adapter.HeaderAndFooterAdapter
import com.android.db.multirecycleviewadapter.animation.AlphaInAnimation
import com.android.db.multirecycleviewadapter.base.BaseActivity
import com.android.db.multirecycleviewadapter.data.DataServer

class HeaderAndFooterUseActivity : BaseActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var headerAndFooterAdapter: HeaderAndFooterAdapter? = null

    private val removeHeaderListener: View.OnClickListener
        get() = View.OnClickListener { v -> headerAndFooterAdapter!!.removeHeaderView(v) }


    private val removeFooterListener: View.OnClickListener
        get() = View.OnClickListener { v -> headerAndFooterAdapter!!.removeFooterView(v) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackBtn()
        setTitle("HeaderAndFooter Use")

        setContentView(R.layout.activity_header_and_footer_use)
        mRecyclerView = findViewById(R.id.rv_list) as RecyclerView
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        initAdapter()

        val headerView = getHeaderView(0, View.OnClickListener {
            headerAndFooterAdapter!!.addHeaderView(getHeaderView(1, removeHeaderListener), 0) })
        headerAndFooterAdapter!!.addHeaderView(headerView)


        val footerView = getFooterView(0, View.OnClickListener { headerAndFooterAdapter!!.addFooterView(getFooterView(1, removeFooterListener), 0) })
        headerAndFooterAdapter!!.addFooterView(footerView, 0)

        mRecyclerView!!.adapter = headerAndFooterAdapter

    }


    private fun getHeaderView(type: Int, listener: View.OnClickListener): View {
        val view = layoutInflater.inflate(R.layout.head_view, mRecyclerView!!.parent as ViewGroup, false)
        if (type == 1) {
            val imageView = view.findViewById(R.id.iv) as ImageView
            imageView.setImageResource(R.mipmap.rm_icon)
        }
        view.setOnClickListener(listener)
        return view
    }

    private fun getFooterView(type: Int, listener: View.OnClickListener): View {
        val view = layoutInflater.inflate(R.layout.footer_view, mRecyclerView!!.parent as ViewGroup, false)
        if (type == 1) {
            val imageView = view.findViewById(R.id.iv) as ImageView
            imageView.setImageResource(R.mipmap.rm_icon)
        }
        view.setOnClickListener(listener)
        return view
    }

    private fun initAdapter() {
        headerAndFooterAdapter = HeaderAndFooterAdapter(PAGE_SIZE)
        headerAndFooterAdapter!!.animation = AlphaInAnimation()
        mRecyclerView!!.adapter = headerAndFooterAdapter
        headerAndFooterAdapter!!.onItemClickListener = object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
                (adapter as HeaderAndFooterAdapter).notify(DataServer.getSampleData(PAGE_SIZE), true)
                Toast.makeText(this@HeaderAndFooterUseActivity, "" + Integer.toString(position), Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private val PAGE_SIZE = 3
    }

}
