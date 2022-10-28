package com.alexey.minay.downloader

import android.app.Application
import androidx.work.Configuration
import com.alexey.minay.downloader.data.FileDownloader

class DownloaderApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(DownloaderWorkerFactory(FileDownloader()))
            .build()
    }

}