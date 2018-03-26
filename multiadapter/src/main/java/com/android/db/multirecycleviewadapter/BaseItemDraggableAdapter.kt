package com.android.db.multirecycleviewadapter

import android.graphics.Canvas
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View
import com.android.db.multirecycleviewadapter.listener.OnItemDragListener
import com.android.db.multirecycleviewadapter.listener.OnItemSwipeListener
import java.util.*

/**
 * Base item draggable adapter
 *
 * Created by DengBo on 16/03/2018.
 */

abstract class BaseItemDraggableAdapter<T, K: BaseViewHolder>: BaseAdapter<T, K> {

    companion object {
        private const val NO_TOGGLE_VIEW = 0
    }

    protected var mToggleViewId = NO_TOGGLE_VIEW
    protected var itemDragEnabled = false
    protected var itemSwipeEnabled = false
    protected var mDragOnLongPress = true
    protected var mItemTouchHelper: ItemTouchHelper? = null
    protected var mOnItemDragListener: OnItemDragListener? = null
    protected var mOnItemSwipeListener: OnItemSwipeListener? = null

    protected var mOnToggleViewTouchListener: View.OnTouchListener? = null
    protected var mOnToggleViewLongClickListener: View.OnLongClickListener? = null

    constructor(data: List<T>): super(0, data)
    constructor(layoutResId: Int, data: List<T>): super(layoutResId, data)

    /**
     * To bind different types of holder and solve different the bind events
     *
     * @param holder
     * @param position
     * @see .getDefItemViewType
     */
    override fun onBindViewHolder(holder: K, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewType = holder.itemViewType

        if (mItemTouchHelper != null && itemDragEnabled
                && viewType != TYPE_LOAD_MORE_VIEW && viewType != TYPE_HEADER_VIEW
                && viewType != TYPE_EMPTY_VIEW && viewType != TYPE_FOOTER_VIEW) {
            if (mToggleViewId != NO_TOGGLE_VIEW) {
                val toggleView = holder.getView<View>(mToggleViewId)
                if (toggleView != null) {
                    toggleView.setTag(R.id.base_adapter_view_holder_support, holder)
                    if (mDragOnLongPress) {
                        toggleView.setOnLongClickListener(mOnToggleViewLongClickListener)
                    } else {
                        toggleView.setOnTouchListener(mOnToggleViewTouchListener)
                    }
                }
            } else {
                holder.itemView.setTag(R.id.base_adapter_view_holder_support, holder)
                holder.itemView.setOnLongClickListener(mOnToggleViewLongClickListener)
            }
        }
    }

    /**
     * Set the toggle view's id which will trigger drag event.
     * If the toggle view id is not set, drag event will be triggered when the item is long pressed.
     *
     * @param toggleViewId the toggle view's id
     */
    fun setToggleViewId(toggleViewId: Int) {
        mToggleViewId = toggleViewId
    }

