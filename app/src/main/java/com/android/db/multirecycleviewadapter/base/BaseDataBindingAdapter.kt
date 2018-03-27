package com.android.db.multirecycleviewadapter.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup

import com.android.db.multirecycleviewadapter.BaseAdapter

abstract class BaseDataBindingAdapter<T, Binding : ViewDataBinding> : BaseAdapter<T, BaseBindingViewHolder<Binding>> {

    constructor(@LayoutRes layoutResId: Int, data: List<T>?) : super(layoutResId, data) {}

    constructor(data: List<T>?) : super(0, data) {}

    constructor(@LayoutRes layoutResId: Int) : super(layoutResId, null) {}

    override fun createBaseViewHolder(view: View): BaseBindingViewHolder<Binding> {
        return BaseBindingViewHolder(view)
    }

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): BaseBindingViewHolder<Binding> {
        val binding = DataBindingUtil.inflate<Binding>(layoutInflater!!, layoutResId, parent, false)
        val view: View
        if (binding == null) {
            view = getItemView(layoutResId, parent)
        } else {
            view = binding.root
        }
        val holder = BaseBindingViewHolder<Binding>(view)
        holder.binding = binding
        return holder
    }

    override fun convert(helper: BaseBindingViewHolder<Binding>, item: T?) {
        convert(helper.binding, item)
        helper.binding!!.executePendingBindings()
    }

    protected abstract fun convert(binding: Binding?, item: T?)
}
