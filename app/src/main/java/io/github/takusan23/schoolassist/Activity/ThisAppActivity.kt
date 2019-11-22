package io.github.takusan23.schoolassist.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import io.github.takusan23.schoolassist.R
import kotlinx.android.synthetic.main.activity_this_app.*

class ThisAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_this_app)

        this_app_github.setOnClickListener {
            openBrowser("https://github.com/takusan23/SchoolAssist")
        }

        this_app_twitter.setOnClickListener {
            openBrowser("https://twitter.com/takusan__23")
        }

        this_app_mastodon.setOnClickListener {
            openBrowser("https://best-friends.chat/@takusan_23")
        }

    }

    fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }

}
