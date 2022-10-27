package com.alexey.minay.downloader.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DownloadWorker(context: Context, parameters: WorkerParameters): CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }

}