package com.android.db.multirecycleviewadapter

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.android.db.multirecycleviewadapter.adapter.UpFetchAdapter
import com.android.db.multirecycleviewadapter.base.BaseActivity
import com.android.db.multirecycleviewadapter.ientity.Movie
import com.android.db.multirecycleviewadapter.listener.UpFetchListener
import java.util.*

class UpFetchUseActivity : BaseActivity() {
    internal lateinit var mRecyclerView: RecyclerView
    internal lateinit var mAdapter: UpFetchAdapter

    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackBtn()
        setTitle("UpFetch Use")
        setContentView(R.layout.activity_data_binding_use)

        mRecyclerView = findViewById(R.id.rv) as RecyclerView
        mAdapter = UpFetchAdapter()
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
        mAdapter.notify(genData(), true)
        mAdapter.isUpFetchEnable = true
        /**
         * start fetch when scroll to position 2, default is 1.
         */
        mAdapter.setStartUpFetchPosition(2)
        mAdapter.setUpFetchListener(object : UpFetchListener {
            override fun onUpFetch() {
                startUpFetch()
            }
        })
    }

    private fun startUpFetch() {
        count++
        /**
         * set fetching on when start network request.
         */
        mAdapter.isUpFetching = true
        /**
         * get data from internet.
         */
        mRecyclerView.postDelayed({
            mAdapter.notify(genData(), 0)
            /**
             * set fetching off when network request ends.
             */
            /**
             * set fetching off when network request ends.
             */
            mAdapter.isUpFetching = false
            /**
             * set fetch enable false when you don't need anymore.
             */
            /**
             * set fetch enable false when you don't need anymore.
             */
            if (count > 5) {
                mAdapter.isUpFetchEnable = false
            }
        }, 300)
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
