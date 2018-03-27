package com.android.db.multirecycleviewadapter

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast

import com.android.db.multirecycleviewadapter.adapter.SectionAdapter
import com.android.db.multirecycleviewadapter.base.BaseActivity
import com.android.db.multirecycleviewadapter.data.DataServer
import com.android.db.multirecycleviewadapter.ientity.MySection

class SectionUseActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mData: List<MySection>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_uer)
        setBackBtn()
        setTitle("Section Use")
        mRecyclerView = findViewById(R.id.rv_list) as RecyclerView
        mRecyclerView!!.layoutManager = GridLayoutManager(this, 2)
        mData = DataServer.sampleData
        val sectionAdapter = SectionAdapter(R.layout.item_section_content, R.layout.def_section_head, mData!!)

        sectionAdapter.onItemClickListener = object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
                val mySection = mData!![position]
                if (mySection.isHeader)
                    Toast.makeText(this@SectionUseActivity, mySection.header, Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this@SectionUseActivity, mySection.entity!!.name, Toast.LENGTH_LONG).show()
            }
        }
        sectionAdapter.onItemChildClickListener = object : BaseAdapter.OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
                Toast.makeText(this@SectionUseActivity, "onItemChildClick$position", Toast.LENGTH_LONG).show()
            }
        }
        mRecyclerView!!.adapter = sectionAdapter
    }


}
