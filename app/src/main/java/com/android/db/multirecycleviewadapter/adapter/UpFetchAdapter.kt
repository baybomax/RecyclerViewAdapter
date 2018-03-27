package com.android.db.multirecycleviewadapter.adapter

import com.android.db.multirecycleviewadapter.R
import com.android.db.multirecycleviewadapter.base.BaseDataBindingAdapter
import com.android.db.multirecycleviewadapter.databinding.ItemMovieBinding
import com.android.db.multirecycleviewadapter.ientity.Movie

class UpFetchAdapter : BaseDataBindingAdapter<Movie, ItemMovieBinding>(R.layout.item_movie, null) {
    override fun convert(binding: ItemMovieBinding?, item: Movie?)  {
        binding?.movie = item
    }
}
