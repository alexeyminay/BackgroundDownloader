package com.alexey.minay.downloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alexey.minay.DownloaderResearch.R
import com.alexey.minay.downloader.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}