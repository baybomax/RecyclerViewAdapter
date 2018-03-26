package com.android.db.multirecycleviewadapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Alpha in animation
 *
 * Created by DengBo on 14/03/2018.
 */

class AlphaInAnimation(private var mAlphaFrom: Float = default_alpha_from,
                       override val duration: Long = 300,
                       override val interpolator: LinearInterpolator = LinearInterpolator()): BaseAnimation {

    companion object {
        private const val default_alpha_from = 0f
    }

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(
                ObjectAnimator.ofFloat(view, "alpha", mAlphaFrom, 1f).apply {
                    duration = this@AlphaInAnimation.duration
                    interpolator = this@AlphaInAnimation.interpolator
                }
        )
    }

}
