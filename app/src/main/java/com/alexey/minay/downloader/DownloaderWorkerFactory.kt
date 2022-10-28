package com.alexey.minay.downloader

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.alexey.minay.downloader.data.FileDownloader
import com.alexey.minay.downloader.worker.DownloadWorker

class DownloaderWorkerFactory(
    private val downloader: FileDownloader
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        DownloadWorker::class.java.name ->
            DownloadWorker(
                context = appContext,
                parameters = workerParameters,
                downloader = downloader
            )
        else -> null
    }

}