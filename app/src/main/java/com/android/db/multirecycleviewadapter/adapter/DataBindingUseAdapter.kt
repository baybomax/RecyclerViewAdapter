package com.android.db.multirecycleviewadapter.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import android.view.ViewGroup
import com.android.db.multirecycleviewadapter.BR
import com.android.db.multirecycleviewadapter.BaseAdapter
import com.android.db.multirecycleviewadapter.BaseViewHolder
import com.android.db.multirecycleviewadapter.R
import com.android.db.multirecycleviewadapter.ientity.Movie
import com.android.db.multirecycleviewadapter.ientity.MoviePresenter

class DataBindingUseAdapter(layoutResId: Int, data: List<Movie>)
    : BaseAdapter<Movie, DataBindingUseAdapter.MovieViewHolder>(layoutResId, data) {

    private val mPresenter: MoviePresenter = MoviePresenter()

    override fun convert(helper: MovieViewHolder, item: Movie?) {
        val binding = helper.binding
        binding.setVariable(BR.movie, item)
        binding.setVariable(BR.presenter, mPresenter)
        binding.executePendingBindings()
        when (helper.layoutPosition % 2) {
            0 -> helper.setImageResource(R.id.iv, R.mipmap.m_img1)
            1 -> helper.setImageResource(R.id.iv, R.mipmap.m_img2)
        }
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding = if (null != layoutInflater) {
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutResId, parent, false)
        } else
            return super.getItemView(layoutResId, parent)
        return binding.root.apply {
            setTag(R.id.base_adapter_data_binding_support, binding)
        }
    }

    class MovieViewHolder(view: View) : BaseViewHolder(view) {
        val binding: ViewDataBinding
            get() = itemView.getTag(R.id.base_adapter_data_binding_support) as ViewDataBinding
    }
}
