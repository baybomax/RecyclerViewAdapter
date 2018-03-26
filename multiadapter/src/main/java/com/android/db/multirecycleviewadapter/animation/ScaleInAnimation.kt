package com.android.db.multirecycleviewadapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Scale in animation
 *
 * Created by DengBo on 14/03/2018.
 */

class ScaleInAnimation(private var mScaleFrom: Float = default_scale_from,
                       override val duration: Long = 300,
                       override val interpolator: LinearInterpolator = LinearInterpolator()): BaseAnimation {

    companion object {
        private const val default_scale_from = .5f
    }

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(
                ObjectAnimator.ofFloat(view, "scaleX", mScaleFrom, 1f).apply {
                    duration = this@ScaleInAnimation.duration
                    interpolator = this@ScaleInAnimation.interpolator
                },
                ObjectAnimator.ofFloat(view, "scaleY", mScaleFrom, 1f).apply {
                    duration = this@ScaleInAnimation.duration
                    interpolator = this@ScaleInAnimation.interpolator
                }
        )
    }

}
