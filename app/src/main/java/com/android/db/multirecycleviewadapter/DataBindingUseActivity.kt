package com.android.db.multirecycleviewadapter

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.db.multirecycleviewadapter.adapter.DataBindingUseAdapter
import com.android.db.multirecycleviewadapter.base.BaseActivity
import com.android.db.multirecycleviewadapter.ientity.Movie
import com.android.db.multirecycleviewadapter.util.ToastUtils
import java.util.*

class DataBindingUseActivity : BaseActivity() {

    internal lateinit var mRecyclerView: RecyclerView
    internal lateinit var mAdapter: DataBindingUseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackBtn()
        setTitle("DataBinding Use")
        setContentView(R.layout.activity_data_binding_use)

        mRecyclerView = findViewById(R.id.rv)
        mAdapter = DataBindingUseAdapter(R.layout.item_movie, genData())
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
        mAdapter.onItemClickListener = object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
                ToastUtils.showShortToast("onItemClick")
            }
        }
        mAdapter.onItemChildLongClickListener = object : BaseAdapter.OnItemChildLongClickListener {
            override fun onItemChildLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int): Boolean {
                ToastUtils.showShortToast("onItemChildLongClick")
                return true
            }
        }
        mAdapter.onItemLongClickListener = object : BaseAdapter.OnItemLongClickListener {
            override fun onItemLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int): Boolean {
                ToastUtils.showShortToast("onItemLongClick")
                return true
            }
        }
    }


    private fun genData(): List<Movie> {
        val list = ArrayList<Movie>()
        val random = Random()
        for (i in 0..9) {
            val name = "Chad"
            val price = random.nextInt(10) + 10
            val len = random.nextInt(80) + 60
            val movie = Movie(name, len, price, "He was one of Australia's most distinguished artistes")
            list.add(movie)
        }
        return list
    }
}
