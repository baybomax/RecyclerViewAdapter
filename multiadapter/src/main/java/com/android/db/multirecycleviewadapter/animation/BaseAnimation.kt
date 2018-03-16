package com.android.db.multirecycleviewadapter.animation

import android.animation.Animator
import android.view.View

/**
 * Base animation interface
 *
 * Created by DengBo on 14/03/2018.
 */

interface BaseAnimation {

    /**
     * Return a array of animators
     *
     * @param view
     * @return #Array[Array]
     */
    fun getAnimators(view: View): Array<Animator>

}
