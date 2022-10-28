package com.alexey.minay.downloader.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.alexey.minay.DownloaderResearch.R
import kotlinx.coroutines.delay

class DownloadWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    private val mNotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val progress = "Starting Download"
        val id = inputData.getString(KEY_CONTENT_ID) ?: ""
        Log.d("DownloadWorker", "ID $id")
        return createForegroundInfo(progress, id)
    }

    override suspend fun doWork(): Result {
        val inputUrl = inputData.getString(KEY_INPUT_URL) ?: return Result.failure()
        val outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME) ?: return Result.failure()

        //зачем если есть getForegroundInfo()?
        try {
            //setForeground(getForegroundInfo())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        download(inputUrl, outputFile)
        return Result.success()
    }

    private suspend fun download(inputUrl: String, outputFile: String) {
        repeat(10) {
            Log.d("DownloadWorker", "DownloadWorker $it")
            setProgress(workDataOf(KEY_PROGRESS to it * 10))
            delay(1000)
        }
    }

    private fun createForegroundInfo(progress: String, contentId: String): ForegroundInfo {
        val channelId = applicationContext.getString(R.string.notification_channel_id)
        val title = applicationContext.getString(R.string.notification_title)
        val cancel = applicationContext.getString(R.string.cancel_download)

        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(channelId)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setProgress(100, 20, false)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        val id = contentId.hashCode()
        return ForegroundInfo(id, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(channelId: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            "notification channel name",
            NotificationManager.IMPORTANCE_HIGH
        )

        mNotificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        const val KEY_INPUT_URL = "key_input_url"
        const val KEY_OUTPUT_FILE_NAME = "key_output_file_name"
        const val KEY_CONTENT_ID = "key_content_id"
        const val KEY_PROGRESS = "key_progress"
    }

}