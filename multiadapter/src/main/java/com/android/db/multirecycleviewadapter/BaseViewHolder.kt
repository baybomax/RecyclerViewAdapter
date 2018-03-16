package com.android.db.multirecycleviewadapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.text.util.Linkify
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import com.android.db.multirecycleviewadapter.listener.ViewHolderActionListener

/**
 * Simple view holder
 *
 * Created by DengBo on 15/03/2018.
 */

open class BaseViewHolder(private val view: View): XViewHolder(view) {

    val nestViewIds = HashSet<Int>()
    val childClickViewIds = LinkedHashSet<Int>()
    val childLongClickViewIds = LinkedHashSet<Int>()

    var onItemChildListener: ViewHolderActionListener? = null

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The XViewHolder for chaining.
     */
    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): BaseViewHolder {
        getView<View>(viewId)?.setBackgroundColor(color)
        return this
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The XViewHolder for chaining.
     */
    fun setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): BaseViewHolder {
        getView<View>(viewId)?.setBackgroundResource(backgroundRes)
        return this
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The XViewHolder for chaining.
     */
    fun setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): BaseViewHolder {
        getView<TextView>(viewId)?.setTextColor(textColor)
        return this
    }


    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The XViewHolder for chaining.
     */
    fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable): BaseViewHolder {
        getView<ImageView>(viewId)?.setImageDrawable(drawable)
        return this
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap): BaseViewHolder {
        getView<ImageView>(viewId)?.setImageBitmap(bitmap)
        return this
    }

    @SuppressLint("ObsoleteSdkInt")
    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    fun setAlpha(@IdRes viewId: Int, value: Float): BaseViewHolder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId)?.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            getView<View>(viewId)?.startAnimation(
                    AlphaAnimation(value, value).apply {
                        duration = 0
                        fillAfter = true
                    }
            )
        }
        return this
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The id of the TextView to linkify.
     * @return The XViewHolder for chaining.
     */
    fun addLink(@IdRes viewId: Int): BaseViewHolder {
        getView<TextView>(viewId)?.let {
            Linkify.addLinks(it, Linkify.ALL)
        }
        return this
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    fun setTypeface(@IdRes viewId: Int, typeface: Typeface): BaseViewHolder {
        getView<TextView>(viewId)?.apply {
            this.typeface = typeface
            paintFlags = paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    fun setTypeface(typeface: Typeface, vararg viewIds: Int): BaseViewHolder {
        for (viewId in viewIds) {
            getView<TextView>(viewId)?.apply {
                this.typeface = typeface
                paintFlags = paintFlags or Paint.SUBPIXEL_TEXT_FLAG
            }
        }
        return this
    }

    /**
     * Sets the progress of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @return The XViewHolder for chaining.
     */
    fun setProgress(@IdRes viewId: Int, progress: Int): BaseViewHolder {
        getView<ProgressBar>(viewId)?.progress = progress
        return this
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The XViewHolder for chaining.
     */
    fun setProgress(@IdRes viewId: Int, progress: Int, max: Int): BaseViewHolder {
        getView<ProgressBar>(viewId)?.apply {
            this.max = max
            this.progress = progress
        }
        return this
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The XViewHolder for chaining.
     */
    fun setMax(@IdRes viewId: Int, max: Int): BaseViewHolder {
        getView<ProgressBar>(viewId)?.max = max
        return this
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @return The XViewHolder for chaining.
     */
    fun setRating(@IdRes viewId: Int, rating: Float): BaseViewHolder {
        getView<RatingBar>(viewId)?.rating = rating
        return this
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The XViewHolder for chaining.
     */
    fun setRating(@IdRes viewId: Int, rating: Float, max: Int): BaseViewHolder {
        getView<RatingBar>(viewId)?.apply {
            this.max = max
            this.rating = rating
        }
        return this
    }

    /**
     * add childView id
     *
     * @param viewId add the child view id   can support childview click
     * @return if you use adapter bind listener
     * @link {(adapter.setOnItemChildClickListener(listener))}
     *
     * or if you can use  recyclerView.addOnItemTouch(listerer)  wo also support this menthod
     */
    fun addOnClickListener(@IdRes viewId: Int): BaseViewHolder {
        childClickViewIds.add(viewId)
        getView<View>(viewId)?.apply {
            isClickable = true
            setOnClickListener {
                onItemChildListener?.onItemChildClick(it, this@BaseViewHolder)
            }
        }
        return this
    }


    /**
     * set nestview id
     *
     * @param viewId add the child view id   can support childview click
     * @return
     */
    fun setNestView(@IdRes viewId: Int): BaseViewHolder {
        addOnClickListener(viewId)
        addOnLongClickListener(viewId)
        nestViewIds.add(viewId)
        return this
    }

    /**
     * add long click view id
     *
     * @param viewId
     * @return if you use adapter bind listener
     * @link {(adapter.setOnItemChildLongClickListener(listener))}
     *
     *
     * or if you can use  recyclerView.addOnItemTouch(listerer)  wo also support this menthod
     */
    fun addOnLongClickListener(@IdRes viewId: Int): BaseViewHolder {
        childLongClickViewIds.add(viewId)
        val view = getView<View>(viewId)?.apply {
            isLongClickable = true
            setOnLongClickListener {
                onItemChildListener?.onItemChildLongClick(it, this@BaseViewHolder)
                true
            }
        }
        return this
    }


    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The XViewHolder for chaining.
     */
    @Deprecated("deprecated")
    fun setOnTouchListener(@IdRes viewId: Int, listener: View.OnTouchListener): BaseViewHolder {
        getView<View>(viewId)?.setOnTouchListener(listener)
        return this
    }

    /**
     * Sets the listview or gridview's item selected click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item selected click listener;
     * @return The XViewHolder for chaining.
     */
    fun setOnItemSelectedClickListener(@IdRes viewId: Int, listener: AdapterView.OnItemSelectedListener): BaseViewHolder {
        getView<AdapterView<*>>(viewId)?.onItemSelectedListener = listener
        return this
    }

    /**
     * Sets the on checked change listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The checked change listener of compound button.
     * @return The XViewHolder for chaining.
     */
    fun setOnCheckedChangeListener(@IdRes viewId: Int, listener: CompoundButton.OnCheckedChangeListener): BaseViewHolder {
        getView<CompoundButton>(viewId)?.setOnCheckedChangeListener(listener)
        return this
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The XViewHolder for chaining.
     */
    fun setTag(@IdRes viewId: Int, tag: Any): BaseViewHolder {
        getView<View>(viewId)?.tag = tag
        return this
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The XViewHolder for chaining.
     */
    fun setTag(@IdRes viewId: Int, key: Int, tag: Any): BaseViewHolder {
        getView<View>(viewId)?.setTag(key, tag)
        return this
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The XViewHolder for chaining.
     */
    fun setChecked(@IdRes viewId: Int, checked: Boolean): BaseViewHolder {
        val view = getView<View>(viewId)?.apply {
            if (this is Checkable) {
                isChecked = checked
            }
        }
        return this
    }

    /**
     * Sets the XAdapter of a XAdapter view.
     *
     * @param viewId  The view id.
     * @param XAdapter The XAdapter;
     * @return The XViewHolder for chaining.
     */
    fun setAdapter(@IdRes viewId: Int, adapter: Adapter): BaseViewHolder {
        getView<AdapterView<*>>(viewId)?.adapter = adapter
        return this
    }

    /**
     * Sets the listener of a adapter view.
     *
     * @param onItemChildListener The listener.
     * @return The XViewHolder for chaining.
     */
    fun setOnItemChildListener(onItemChildListener: ViewHolderActionListener): BaseViewHolder {
        this.onItemChildListener = onItemChildListener
        return this
    }

}
