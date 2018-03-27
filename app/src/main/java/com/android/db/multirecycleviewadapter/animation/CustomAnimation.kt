package com.android.db.multirecycleviewadapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

class CustomAnimation(override val duration: Long = 300,
                      override val interpolator: LinearInterpolator = LinearInterpolator()) : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f))
    }
}
