package com.alexey.minay.downloader.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.alexey.minay.DownloaderResearch.R
import com.alexey.minay.downloader.data.FileDownloader
import com.alexey.minay.downloader.data.Result.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.File

class DownloadWorker(
    context: Context,
    parameters: WorkerParameters,
    private val downloader: FileDownloader,
) : CoroutineWorker(context, parameters) {

    private val mNotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val contentId = inputData.getString(KEY_CONTENT_ID) ?: ""
        val inputTitle = inputData.getString(KEY_INPUT_TITLE) ?: ""
        return createForegroundInfo(0, contentId, inputTitle)
    }

    override suspend fun doWork(): Result {
        val inputUrl = inputData.getString(KEY_INPUT_URL) ?: return Result.failure()
        val inputTitle = inputData.getString(KEY_INPUT_TITLE) ?: return Result.failure()
        val outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME) ?: return Result.failure()

        val contentId = inputData.getString(KEY_CONTENT_ID) ?: ""
        download(inputUrl, outputFile, contentId, inputTitle)
        return Result.success()
    }

    private suspend fun download(
        inputUrl: String,
        outputFile: String,
        contentId: String,
        inputTitle: String
    ) {
        val file = File(applicationContext.filesDir, outputFile)
        downloader.download(inputUrl, file)
            .flowOn(Dispatchers.IO)
            .collect { result ->
                when (result) {
                    is Success -> {
                        Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                    }
                    is Progress -> {
                        val progress = (result.progress * 100f / result.total).toInt()
                        coroutineScope {
                            launch {
                                setForeground(
                                    createForegroundInfo(
                                        progress = progress,
                                        contentId = contentId,
                                        inputTitle = inputTitle
                                    )
                                )
                            }
                            launch { setProgress(workDataOf(KEY_PROGRESS to progress)) }
                        }
                    }
                    is Error -> {
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun createForegroundInfo(
        progress: Int,
        contentId: String,
        inputTitle: String
    ): ForegroundInfo {
        val channelId = applicationContext.getString(R.string.notification_channel_id)
        val title = applicationContext.getString(R.string.notification_title)
        val cancel = applicationContext.getString(R.string.cancel_download)

        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(channelId)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(inputTitle)
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
        const val KEY_INPUT_TITLE = "key_input_title"
        const val KEY_OUTPUT_FILE_NAME = "key_output_file_name"
        const val KEY_CONTENT_ID = "key_content_id"
        const val KEY_PROGRESS = "key_progress"
    }

}