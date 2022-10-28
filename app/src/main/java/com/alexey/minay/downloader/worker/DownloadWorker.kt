package com.alexey.minay.downloader.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.alexey.minay.DownloaderResearch.R
import kotlinx.coroutines.delay
import kotlin.random.Random

class DownloadWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    private val mNotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val contentId = inputData.getString(KEY_CONTENT_ID) ?: ""
        return createForegroundInfo(0, contentId)
    }

    override suspend fun doWork(): Result {
        val inputUrl = inputData.getString(KEY_INPUT_URL) ?: return Result.failure()
        val outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME) ?: return Result.failure()

        val contentId = inputData.getString(KEY_CONTENT_ID) ?: ""
        download(inputUrl, outputFile, contentId)
        return Result.success()
    }

    private suspend fun download(inputUrl: String, outputFile: String, contentId: String) {
        repeat(20) {
            try {
                setForeground(createForegroundInfo(it * 5, contentId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            setProgress(workDataOf(KEY_PROGRESS to it * 5))
            delay(Random.nextInt(500, 1200).toLong())
        }
    }

    private fun createForegroundInfo(progress: Int, contentId: String): ForegroundInfo {
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
            .setProgress(100, progress, false)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .addAction(R.drawable.baseline_close_24, cancel, intent)
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