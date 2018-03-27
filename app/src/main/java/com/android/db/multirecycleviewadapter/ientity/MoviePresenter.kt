package com.android.db.multirecycleviewadapter.ientity

import android.view.View
import android.widget.Toast

class MoviePresenter {
    fun buyTicket(view: View, movie: Movie) {
        Toast.makeText(view.context, "buy ticket: " + movie.name, Toast.LENGTH_SHORT).show()
    }
}
