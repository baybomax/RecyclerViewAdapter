package com.android.db.multirecycleviewadapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

/**
 * Slide in left animation
 *
 * Created by DengBo on 14/03/2018.
 */

class SlideInLeftAnimation: BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(
                ObjectAnimator.ofFloat(view, "translationX", -view.rootView.width.toFloat(), 0f)
        )
    }

}
