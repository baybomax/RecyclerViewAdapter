package com.android.db.multirecycleviewadapter.adapter

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.android.db.multirecycleviewadapter.BaseAdapter
import com.android.db.multirecycleviewadapter.BaseViewHolder
import com.android.db.multirecycleviewadapter.R
import com.android.db.multirecycleviewadapter.data.DataServer
import com.android.db.multirecycleviewadapter.ientity.Status
import com.android.db.multirecycleviewadapter.util.ClickableMovementMethod
import com.android.db.multirecycleviewadapter.util.SpannableStringUtils
import com.android.db.multirecycleviewadapter.util.Utils

class AnimationAdapter: BaseAdapter<Status, BaseViewHolder>(
        R.layout.layout_animation, DataServer.getSampleData(100)) {

    private var clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
//            ToastUtils.showShortToast("事件触发了 landscapes and needs")
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = Utils.getContext().resources.getColor(R.color.clickspan_color)
            ds.isUnderlineText = true
        }
    }

    override fun convert(helper: BaseViewHolder, item: Status?) {
        helper.addOnClickListener(R.id.img).addOnClickListener(R.id.tweetName)
        when (helper.layoutPosition % 3) {
            0 -> helper.setImageResource(R.id.img, R.mipmap.animation_img1)
            1 -> helper.setImageResource(R.id.img, R.mipmap.animation_img2)
            2 -> helper.setImageResource(R.id.img, R.mipmap.animation_img3)
        }
        helper.setText(R.id.tweetName, "Hoteis in Rio de Janeiro")
        val msg = "\"He was one of Australia's most of distinguished artistes, renowned for his portraits\""
        (helper.getView<View>(R.id.tweetText) as TextView).text = SpannableStringUtils.getBuilder(msg).append("landscapes and nedes").setClickSpan(clickableSpan).create()
        (helper.getView<View>(R.id.tweetText) as TextView).movementMethod = ClickableMovementMethod.instance
        (helper.getView<View>(R.id.tweetText) as TextView).isFocusable = false
        (helper.getView<View>(R.id.tweetText) as TextView).isClickable = false
        (helper.getView<View>(R.id.tweetText) as TextView).isLongClickable = false
    }
}
