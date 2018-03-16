package com.android.db.multirecycleviewadapter.listener

import android.os.Build
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import com.android.db.multirecycleviewadapter.BaseAdapter
import com.android.db.multirecycleviewadapter.BaseAdapter.Companion.EMPTY_VIEW
import com.android.db.multirecycleviewadapter.BaseAdapter.Companion.FOOTER_VIEW
import com.android.db.multirecycleviewadapter.BaseAdapter.Companion.HEADER_VIEW
import com.android.db.multirecycleviewadapter.BaseAdapter.Companion.LOADING_VIEW
import com.android.db.multirecycleviewadapter.XViewHolder
import com.android.db.multirecycleviewadapter.BaseViewHolder

/**
 *
 * Created by DengBo on 16/03/2018.
 *
 * <p>
 *
 * This can be useful for applications that wish to implement various forms of click and longclick and childView click
 * manipulation of item views within the RecyclerView. SimpleClickListener may intercept
 * a touch interaction already in progress even if the SimpleClickListener is already handling that
 * gesture stream itself for the purposes of scrolling.
 *
 * @see RecyclerView.OnItemTouchListener
 */
abstract class SimpleClickListener: RecyclerView.OnItemTouchListener {

    private var mGestureDetector: GestureDetectorCompat? = null
    private var recyclerView: RecyclerView? = null
    private var mPressedView: View? = null
    private var mIsPrepressed = false
    private var mIsShowPress = false

    protected var baseAdapter: BaseAdapter<*, *>? = null

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        if (recyclerView == null || recyclerView != rv) {
            recyclerView = rv
            recyclerView?.apply {
                baseAdapter = adapter as BaseAdapter<*, *>?
                mGestureDetector = GestureDetectorCompat(context, ItemTouchHelperGestureListener(this))
            }
        }
        if (mGestureDetector?.onTouchEvent(e) != true && e?.actionMasked == MotionEvent.ACTION_UP && mIsShowPress) {
            if (mPressedView != null) {
                val vh = recyclerView?.getChildViewHolder(mPressedView) as XViewHolder
                if (!isHeaderOrFooterView(vh.itemViewType)) {
                    mPressedView?.isPressed = false
                }
            }
            mIsShowPress = false
            mIsPrepressed = false
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        mGestureDetector?.onTouchEvent(e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    private inner class ItemTouchHelperGestureListener constructor(private val recyclerView: RecyclerView) : GestureDetector.OnGestureListener {

        override fun onDown(e: MotionEvent): Boolean {
            mIsPrepressed = true
            mPressedView = recyclerView.findChildViewUnder(e.x, e.y)
            return false
        }

        override fun onShowPress(e: MotionEvent) {
            if (mIsPrepressed && mPressedView != null) {
                //mPressedView.setPressed(true);
                mIsShowPress = true
            }
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (mIsPrepressed && mPressedView != null) {
                if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    return false
                }
                val pressedView = mPressedView
                val vh = recyclerView.getChildViewHolder(pressedView) as BaseViewHolder

                if (isHeaderOrFooterPosition(vh.layoutPosition)) {
                    return false
                }
                val childClickViewIds = vh.childClickViewIds
                val nestViewIds = vh.nestViewIds
                if (childClickViewIds.size > 0) {
                    for (childClickViewId in childClickViewIds) {
                        val childView = pressedView?.findViewById<View>(childClickViewId)
                        if (childView != null) {
                            if (inRangeOfView(childView, e) && childView.isEnabled) {
                                if (nestViewIds.contains(childClickViewId)) {
                                    return false
                                }
                                setPressViewHotSpot(e, childView)
                                childView.isPressed = true
                                baseAdapter?.apply {
                                    onItemChildClick(this, childView, vh.layoutPosition - getHeaderLayoutCount())
                                }
                                resetPressedView(childView)
                                return true
                            } else {
                                childView.isPressed = false
                            }
                        }
                    }
                    setPressViewHotSpot(e, pressedView)
                    mPressedView?.isPressed = true
                    for (childClickViewId in childClickViewIds) {
                        val childView = pressedView?.findViewById<View>(childClickViewId)
                        childView?.isPressed = false
                    }
                    baseAdapter?.apply {
                        if (null != pressedView) {
                            onItemChildClick(this, pressedView, vh.layoutPosition - getHeaderLayoutCount())
                        }
                    }
                } else {
                    setPressViewHotSpot(e, pressedView)
                    mPressedView?.isPressed = true
                    if (childClickViewIds.size > 0) {
                        for (childClickViewId in childClickViewIds) {
                            val childView = pressedView?.findViewById<View>(childClickViewId)
                            childView?.isPressed = false
                        }
                    }
                    baseAdapter?.apply {
                        if (null != pressedView) {
                            onItemChildClick(this, pressedView, vh.layoutPosition - getHeaderLayoutCount())
                        }
                    }
                }
                resetPressedView(pressedView)

            }
            return true
        }

        private fun resetPressedView(pressedView: View?) {
            pressedView?.postDelayed({
                pressedView.isPressed = false
            }, 50)

            mIsPrepressed = false
            mPressedView = null
        }

        override fun onLongPress(e: MotionEvent) {
            var isChildLongClick = false
            if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                return
            }
            if (mIsPrepressed && mPressedView != null) {
                mPressedView?.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                val vh = recyclerView.getChildViewHolder(mPressedView) as BaseViewHolder
                if (!isHeaderOrFooterPosition(vh.layoutPosition)) {
                    val longClickViewIds = vh.childLongClickViewIds
                    val nestViewIds = vh.nestViewIds
                    if (longClickViewIds.size > 0) {
                        for (longClickViewId in longClickViewIds) {
                            val childView = mPressedView?.findViewById<View>(longClickViewId)
                            if (inRangeOfView(childView, e) && childView?.isEnabled == true) {
                                if (nestViewIds.contains(longClickViewId)) {
                                    isChildLongClick = true
                                    break
                                }
                                setPressViewHotSpot(e, childView)
                                baseAdapter?.apply {
                                    onItemChildLongClick(this, childView, vh.layoutPosition - getHeaderLayoutCount())
                                }
                                childView.isPressed = true
                                mIsShowPress = true
                                isChildLongClick = true
                                break
                            }
                        }
                    }
                    if (!isChildLongClick) {
                        baseAdapter?.apply {
                            if (null != mPressedView) {
                                onItemChildLongClick(this, mPressedView!!, vh.layoutPosition - getHeaderLayoutCount())
                            }
                        }
                        setPressViewHotSpot(e, mPressedView)
                        mPressedView?.isPressed = true
                        for (longClickViewId in longClickViewIds) {
                            val childView = mPressedView?.findViewById<View>(longClickViewId)
                            childView?.isPressed = false
                        }
                        mIsShowPress = true
                    }
                }
            }
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            return false
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            return false
        }
    }

