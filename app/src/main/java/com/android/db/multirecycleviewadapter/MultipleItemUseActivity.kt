package com.android.db.multirecycleviewadapter

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.android.db.multirecycleviewadapter.adapter.MultipleItemQuickAdapter
import com.android.db.multirecycleviewadapter.base.BaseActivity
import com.android.db.multirecycleviewadapter.data.DataServer

class MultipleItemUseActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_item_use)
        setTitle("MultipleItem Use")
        setBackBtn()
        mRecyclerView = findViewById(R.id.rv_list)
        val data = DataServer.multipleItemData
        val multipleItemAdapter = MultipleItemQuickAdapter(this, data)
        val manager = GridLayoutManager(this, 4)
        mRecyclerView!!.layoutManager = manager
        multipleItemAdapter.setSpanSizeLookup(object : BaseAdapter.SpanSizeLookup {
            override fun getSpanSize(gridLayoutManager: GridLayoutManager, position: Int): Int {
                return data[position].spanSize
            }
        })
        mRecyclerView!!.adapter = multipleItemAdapter
    }


}
