package com.android.db.multirecycleviewadapter

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.db.multirecycleviewadapter.adapter.AnimationAdapter
import com.android.db.multirecycleviewadapter.animation.*
import com.android.db.multirecycleviewadapter.ientity.Status
import com.jaredrummler.materialspinner.MaterialSpinner


class AnimationUseActivity : Activity() {
    private var mRecyclerView: RecyclerView? = null
    private var mAnimationAdapter: AnimationAdapter? = null
    private var mImgBtn: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter_use)
        mRecyclerView = findViewById<RecyclerView>(R.id.rv_list)
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        initAdapter()
        initMenu()
        initView()
    }

    private fun initView() {

        mImgBtn = findViewById(R.id.img_back)
        mImgBtn?.setOnClickListener { finish() }
    }

    private fun initAdapter() {
        mAnimationAdapter = AnimationAdapter()
        mAnimationAdapter?.animation = AlphaInAnimation()
        mAnimationAdapter?.onItemChildClickListener = object : BaseAdapter.OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
                val content: String?
                val status = adapter.get(position) as Status
                when (view.id) {
                    R.id.img -> {
                        content = "img:" + status.userAvatar
                        Toast.makeText(this@AnimationUseActivity, content, Toast.LENGTH_LONG).show()
                    }
                    R.id.tweetName -> {
                        content = "name:" + status.userName
                        Toast.makeText(this@AnimationUseActivity, content, Toast.LENGTH_LONG).show()
                    }
                    R.id.tweetText -> {
                        content = "tweetText:" + status.userName
                        Toast.makeText(this@AnimationUseActivity, content, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        mRecyclerView!!.adapter = mAnimationAdapter
    }

    private fun initMenu() {
        val spinner = findViewById<MaterialSpinner>(R.id.spinner)
        spinner.setItems("AlphaIn", "ScaleIn", "SlideInBottom", "SlideInLeft", "SlideInRight", "Custom")
        spinner.setOnItemSelectedListener { _, p1, _, _ ->
            when (p1) {
                0 -> mAnimationAdapter?.animation = AlphaInAnimation()
                1 -> mAnimationAdapter?.animation = ScaleInAnimation()
                2 -> mAnimationAdapter?.animation = SlideInBottomAnimation()
                3 -> mAnimationAdapter?.animation = SlideInLeftAnimation()
                4 -> mAnimationAdapter?.animation = SlideInRightAnimation()
                5 -> mAnimationAdapter?.animation = CustomAnimation()
                else -> {
                }
            }
            mRecyclerView?.adapter = mAnimationAdapter
        }
    }
}
