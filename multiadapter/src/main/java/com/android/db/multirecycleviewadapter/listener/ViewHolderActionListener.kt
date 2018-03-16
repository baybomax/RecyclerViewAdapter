package com.android.db.multirecycleviewadapter.listener

import android.view.View
import com.android.db.multirecycleviewadapter.XViewHolder

/**
 * ViewHolder action
 *
 * Created by DengBo on 15/03/2018.
 */

interface ViewHolderActionListener {

    /**
     * ViewHolder child item click
     */
    fun onItemChildClick(view: View, holder: XViewHolder)

    /**
     * ViewHolder child item long click
     */
    fun onItemChildLongClick(view: View, holder: XViewHolder)

}
