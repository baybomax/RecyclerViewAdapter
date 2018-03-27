package com.android.db.multirecycleviewadapter.base

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.android.db.multirecycleviewadapter.R

open class BaseActivity : AppCompatActivity() {
    private var title: TextView? = null
    private var back: ImageView? = null
    protected val TAG = this.javaClass.simpleName

    private var rootLayout: LinearLayout? = null

    protected fun setTitle(msg: String) {
        if (title != null) {
            title!!.text = msg
        }
    }

    /**
     * sometime you want to define back event
     */
    protected fun setBackBtn() {
        if (back != null) {
            back!!.visibility = View.VISIBLE
            back!!.setOnClickListener { finish() }
        }
    }

    protected fun setBackClickListener(l: View.OnClickListener) {
        if (back != null) {
            back!!.visibility = View.VISIBLE
            back!!.setOnClickListener(l)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        super.setContentView(R.layout.activity_base)
        initToolbar()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        if (supportActionBar != null) {
            // Enable the Up button
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setDisplayShowTitleEnabled(false)

        }
        back = findViewById<ImageView>(R.id.img_back)
        title = findViewById<View>(R.id.title) as TextView
    }

    override fun setContentView(layoutId: Int) {
        setContentView(View.inflate(this, layoutId, null))
    }

    override fun setContentView(view: View) {
        rootLayout = findViewById<LinearLayout>(R.id.root_layout)
        if (rootLayout == null) return
        rootLayout!!.addView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        initToolbar()
    }
}
