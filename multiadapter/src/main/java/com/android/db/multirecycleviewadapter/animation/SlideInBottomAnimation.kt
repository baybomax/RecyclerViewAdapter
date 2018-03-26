package com.android.db.multirecycleviewadapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Slide in bottom animation
 *
 * Created by DengBo on 14/03/2018.
 */

class SlideInBottomAnimation(override val duration: Long = 300,
                             override val interpolator: LinearInterpolator = LinearInterpolator()): BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(
                ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0f).apply {
                    duration = this@SlideInBottomAnimation.duration
                    interpolator = this@SlideInBottomAnimation.interpolator
                }
        )
    }

}
