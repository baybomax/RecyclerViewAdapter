package com.android.db.multirecycleviewadapter


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.android.db.multirecycleviewadapter.adapter.HomeAdapter
import com.android.db.multirecycleviewadapter.ientity.HomeItem
import java.util.*

class HomeActivity : AppCompatActivity() {
    private var mDataList: ArrayList<HomeItem>? = null
    private var mRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        initData()
        initAdapter()
    }

    private fun initView() {
        mRecyclerView = findViewById(R.id.rv_list)
        mRecyclerView!!.layoutManager = GridLayoutManager(this, 2)
    }

    private fun initAdapter() {
        val homeAdapter = HomeAdapter(R.layout.home_item_view, mDataList!!)
        val top = layoutInflater.inflate(R.layout.top_view, mRecyclerView!!.parent as ViewGroup, false)
        homeAdapter.addHeaderView(top)
        homeAdapter.onItemClickListener = object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
                val intent = Intent(this@HomeActivity, ACTIVITY[position])
                startActivity(intent)
            }
        }

        mRecyclerView!!.adapter = homeAdapter
    }

    private fun initData() {
        mDataList = ArrayList<HomeItem>()
        for (i in TITLE.indices) {
            val item = HomeItem()
            item.title = TITLE[i]
            item.activity = ACTIVITY[i]
            item.imageResource = IMG[i]
            mDataList!!.add(item)
        }
    }

    companion object {
        private val ACTIVITY = arrayOf(
                AnimationUseActivity::class.java,
                MultipleItemUseActivity::class.java,
                HeaderAndFooterUseActivity::class.java,
                PullToRefreshUseActivity::class.java,
                SectionUseActivity::class.java,
                EmptyViewUseActivity::class.java,
                ItemDragAndSwipeUseActivity::class.java,
                ItemClickActivity::class.java,
                ExpandableUseActivity::class.java,
                DataBindingUseActivity::class.java,
                UpFetchUseActivity::class.java
        )
        private val TITLE = arrayOf(
                "Animation",
                "MultipleItem",
                "Header/Footer",
                "PullToRefresh",
                "Section",
                "EmptyView",
                "DragAndSwipe",
                "ItemClick",
                "ExpandableItem",
                "DataBinding",
                "UpFetchData"
        )
        private val IMG = intArrayOf(
                R.mipmap.gv_animation,
                R.mipmap.gv_multipleltem,
                R.mipmap.gv_header_and_footer,
                R.mipmap.gv_pulltorefresh,
                R.mipmap.gv_section,
                R.mipmap.gv_empty,
                R.mipmap.gv_drag_and_swipe,
                R.mipmap.gv_item_click,
                R.mipmap.gv_expandable,
                R.mipmap.gv_databinding,
                R.drawable.gv_up_fetch
        )
    }

}
