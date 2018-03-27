package com.android.db.multirecycleviewadapter

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.android.db.multirecycleviewadapter.animation.BaseAnimation
import com.android.db.multirecycleviewadapter.entity.IExpandable
import com.android.db.multirecycleviewadapter.listener.LoadMoreRequestListener
import com.android.db.multirecycleviewadapter.listener.UpFetchListener
import com.android.db.multirecycleviewadapter.listener.ViewHolderActionListener
import com.android.db.multirecycleviewadapter.util.MultiTypeDelegate
import com.android.db.multirecycleviewadapter.view.LoadMoreView
import com.android.db.multirecycleviewadapter.view.SimpleLoadMoreView
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

/**
 * Simple adapter
 *
 * Created by DengBo on 15/03/2018.
 */

@Suppress("UNCHECKED_CAST", "FINAL_UPPER_BOUND")
abstract class BaseAdapter<T, K: BaseViewHolder>(): XAdapter<T, K>(), ViewHolderActionListener {

    companion object {
        const val TYPE_HEADER_VIEW      = 0x00000111
        const val TYPE_FOOTER_VIEW      = 0x00000222
        const val TYPE_EMPTY_VIEW       = 0x00000333
        const val TYPE_LOAD_MORE_VIEW   = 0x00000555
    }

    open var loadMoreView: LoadMoreView = SimpleLoadMoreView()
    private var loadMoreViewRequestListener: LoadMoreRequestListener? = null
    private var isLoadViewNextEnable = false
    var isLoadMoreViewLoading = false
        private set
    var isLoadMoreViewEnable = false
        set(value) {
            val p1 = loadMoreViewCount
            field = value
            val p2 = loadMoreViewCount
            if (p1 > 0) {
                if (p2 <= 0) {
                    notifyItemRemoved(loadMoreViewPosition)
                }
            } else {
                if (p2 > 0) {
                    loadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
                    notifyItemInserted(loadMoreViewPosition)
                }
            }
        }
    var loadMoreViewCount = 0
        private set
        get() {
            val p1 = null == loadMoreViewRequestListener || !isLoadMoreViewEnable
            val p2 = !isLoadViewNextEnable && loadMoreView.loadEndGone

            return if (p1 || p2 || dataSrc.size <= 0) {
                0
            } else
                1
        }
    var loadMoreViewPosition = 0
        private set
        get() = headerLayoutCount + dataSrc.size + footerLayoutCount

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    var onItemChildClickListener: OnItemChildClickListener? = null
    var onItemChildLongClickListener: OnItemChildLongClickListener? = null

    private var lastLayoutPosition = -1

    open var animation: BaseAnimation? = null

    var headerLayout: LinearLayout? = null
        private set
    var footerLayout: LinearLayout? = null
        private set
    var headerLayoutCount = 0
        private set
        get() {
            headerLayout?.apply {
                if (childCount > 0) {
                    return 1
                }
            }
            return 0
        }
    var headerLayoutPosition = 0
        private set
        get() {
            if (emptyViewCount > 0) {
                if (isHeaderEnableWhenEmpty) {
                    return 0
                }
            } else {
                return 0
            }
            return -1
        }
    var footerLayoutCount = 0
        private set
        get() {
            footerLayout?.apply {
                if (childCount > 0) {
                    return 1
                }
            }
            return 0
        }
    var footerLayoutPosition = 0
        private set
        get() {
            if (emptyViewCount > 0) {
                var position = 1
                if (isHeaderEnableWhenEmpty && headerLayoutCount > 0) {
                    position++
                }
                if (isFooterEnableWhenEmpty) {
                    return position
                }
            } else {
                return headerLayoutCount + dataSrc.size
            }
            return -1
        }

    var isUpFetching = false
    var isUpFetchEnable = false
    private var startUpFetchPosition = 1
    private var upFetchListener: UpFetchListener? = null

    var isHeaderEnableWhenEmpty = false
    var isFooterEnableWhenEmpty = false
    var emptyViewLayout: FrameLayout? = null
        private set
    var emptyViewCount: Int = 0
        private set
        get() {
            emptyViewLayout?.apply {
                if (childCount > 0 && dataSrc.isEmpty()) {
                    return 1
                }
            }
            return 0
        }

    protected var layoutResId: Int = 0
    protected var context: Context? = null
    protected var layoutInflater: LayoutInflater? = null

    protected var recyclerView: RecyclerView? = null
        private set

    var multiTypeDelegate: MultiTypeDelegate<T>? = null

    private var autoPreLoadCount = 1

    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use [GridLayoutManager],and it will ignore span size.
     */
    var isHeaderViewAsFlow: Boolean = false
    var isFooterViewAsFlow: Boolean = false
    private var spanSizeLookup: SpanSizeLookup? = null

    interface SpanSizeLookup {
        fun getSpanSize(gridLayoutManager: GridLayoutManager, position: Int): Int
    }

