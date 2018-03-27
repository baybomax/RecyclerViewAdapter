package com.android.db.multirecycleviewadapter.base

import android.databinding.ViewDataBinding
import android.view.View

import com.android.db.multirecycleviewadapter.BaseViewHolder

class BaseBindingViewHolder<Binding : ViewDataBinding>(view: View) : BaseViewHolder(view) {
    var binding: Binding? = null
}
