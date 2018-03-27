package com.android.db.multirecycleviewadapter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        Handler().postDelayed({
            val intent = Intent(this@WelcomeActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
