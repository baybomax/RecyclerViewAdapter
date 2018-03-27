package com.android.db.multirecycleviewadapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import com.android.db.multirecycleviewadapter.adapter.ItemDragAdapter
import com.android.db.multirecycleviewadapter.base.BaseActivity
import com.android.db.multirecycleviewadapter.listener.ItemDragAndSwipeCallback
import com.android.db.multirecycleviewadapter.listener.OnItemDragListener
import com.android.db.multirecycleviewadapter.listener.OnItemSwipeListener
import com.android.db.multirecycleviewadapter.util.ToastUtils
import java.util.*

class ItemDragAndSwipeUseActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mData: List<String>? = null
    private var mAdapter: ItemDragAdapter? = null
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var mItemDragAndSwipeCallback: ItemDragAndSwipeCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_touch_use)
        setBackBtn()
        setTitle("ItemDrag  And Swipe")
        mRecyclerView = findViewById(R.id.rv_list) as RecyclerView
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mData = generateData(50)
        val listener = object : OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "drag start")
                val holder = viewHolder as BaseViewHolder
                //                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            override fun onItemDragMoving(source: RecyclerView.ViewHolder, from: Int, target: RecyclerView.ViewHolder, to: Int) {
                Log.d(TAG, "move from: " + source.adapterPosition + " to: " + target.adapterPosition)
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "drag end")
                val holder = viewHolder as BaseViewHolder
                //                holder.setTextColor(R.id.tv, Color.BLACK);
            }
        }
        val paint = Paint()
        paint.isAntiAlias = true
        paint.textSize = 20f
        paint.color = Color.BLACK
        val onItemSwipeListener = object : OnItemSwipeListener {
            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "view swiped start: $pos")
                val holder = viewHolder as BaseViewHolder
                //                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            override fun clearView(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "View reset: $pos")
                val holder = viewHolder as BaseViewHolder
                //                holder.setTextColor(R.id.tv, Color.BLACK);
            }

            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                Log.d(TAG, "View Swiped: $pos")
            }

            override fun onItemSwipeMoving(canvas: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, isCurrentlyActive: Boolean) {
                canvas.drawColor(ContextCompat.getColor(this@ItemDragAndSwipeUseActivity, R.color.color_light_blue))
                //                canvas.drawText("Just some text", 0, 40, paint);
            }
        }

        mAdapter = ItemDragAdapter(mData!!)
        mItemDragAndSwipeCallback = ItemDragAndSwipeCallback(mAdapter!!)
        mItemTouchHelper = ItemTouchHelper(mItemDragAndSwipeCallback)
        mItemTouchHelper!!.attachToRecyclerView(mRecyclerView)

        //mItemDragAndSwipeCallback.setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        mItemDragAndSwipeCallback!!.setSwipeMoveFlags(ItemTouchHelper.START or ItemTouchHelper.END)
        mAdapter!!.enableSwipeItem()
        mAdapter!!.setOnItemSwipeListener(onItemSwipeListener)
        mAdapter!!.enableDragItem(mItemTouchHelper!!)
        mAdapter!!.setOnItemDragListener(listener)
        //        mRecyclerView.addItemDecoration(new GridItemDecoration(this ,R.drawable.list_divider));

        mRecyclerView!!.adapter = mAdapter
        //        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
        //            @Override
        //            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
        //                ToastUtils.showShortToast("点击了" + position);
        //            }
        //        });
        mAdapter!!.onItemClickListener = object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int) {
                ToastUtils.showShortToast("点击了$position")
            }
        }
    }

    private fun generateData(size: Int): List<String> {
        val data = ArrayList<String>(size)
        for (i in 0 until size) {
            data.add("item $i")
        }
        return data
    }

    companion object {
        private val TAG = ItemDragAndSwipeUseActivity::class.java.simpleName
    }


}