    /**
     * Set the drag event should be trigger on long press.
     * Work when the toggleViewId has been set.
     *
     * @param longPress by default is true.
     */
    fun setToggleDragOnLongPress(longPress: Boolean) {
        mDragOnLongPress = longPress
        if (mDragOnLongPress) {
            mOnToggleViewTouchListener = null
            mOnToggleViewLongClickListener = View.OnLongClickListener { v ->
                if (mItemTouchHelper != null && itemDragEnabled) {
                    mItemTouchHelper?.startDrag(v.getTag(R.id.base_adapter_view_holder_support) as RecyclerView.ViewHolder)
                }
                true
            }
        } else {
            mOnToggleViewTouchListener = View.OnTouchListener { v, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN && !mDragOnLongPress) {
                    if (mItemTouchHelper != null && itemDragEnabled) {
                        mItemTouchHelper?.startDrag(v.getTag(R.id.base_adapter_view_holder_support) as RecyclerView.ViewHolder)
                    }
                    true
                } else {
                    false
                }
            }
            mOnToggleViewLongClickListener = null
        }
    }

    /**
     * Enable drag items.
     * Use itemView as the toggleView when long pressed.
     *
     * @param itemTouchHelper [ItemTouchHelper]
     */
    fun enableDragItem(itemTouchHelper: ItemTouchHelper) {
        enableDragItem(itemTouchHelper, NO_TOGGLE_VIEW, true)
    }

    /**
     * Enable drag items. Use the specified view as toggle.
     *
     * @param itemTouchHelper [ItemTouchHelper]
     * @param toggleViewId    The toggle view's id.
     * @param dragOnLongPress If true the drag event will be trigger on long press, otherwise on touch down.
     */
    fun enableDragItem(itemTouchHelper: ItemTouchHelper, toggleViewId: Int, dragOnLongPress: Boolean) {
        itemDragEnabled = true
        mItemTouchHelper = itemTouchHelper
        setToggleViewId(toggleViewId)
        setToggleDragOnLongPress(dragOnLongPress)
    }

    /**
     * Disable drag items.
     */
    fun disableDragItem() {
        itemDragEnabled = false
        mItemTouchHelper = null
    }

    fun isItemDraggable(): Boolean {
        return itemDragEnabled
    }

    /**
     *
     * Enable swipe items.
     * You should attach [ItemTouchHelper] which construct with [ItemDragAndSwipeCallback] to the Recycler when you enable this.
     */
    fun enableSwipeItem() {
        itemSwipeEnabled = true
    }

    fun disableSwipeItem() {
        itemSwipeEnabled = false
    }

    fun isItemSwipeEnable(): Boolean {
        return itemSwipeEnabled
    }

    /**
     * @param onItemDragListener Register a callback to be invoked when drag event happen.
     */
    fun setOnItemDragListener(onItemDragListener: OnItemDragListener) {
        mOnItemDragListener = onItemDragListener
    }

    fun getViewHolderPosition(viewHolder: RecyclerView.ViewHolder): Int {
        return viewHolder.adapterPosition - headerLayoutCount
    }

    fun onItemDragStart(viewHolder: RecyclerView.ViewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener?.onItemDragStart(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    fun onItemDragMoving(source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) {
        val from = getViewHolderPosition(source)
        val to = getViewHolderPosition(target)

        if (inRange(from) && inRange(to)) {
            if (from < to) {
                for (i in from until to) {
                    Collections.swap(dataSrc, i, i + 1)
                }
            } else {
                for (i in from downTo to + 1) {
                    Collections.swap(dataSrc, i, i - 1)
                }
            }
            notifyItemMoved(source.adapterPosition, target.adapterPosition)
        }

        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener?.onItemDragMoving(source, from, target, to)
        }
    }

    fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder) {
        if (mOnItemDragListener != null && itemDragEnabled) {
            mOnItemDragListener?.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    fun setOnItemSwipeListener(listener: OnItemSwipeListener) {
        mOnItemSwipeListener = listener
    }

    fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener?.onItemSwipeStart(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    fun onItemSwipeClear(viewHolder: RecyclerView.ViewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener?.clearView(viewHolder, getViewHolderPosition(viewHolder))
        }
    }

    fun onItemSwiped(viewHolder: RecyclerView.ViewHolder) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener?.onItemSwiped(viewHolder, getViewHolderPosition(viewHolder))
        }

        val pos = getViewHolderPosition(viewHolder)

        if (inRange(pos)) {
            dataSrc.removeAt(pos)
            notifyItemRemoved(viewHolder.adapterPosition)
        }
    }

    fun onItemSwiping(canvas: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, isCurrentlyActive: Boolean) {
        if (mOnItemSwipeListener != null && itemSwipeEnabled) {
            mOnItemSwipeListener?.onItemSwipeMoving(canvas, viewHolder, dX, dY, isCurrentlyActive)
        }
    }

    private fun inRange(position: Int): Boolean {
        return position >= 0 && position < dataSrc.size
    }
}