    private fun setPressViewHotSpot(e: MotionEvent, mPressedView: View?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /**
             * when click Outside the region, mPressedView is null
             */
            if (mPressedView != null && mPressedView.background != null) {
                mPressedView.background.setHotspot(e.rawX, e.y - mPressedView.y)
            }
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param view     The view within the AdapterView that was clicked (this
     * will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    abstract fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int)

    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param view     The view whihin the AbsListView that was clicked
     * @param position The position of the view int the adapter
     * @return true if the callback consumed the long click ,false otherwise
     */
    abstract fun onItemLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int)

    abstract fun onItemChildClick(adapter: BaseAdapter<*, *>, view: View, position: Int)

    abstract fun onItemChildLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int)

    fun inRangeOfView(view: View?, ev: MotionEvent): Boolean {
        val location = IntArray(2)
        if (view == null || !view.isShown) {
            return false
        }
        view.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]
        return !(ev.rawX < x
                || ev.rawX > x + view.width
                || ev.rawY < y
                || ev.rawY > y + view.height)
    }

    private fun isHeaderOrFooterPosition(position: Int): Boolean {
        /**
         * have a headview and EMPTY_VIEW FOOTER_VIEW LOADING_VIEW
         */
        if (baseAdapter == null) {
            if (recyclerView != null) {
                baseAdapter = recyclerView?.adapter as BaseAdapter<*, *>?
            } else {
                return false
            }
        }
        val type = baseAdapter?.getItemViewType(position)
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW
    }

    private fun isHeaderOrFooterView(type: Int): Boolean {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW
    }

}
