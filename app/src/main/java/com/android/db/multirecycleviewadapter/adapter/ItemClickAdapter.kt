package com.android.db.multirecycleviewadapter.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.android.db.multirecycleviewadapter.BaseAdapter
import com.android.db.multirecycleviewadapter.BaseMultiTypeAdapter
import com.android.db.multirecycleviewadapter.BaseViewHolder
import com.android.db.multirecycleviewadapter.R
import com.android.db.multirecycleviewadapter.ientity.ClickEntity
import com.android.db.multirecycleviewadapter.util.Utils

/**
 *
 */
class ItemClickAdapter(data: List<ClickEntity>)
    : BaseMultiTypeAdapter<ClickEntity, BaseViewHolder>(data),
        BaseAdapter.OnItemClickListener, BaseAdapter.OnItemChildClickListener {

    private lateinit var nestAdapter: NestAdapter

    init {
        addItemType(ClickEntity.CLICK_ITEM_VIEW, R.layout.item_click_view)
        addItemType(ClickEntity.CLICK_ITEM_CHILD_VIEW, R.layout.item_click_childview)
        addItemType(ClickEntity.LONG_CLICK_ITEM_VIEW, R.layout.item_long_click_view)
        addItemType(ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW, R.layout.item_long_click_childview)
        addItemType(ClickEntity.NEST_CLICK_ITEM_CHILD_VIEW, R.layout.item_nest_click)
    }


    override fun convert(helper: BaseViewHolder, item: ClickEntity?) {
        when (helper.itemViewType) {
            ClickEntity.CLICK_ITEM_VIEW -> helper.addOnClickListener(R.id.btn)
            ClickEntity.CLICK_ITEM_CHILD_VIEW -> helper.addOnClickListener(R.id.iv_num_reduce).addOnClickListener(R.id.iv_num_add)
                    .addOnLongClickListener(R.id.iv_num_reduce).addOnLongClickListener(R.id.iv_num_add)
            ClickEntity.LONG_CLICK_ITEM_VIEW -> helper.addOnLongClickListener(R.id.btn)
            ClickEntity.LONG_CLICK_ITEM_CHILD_VIEW -> helper.addOnLongClickListener(R.id.iv_num_reduce).addOnLongClickListener(R.id.iv_num_add)
                    .addOnClickListener(R.id.iv_num_reduce).addOnClickListener(R.id.iv_num_add)
            ClickEntity.NEST_CLICK_ITEM_CHILD_VIEW -> {
                helper.setNestView(R.id.item_click)
                val recyclerView = helper.getView<RecyclerView>(R.id.nest_list)
                recyclerView?.layoutManager = LinearLayoutManager(helper.itemView.context, LinearLayoutManager.VERTICAL, false)
                recyclerView?.setHasFixedSize(true)

                nestAdapter = NestAdapter()
                nestAdapter.onItemClickListener = this
                nestAdapter.onItemChildClickListener = this
                recyclerView?.adapter = nestAdapter
            }
        }
    }

    override fun onItemChildClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        Toast.makeText(Utils.getContext(), "childView click", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
        Toast.makeText(Utils.getContext(), "嵌套RecycleView item 收到: 点击了第 $position 一次", Toast.LENGTH_SHORT).show()
    }
}