    /**
     * @param spanSizeLookup instance to be used to query number of spans occupied by each item
     */
    fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup) {
        this.spanSizeLookup = spanSizeLookup
    }

    /**
     * Check recycler view is bind?
     * @throws RuntimeException
     */
    private fun checkRecyclerView() {
        if (null == recyclerView) {
            throw RuntimeException("Please bind recyclerView")
        }
    }

    /**
     * Same as recyclerView.setAdapter(), and save the instance of recyclerView
     */
    fun bindRecyclerView(recyclerView: RecyclerView) {
        if (this.recyclerView != null) {
            throw RuntimeException("RecyclerView already bind")
        }
        this.recyclerView = recyclerView
        this.recyclerView?.adapter = this
    }

    /**
     * Set load more view request listener.
     *
     * @param recyclerView The recyclerView to bind.
     * @param loadMoreRequestListener The listener.
     */
    fun setLoadMoreRequestListener(recyclerView: RecyclerView, loadMoreRequestListener: LoadMoreRequestListener) {
        loadMoreViewRequestListener = loadMoreRequestListener

        isLoadViewNextEnable = true
        isLoadMoreViewEnable = true
        isLoadMoreViewLoading = false

        if (null == this.recyclerView) {
            this.recyclerView = recyclerView
        }
    }

    /**
     * Check if full page after method .notify new data, if full, it will enable load more again.
     *
     *@see [notify]
     */
    fun enableLoadMoreViewIfPaged() {
        checkRecyclerView()
        isLoadMoreViewEnable = false
        recyclerView?.apply {
            val manager = layoutManager
            if (manager is LinearLayoutManager) {
                postDelayed({
                    if (manager.findLastCompletelyVisibleItemPosition() + 1 != itemCount) {
                        isLoadMoreViewEnable = true
                    }
                }, 50)
            } else if (manager is StaggeredGridLayoutManager) {
                postDelayed({
                    val positions = IntArray(manager.spanCount)
                    manager.findLastCompletelyVisibleItemPositions(positions)
                    if ((positions.max() ?: -1) + 1 != itemCount) {
                        isLoadMoreViewEnable = true
                    }
                }, 50)
            }
        }
    }

    /**
     * Set the upFetch listener.
     *
     * @param listener
     */
    fun setUpFetchListener(listener: UpFetchListener) {
        upFetchListener = listener
        isUpFetchEnable = true
    }

    /**
     * Set the post of upFetch start.
     */
    fun setStartUpFetchPosition(position: Int) {
        startUpFetchPosition = position
    }

    /**
     * Auto upFetch each time create viewHolder at front start
     */
    private fun autoUpFetch(position: Int) {
        if (isUpFetchEnable && !isUpFetching && position <= startUpFetchPosition) {
            upFetchListener?.onUpFetch()
        }
    }

    /**
     * There is no more data, load more end view display
     * default is show the end view visible.
     */
    fun loadMoreEnd() {
        loadMoreEnd(false)
    }

    /**
     * There is no more data, load more end view display
     *
     * @param gone if true gone the load more view
     */
    fun loadMoreEnd(gone: Boolean) {
        if (loadMoreViewCount <= 0) { return }
        isLoadViewNextEnable = false
        isLoadMoreViewLoading = false
        loadMoreView.loadEndGone = gone
        if (gone) {
            notifyItemRemoved(loadMoreViewPosition)
        } else {
            loadMoreView.loadMoreStatus = LoadMoreView.STATUS_END
            notifyItemChanged(loadMoreViewPosition)
        }
    }

    /**
     * Handle that load more complete
     */
    fun loadMoreComplete() {
        if (loadMoreViewCount <= 0) { return }
        isLoadViewNextEnable = true
        isLoadMoreViewLoading = false
        loadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
        notifyItemChanged(loadMoreViewPosition)
    }

    /**
     * Handle that load more fail
     */
    fun loadMoreFail() {
        if (loadMoreViewCount == 0) { return }
        isLoadMoreViewLoading = false
        loadMoreView.loadMoreStatus = LoadMoreView.STATUS_FAIL
        notifyItemChanged(loadMoreViewPosition)
    }

    /**
     * Constructor to generate initialization data.
     *
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    constructor(@LayoutRes layoutResId: Int = 0, data: List<T>? = null): this() {
        data?.let {
            dataSrc.addAll(it)
        }
        if (layoutResId != 0) {
            this.layoutResId = layoutResId
        }
    }

    /**
     * Add one new data in to certain location
     *
     * @param position
     * @param data
     */
    fun add(@IntRange(from = 0) position: Int, data: T) {
        dataSrc.add(position, data)
        notifyItemInserted(position + headerLayoutCount)
        compatibilityDataSizeChanged(1)
    }

    /**
     * add one new data
     *
     * @param data
     */
    fun add(data: T) {
        dataSrc.add(data)
        notifyItemInserted(dataSrc.size + headerLayoutCount)
        compatibilityDataSizeChanged(1)
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    open fun remove(@IntRange(from = 0) position: Int) {
        dataSrc.removeAt(position)
        val internalPosition = position + headerLayoutCount
        notifyItemRemoved(internalPosition)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(internalPosition, dataSrc.size - internalPosition)
    }

    /**
     * Set the item data at specify position
     *
     * @param index
     * @param data
     */
    fun set(@IntRange(from = 0) index: Int, data: T) {
        dataSrc[index] = data
        notifyItemChanged(index + headerLayoutCount)
    }

    /**
     * Notify that collection arrived, should notify adapter
     *
     * @param data
     * @param refresh if true, data from pull-down refresh, otherwise
     *                is from pull-up load
     *                default is false
     */
    fun notify(data: Collection<T>, refresh: Boolean = false) {
        if (refresh) {
            dataSrc.clear()
            dataSrc.addAll(data)
            loadMoreViewRequestListener?.run {
                isLoadViewNextEnable = true
                isLoadMoreViewEnable = true
                isLoadMoreViewLoading = false
                loadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
            }
            lastLayoutPosition = -1
            notifyDataSetChanged()
        } else {
            dataSrc.addAll(data)
            notifyItemRangeInserted(dataSrc.size - data.size + headerLayoutCount, data.size)
            compatibilityDataSizeChanged(data.size)
        }
    }

    /**
     * Notify that notify adapter add collection at specify position
     *
     * @param data
     * @param position
     */
    fun notify(data: Collection<T>, @IntRange(from = 0) position: Int) {
        dataSrc.addAll(position, data)
        notifyItemRangeInserted(position + headerLayoutCount, data.size)
        compatibilityDataSizeChanged(data.size)
    }

    /**
     * use data to replace all item in dataSrc. this method is different [.setNewData],
     * it doesn't change the dataSrc reference
     *
     * @param data data collection
     */
    fun replace(data: Collection<T>) {
        if (data !== dataSrc) {
            dataSrc.clear()
            dataSrc.addAll(data)
        }
        notifyDataSetChanged()
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    private fun compatibilityDataSizeChanged(size: Int) {
        if (dataSrc.size == size) {
            notifyDataSetChanged()
        }
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    fun get(@IntRange(from = 0) position: Int): T? {
        return if (position >= 0 && position < dataSrc.size)
            dataSrc[position]
        else
            null
    }

    /**
     * @Inherit
     */
    override fun getItemCount(): Int {
        var count = 0
        if (emptyViewCount > 0) {
            count++
            if (isHeaderEnableWhenEmpty && headerLayoutCount > 0) {
                count++
            }
            if (isFooterEnableWhenEmpty && footerLayoutCount > 0) {
                count++
            }
        } else {
            count = headerLayoutCount + dataSrc.size + footerLayoutCount + loadMoreViewCount
        }
        return count
    }

    /**
     * @Inherit
     */
    override fun getItemViewType(position: Int): Int {
        if (emptyViewCount > 0) {
            val header = isHeaderEnableWhenEmpty && headerLayoutCount > 0
            return when (position) {
                0 -> if (header) { TYPE_HEADER_VIEW } else { TYPE_EMPTY_VIEW }
                1 -> if (header) { TYPE_EMPTY_VIEW } else { TYPE_FOOTER_VIEW }
                2 -> TYPE_FOOTER_VIEW
                else -> TYPE_EMPTY_VIEW
            }
        }
        val headers = headerLayoutCount
        if (position < headers) {
            return TYPE_HEADER_VIEW
        } else {
            var iPosition = position - headers
            val adapterCount = dataSrc.size
            return if (iPosition < adapterCount) {
                getDefItemViewType(iPosition)
            } else {
                iPosition -= adapterCount
                val footers = footerLayoutCount
                if (iPosition < footers) {
                    TYPE_FOOTER_VIEW
                } else {
                    TYPE_LOAD_MORE_VIEW
                }
            }
        }
    }

    /**
     * Return 'Int' item type at specify position
     */
    protected open fun getDefItemViewType(position: Int): Int {
        multiTypeDelegate?.apply {
            return getDefItemViewType(dataSrc, position)
        }
        return super.getItemViewType(position)
    }

    /**
     * @Inherit
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K {
        val baseViewHolder: K
        context = parent.context
        layoutInflater = LayoutInflater.from(context)
        when (viewType) {
            TYPE_LOAD_MORE_VIEW -> baseViewHolder = generateLoadMoreView(parent)
            TYPE_HEADER_VIEW -> baseViewHolder = createBaseViewHolder(headerLayout ?: LinearLayout(parent.context))
            TYPE_EMPTY_VIEW -> baseViewHolder = createBaseViewHolder(emptyViewLayout ?: LinearLayout(parent.context))
            TYPE_FOOTER_VIEW -> baseViewHolder = createBaseViewHolder(footerLayout ?: LinearLayout(parent.context))
            else -> {
                baseViewHolder = onCreateDefViewHolder(parent, viewType)
                baseViewHolder.itemView?.apply {
                    setOnClickListener {
                        onItemClickListener?.onItemClick(this@BaseAdapter, it, baseViewHolder.layoutPosition - headerLayoutCount)
                    }
                    setOnLongClickListener {
                        onItemLongClickListener?.onItemLongClick(this@BaseAdapter, it, baseViewHolder.layoutPosition - headerLayoutCount)
                        true
                    }
                }
            }
        }
        baseViewHolder.setOnItemChildListener(this)
        return baseViewHolder
    }

    /**
     * Generate load more view
     */
    private fun generateLoadMoreView(parent: ViewGroup): K {
        return createBaseViewHolder(getItemView(loadMoreView.getLayoutId(), parent)).apply {
            itemView.setOnClickListener {
                if (loadMoreView.loadMoreStatus == LoadMoreView.STATUS_FAIL) { notifyLoadMoreLoading() }
                if (loadMoreView.loadMoreStatus == LoadMoreView.STATUS_END) { notifyLoadMoreEnd() }
            }
        }
    }

    /**
     * Notify that load more view state change to loading and notify changed.
     */
    open fun notifyLoadMoreLoading() {
        if (loadMoreView.loadMoreStatus == LoadMoreView.STATUS_LOADING) { return }
        loadMoreView.loadMoreStatus = LoadMoreView.STATUS_DEFAULT
        notifyItemChanged(loadMoreViewPosition)
    }

    /**
     * Notify that load more view when state is end
     * Default exec [notifyLoadMoreLoading], should #Inherit if need.
     */
    open fun notifyLoadMoreEnd() {
        notifyLoadMoreLoading()
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     * [.setFullSpan]
     *
     * @param holder
     */
    override fun onViewAttachedToWindow(holder: K) {
        super.onViewAttachedToWindow(holder)
        val type = holder.itemViewType
        if (type == TYPE_EMPTY_VIEW || type == TYPE_HEADER_VIEW
                || type == TYPE_FOOTER_VIEW || type == TYPE_LOAD_MORE_VIEW) {
            setFullSpan(holder)
        } else {
            holder.let {
                attachAnimation(holder)
            }
        }
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected fun setFullSpan(holder: RecyclerView.ViewHolder) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val params = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            params.isFullSpan = true
        }
    }

    /**
     * @Inherit
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val type = getItemViewType(position)
                    if (type == TYPE_HEADER_VIEW && isHeaderViewAsFlow) { return 1 }
                    if (type == TYPE_FOOTER_VIEW && isFooterViewAsFlow) { return 1 }

                    spanSizeLookup?.apply {
                        return if (isFixedViewType(type))
                            manager.spanCount
                        else
                            getSpanSize(manager, position - headerLayoutCount)
                    }
                    return if (isFixedViewType(type)) manager.spanCount else 1
                }
            }
        }
    }

    /**
     *
     */
    protected open fun isFixedViewType(type: Int): Boolean {
        return type == TYPE_EMPTY_VIEW || type == TYPE_HEADER_VIEW
                || type == TYPE_FOOTER_VIEW || type == TYPE_LOAD_MORE_VIEW
    }

    /**
     * To bind different types of holder and solve different the bind events
     *
     * @param holder
     * @param position
     * @see .getDefItemViewType
     */
    override fun onBindViewHolder(holder: K, position: Int) {
        //Add up fetch logic, almost like load more, but simpler.
        autoUpFetch(position)
        //Do not move position, need to change before LoadMoreView binding
        autoLoadMore(position)

        val viewType = holder.itemViewType
        when (viewType) {
            0 -> convert(holder, get(position - headerLayoutCount))
            TYPE_LOAD_MORE_VIEW -> loadMoreView.convert(holder)
            TYPE_HEADER_VIEW -> {}
            TYPE_EMPTY_VIEW -> {}
            TYPE_FOOTER_VIEW -> {}
            else -> convert(holder, get(position - headerLayoutCount))
        }
    }

    /**
     * Generate a default view holder if need.
     */
    protected open fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): K {
        var layoutId = layoutResId
        multiTypeDelegate?.apply {
            layoutId = getLayoutId(viewType)
        }
        return createBaseViewHolder(parent, layoutId)
    }

    /**
     * Create a base view holder[BaseViewHolder]
     */
    protected open fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): K {
        return createBaseViewHolder(getItemView(layoutResId, parent))
    }

    /**
     * if you want to use subclass of XViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @param view view
     * @return new ViewHolder
     */
    protected open fun createBaseViewHolder(view: View): K {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp)
            temp = temp.superclass
        }
        val k: K?
        if (z == null) {
            k = BaseViewHolder(view) as K?
        } else {
            k = createGenericKInstance(z, view)
        }
        return k ?: (BaseViewHolder(view) as K)
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
    private fun createGenericKInstance(z: Class<*>, view: View): K? {
        try {
            val constructor: Constructor<*>
            // inner and unstatic class
            if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                constructor.isAccessible = true
                return constructor.newInstance(this, view) as K?
            } else {
                constructor = z.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                return constructor.newInstance(view) as K?
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * get generic parameter K
     *
     * @param z
     * @return
     */
    private fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
        val type = z.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (temp in types) {
                if (temp is Class<*>) {
                    if (XViewHolder::class.java.isAssignableFrom(temp)) {
                        return temp
                    }
                } else if (temp is ParameterizedType) {
                    val rawType = temp.rawType
                    if (rawType is Class<*> && XViewHolder::class.java.isAssignableFrom(rawType)) {
                        return rawType
                    }
                }
            }
        }
        return null
    }

    /**
     * Append header to the rear of the headerLayout.
     *
     * @param header
     */
    fun addHeaderView(header: View): Int {
        return addHeaderView(header, -1)
    }

    /**
     * Add header view to headerLayout and set header view position in headerLayout.
     * When index = -1 or index >= child count in headerLayout,
     * the effect of this method is the same as that of [.addHeaderView].
     *
     * @param header
     * @param index  the position in headerLayout of this header.
     * When index = -1 or index >= child count in headerLayout,
     * the effect of this method is the same as that of [.addHeaderView].
     */
    fun addHeaderView(header: View, index: Int): Int {
        return addHeaderView(header, index, LinearLayout.VERTICAL)
    }

    /**
     * Generate header view layout
     *
     * @param header
     * @param i
     * @param orientation
     */
    fun addHeaderView(header: View, i: Int, orientation: Int): Int {
        var index = i
        if (headerLayout == null) {
            headerLayout = LinearLayout(header.context)
            if (orientation == LinearLayout.VERTICAL) {
                headerLayout?.orientation = LinearLayout.VERTICAL
                headerLayout?.layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                headerLayout?.orientation = LinearLayout.HORIZONTAL
                headerLayout?.layoutParams = RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }
        headerLayout?.apply {
            if (index < 0 || index > childCount) {
                index = childCount
            }
            addView(header, index)
            if (childCount == 1) {
                val position = headerLayoutPosition
                if (position != -1) {
                    notifyItemInserted(position)
                }
            }
        }
        return index
    }

    /**
     * @see setHeaderView
     */
    fun setHeaderView(header: View): Int {
        return setHeaderView(header, 0, LinearLayout.VERTICAL)
    }

    /**
     * @see setHeaderView
     */
    fun setHeaderView(header: View, index: Int): Int {
        return setHeaderView(header, index, LinearLayout.VERTICAL)
    }

    /**
     * Set header view
     *
     */
    fun setHeaderView(header: View, index: Int, orientation: Int): Int {
        return if (headerLayout == null || headerLayout?.childCount ?: 0 <= index) {
            addHeaderView(header, index, orientation)
        } else {
            headerLayout?.removeViewAt(index)
            headerLayout?.addView(header, index)
            index
        }
    }

    /**
     * Append footer to the rear of the footerLayout.
     *
     * @param footer
     */
    fun addFooterView(footer: View): Int {
        return addFooterView(footer, -1, LinearLayout.VERTICAL)
    }

    fun addFooterView(footer: View, index: Int): Int {
        return addFooterView(footer, index, LinearLayout.VERTICAL)
    }

    /**
     * Add footer view to footerLayout and set footer view position in footerLayout.
     * When index = -1 or index >= child count in footerLayout,
     * the effect of this method is the same as that of [.addFooterView].
     *
     * @param footer
     * @param index  the position in footerLayout of this footer.
     * When index = -1 or index >= child count in footerLayout,
     * the effect of this method is the same as that of [.addFooterView].
     */
    fun addFooterView(footer: View, i: Int, orientation: Int): Int {
        var index = i
        if (footerLayout == null) {
            footerLayout = LinearLayout(footer.context)
            if (orientation == LinearLayout.VERTICAL) {
                footerLayout?.orientation = LinearLayout.VERTICAL
                footerLayout?.layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                footerLayout?.orientation = LinearLayout.HORIZONTAL
                footerLayout?.layoutParams = RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }
        footerLayout?.apply {
            if (index < 0 || index > childCount) {
                index = childCount
            }
            addView(footer, index)
            if (childCount == 1) {
                val position = footerLayoutPosition
                if (position != -1) {
                    notifyItemInserted(position)
                }
            }
        }

        return index
    }

    /**
     * @see setFooterView
     */
    fun setFooterView(header: View): Int {
        return setFooterView(header, 0, LinearLayout.VERTICAL)
    }

    /**
     * @see setFooterView
     */
    fun setFooterView(header: View, index: Int): Int {
        return setFooterView(header, index, LinearLayout.VERTICAL)
    }

    /**
     * Set footer view
     */
    fun setFooterView(header: View, index: Int, orientation: Int): Int {
        return if (footerLayout == null || footerLayout?.childCount ?: 0 <= index) {
            addFooterView(header, index, orientation)
        } else {
            footerLayout?.removeViewAt(index)
            footerLayout?.addView(header, index)
            index
        }
    }

    /**
     * remove header view from headerLayout.
     * When the child count of headerLayout is 0, headerLayout will be set to null.
     *
     * @param header
     */
    fun removeHeaderView(header: View) {
        if (headerLayoutCount <= 0) return
        headerLayout?.removeView(header)
        if (headerLayout?.childCount == 0) {
            val position = headerLayoutPosition
            if (position >= 0) {
                notifyItemRemoved(position)
            }
        }
    }

    /**
     * remove footer view from footerLayout,
     * When the child count of footerLayout is 0, footerLayout will be set to null.
     *
     * @param footer
     */
    fun removeFooterView(footer: View) {
        if (footerLayoutCount == 0) return
        footerLayout?.removeView(footer)
        if (footerLayout?.childCount == 0) {
            val position = footerLayoutPosition
            if (position >= 0) {
                notifyItemRemoved(position)
            }
        }
    }

    /**
     * remove all header view from headerLayout and set null to headerLayout
     */
    fun removeAllHeaderView() {
        if (headerLayoutCount == 0) return
        headerLayout?.removeAllViews()
        val position = headerLayoutPosition
        if (position >= 0) {
            notifyItemRemoved(position)
        }
    }

    /**
     * remove all footer view from footerLayout and set null to footerLayout
     */
    fun removeAllFooterView() {
        if (footerLayoutCount == 0) return
        footerLayout?.removeAllViews()
        val position = footerLayoutPosition
        if (position >= 0) {
            notifyItemRemoved(position)
        }
    }

    /**
     * Set empty view
     */
    fun setEmptyView(layoutResId: Int, viewGroup: ViewGroup) {
        setEmptyView(LayoutInflater.from(viewGroup.context).inflate(layoutResId, viewGroup, false))
    }

    /**
     * bind recyclerView [.bindToRecyclerView] before use!
     *
     * @see .bindToRecyclerView
     */
    fun setEmptyView(layoutResId: Int) {
        checkRecyclerView()
        recyclerView?.let {
            setEmptyView(layoutResId, it)
        }
    }

    /**
     * Set empty view impl
     */
    fun setEmptyView(emptyView: View) {
        var insert = false
        if (emptyViewLayout == null) {
            emptyViewLayout = FrameLayout(emptyView.context)
            val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT)
            val lp = emptyView.layoutParams
            layoutParams.width = lp.width
            layoutParams.height = lp.height
            emptyViewLayout?.layoutParams = layoutParams
            insert = true
        }
        emptyViewLayout?.removeAllViews()
        emptyViewLayout?.addView(emptyView)
        if (insert) {
            if (emptyViewCount == 1) {
                var position = 0
                if (isHeaderEnableWhenEmpty && headerLayoutCount > 0) {
                    position++
                }
                notifyItemInserted(position)
            }
        }
    }

    /**
     * Set the position to auto preLoad data.
     * default is 1, that mean if leave 1 item will preLoad new data.
     */
    fun setAutoPreLoadCount(preLoadNumber: Int) {
        if (preLoadNumber > 1) {
            autoPreLoadCount = preLoadNumber
        }
    }

    /**
     * Auto load more data when goto specify position.
     * @param position
     */
    private fun autoLoadMore(position: Int) {
        if (loadMoreViewCount == 0) { return }
        if (position < itemCount - autoPreLoadCount) { return }
        if (loadMoreView.loadMoreStatus != LoadMoreView.STATUS_DEFAULT) { return }

        loadMoreView.loadMoreStatus = LoadMoreView.STATUS_LOADING
        if (!isLoadMoreViewLoading) {
            isLoadMoreViewLoading = true
            if (recyclerView != null) {
                recyclerView?.post({ loadMoreViewRequestListener?.onLoadMoreRequest() })
            } else {
                loadMoreViewRequestListener?.onLoadMoreRequest()
            }
        }
    }

    /**
     * Attach animation to this window
     *
     * @param holder
     */
    private fun attachAnimation(holder: RecyclerView.ViewHolder) {
        if (holder.layoutPosition > lastLayoutPosition) {
            animation?.apply {
                getAnimators(holder.itemView).forEach {
                    it.start()
                }
            }
            lastLayoutPosition = holder.layoutPosition
        }
    }

    /**
     * @param layoutResId ID for an XML layout resource to load
     * @param parent      Optional view to be the parent of the generated hierarchy or else simply an object that
     * provides a set of LayoutParams values for root of the returned
     * hierarchy
     * @return view will be return
     */
    protected open fun getItemView(@LayoutRes layoutResId: Int, parent: ViewGroup): View {
        layoutInflater?.apply {
            return inflate(layoutResId, parent, false)
        }
        return LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract fun convert(helper: K, item: T?)

    /**
     * get the specific view by position,e.g. getViewByPosition(2, R.id.textView)
     * bind recyclerView [.bindToRecyclerView] before use!
     *
     * @see .bindToRecyclerView
     */
    fun getViewByPosition(position: Int, @IdRes viewId: Int): View? {
        checkRecyclerView()
        return getViewByPosition(recyclerView, position, viewId)
    }

    /**
     * @see getViewByPosition
     */
    fun getViewByPosition(recyclerView: RecyclerView?, position: Int, @IdRes viewId: Int): View? {
        recyclerView?.apply {
            val viewHolder = findViewHolderForLayoutPosition(position) as XViewHolder
            return viewHolder.getView(viewId)
        }
        return null
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Related to expand list
     * <p>
     *     l1
     *     l2
     *     ..
     * <p>
     */
    private fun recursiveExpand(position: Int, list: List<*>): Int {
        var count = list.size
        var pos = position + list.size - 1
        var i = list.size - 1
        while (i >= 0) {
            if (list[i] is IExpandable<*>) {
                val item = list[i] as IExpandable<T>
                if (item.expandable && hasSubItems(item)) {
                    val subList = item.subItems
                    dataSrc.addAll(pos + 1, subList)
                    val subItemCount = recursiveExpand(pos + 1, subList)
                    count += subItemCount
                }
            }
            i--
            pos--
        }
        return count

    }

    /**
     * Expand an expandable item
     *
     * @param position     position of the item
     * @param animate      expand items with animation
     * @param shouldNotify notify the RecyclerView to rebind items, **false** if you want to do it
     * yourself.
     * @return the number of items that have been added.
     */
    fun expand(@IntRange(from = 0) pos: Int, animate: Boolean, shouldNotify: Boolean): Int {
        var position = pos
        position -= headerLayoutCount

        val expandable = getExpandableItem(position) ?: return 0
        if (!hasSubItems(expandable)) {
            expandable.expandable = true
            notifyItemChanged(position)
            return 0
        }
        var subItemCount = 0
        if (!expandable.expandable) {
            val list = expandable.subItems
            dataSrc.addAll(position + 1, list)
            subItemCount += recursiveExpand(position + 1, list)

            expandable.expandable = true
            //subItemCount += list.size();
        }
        val parentPos = position + headerLayoutCount
        if (shouldNotify) {
            if (animate) {
                notifyItemChanged(parentPos)
                notifyItemRangeInserted(parentPos + 1, subItemCount)
            } else {
                notifyDataSetChanged()
            }
        }
        return subItemCount
    }

    /**
     * Expand an expandable item
     *
     * @param position position of the item, which includes the header layout count.
     * @param animate  expand items with animation
     * @return the number of items that have been added.
     */
    fun expand(@IntRange(from = 0) position: Int, animate: Boolean): Int {
        return expand(position, animate, true)
    }

    /**
     * Expand an expandable item with animation.
     *
     * @param position position of the item, which includes the header layout count.
     * @return the number of items that have been added.
     */
    fun expand(@IntRange(from = 0) position: Int): Int {
        return expand(position, true, true)
    }

    /**
     * Expand all
     * ###
     *
     */
    fun expandAll(pos: Int, animate: Boolean, notify: Boolean): Int {
        var position = pos
        position -= headerLayoutCount

        var endItem: T? = null
        if (position + 1 < dataSrc.size) {
            endItem = get(position + 1)
        }

        val expandable = getExpandableItem(position) ?: return 0
        if (!hasSubItems(expandable)) {
            expandable.expandable = true
            notifyItemChanged(position)
            return 0
        }

        var count = expand(position + headerLayoutCount, false, false)
        for (i in position + 1 until dataSrc.size) {
            val item = get(i)
            if (item == endItem) { break }
            if (isExpandable(item)) {
                count += expand(i + headerLayoutCount, false, false)
            }
        }

        if (notify) {
            if (animate) {
                notifyItemRangeInserted(position + headerLayoutCount + 1, count)
            } else {
                notifyDataSetChanged()
            }
        }
        return count
    }

    /**
     * expand the item and all its subItems
     *
     * @param position position of the item, which includes the header layout count.
     * @param init     whether you are initializing the recyclerView or not.
     * if **true**, it won't notify recyclerView to redraw UI.
     * @return the number of items that have been added to the adapter.
     */
    fun expandAll(position: Int, init: Boolean): Int {
        return expandAll(position, true, !init)
    }

    /**
     * @see expandAll
     */
    fun expandAll() {
        for (i in dataSrc.size - 1 + headerLayoutCount downTo headerLayoutCount) {
            expandAll(i, false, false)
        }
    }

    ///////
    private fun recursiveCollapse(@IntRange(from = 0) position: Int): Int {
        val item = get(position)
        if (!isExpandable(item)) { return 0 }
        val expandable = item as IExpandable<T>
        var subItemCount = 0
        if (expandable.expandable) {
            val subItems = expandable.subItems

            for (i in subItems.indices.reversed()) {
                val subItem = subItems[i]
                val pos = getItemPosition(subItem)
                if (pos < 0) { continue }
                if (subItem is IExpandable<*>) {
                    subItemCount += recursiveCollapse(pos)
                }
                dataSrc.removeAt(pos)
                subItemCount++
            }
        }
        return subItemCount
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @param animate  collapse with animation or not.
     * @param notify   notify the recyclerView refresh UI or not.
     * @return the number of subItems collapsed.
     */
    fun collapse(@IntRange(from = 0) pos: Int, animate: Boolean, notify: Boolean): Int {
        var position = pos
        position -= headerLayoutCount

        val expandable = getExpandableItem(position) ?: return 0
        val subItemCount = recursiveCollapse(position)
        expandable.expandable = false
        val parentPos = position + headerLayoutCount
        if (notify) {
            if (animate) {
                notifyItemChanged(parentPos)
                notifyItemRangeRemoved(parentPos + 1, subItemCount)
            } else {
                notifyDataSetChanged()
            }
        }
        return subItemCount
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @return the number of subItems collapsed.
     */
    fun collapse(@IntRange(from = 0) position: Int): Int {
        return collapse(position, true, true)
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @return the number of subItems collapsed.
     */
    fun collapse(@IntRange(from = 0) position: Int, animate: Boolean): Int {
        return collapse(position, animate, true)
    }

    /**
     * Return the position of specify item
     */
    private fun getItemPosition(item: T?): Int {
        return if (item != null && !dataSrc.isEmpty()) dataSrc.indexOf(item) else -1
    }

    /**
     * Notify that specify item if has sub items
     */
    private fun hasSubItems(item: IExpandable<T>?): Boolean {
        if (item == null) {
            return false
        }
        val list = item.subItems
        return list.size > 0
    }

    /**
     * Return [Boolean] true, if specify item is expandable.
     * @param item T
     */
    fun isExpandable(item: T?): Boolean {
        return item != null && item is IExpandable<*>
    }

    /**
     * Get the expandable item[IExpandable] at specify position
     */
    fun getExpandableItem(position: Int): IExpandable<T>? {
        val item = get(position)
        return if (isExpandable(item)) {
            item as IExpandable<T>?
        } else {
            null
        }
    }

    /**
     * Get the parent item position of the IExpandable item
     *
     * @return return the closest parent item position of the IExpandable.
     * if the IExpandable item's level is 0, return itself position.
     * if the item's level is negative which mean do not implement this, return a negative
     * if the item is not exist in the data list, return a negative.
     */
    fun getParentPosition(item: T): Int {
        val position = getItemPosition(item)
        if (position == -1) {
            return -1
        }

        // if the item is IExpandable, return a closest IExpandable item position whose level smaller than this.
        // if it is not, return the closest IExpandable item position whose level is not negative
        val level: Int = if (item is IExpandable<*>) {
            (item as IExpandable<T>).level
        } else {
            Integer.MAX_VALUE
        }
        if (level == 0) {
            return position
        } else if (level == -1) {
            return -1
        }

        for (i in position downTo 0) {
            val temp = dataSrc[i]
            if (temp is IExpandable<*>) {
                val expandable = temp as IExpandable<T>
                if (expandable.level in 0..(level - 1)) {
                    return i
                }
            }
        }
        return -1
    }

    /**
     * Interface definition for a callback to be invoked when an itemchild in this
     * view has been clicked
     */
    interface OnItemChildClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param view     The view whihin the ItemView that was clicked
         * @param position The position of the view int the adapter
         */
        fun onItemChildClick(adapter: BaseAdapter<*, *>, view: View, position: Int)
    }


    /**
     * Interface definition for a callback to be invoked when an childView in this
     * view has been clicked and held.
     */
    interface OnItemChildLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param view     The childView whihin the itemView that was clicked and held.
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        fun onItemChildLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int): Boolean
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * view has been clicked and held.
     */
    interface OnItemLongClickListener {
        /**
         * callback method to be invoked when an item in this view has been
         * click and held
         *
         * @param adapter  the adpater
         * @param view     The view whihin the RecyclerView that was clicked and held.
         * @param position The position of the view int the adapter
         * @return true if the callback consumed the long click ,false otherwise
         */
        fun onItemLongClick(adapter: BaseAdapter<*, *>, view: View, position: Int): Boolean
    }


    /**
     * Interface definition for a callback to be invoked when an item in this
     * RecyclerView itemView has been clicked.
     */
    interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this RecyclerView has
         * been clicked.
         *
         * @param adapter  the adpater
         * @param view     The itemView within the RecyclerView that was clicked (this
         * will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        fun onItemClick(adapter: BaseAdapter<*, *>, view: View, position: Int)
    }

    /**
     *
     */
    private fun getClickPosition(holder: XViewHolder): Int {
        if (holder.layoutPosition >= headerLayoutCount) {
            return holder.layoutPosition - headerLayoutCount
        }
        return 0
    }

    override fun onItemChildClick(view: View, holder: XViewHolder) {
        onItemChildClickListener?.onItemChildClick(this, view, getClickPosition(holder))
    }

    override fun onItemChildLongClick(view: View, holder: XViewHolder) {
        onItemChildLongClickListener?.onItemChildLongClick(this, view, getClickPosition(holder))
    }
}
