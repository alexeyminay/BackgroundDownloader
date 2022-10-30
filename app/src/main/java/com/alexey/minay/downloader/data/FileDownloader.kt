package com.alexey.minay.downloader.data

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.net.HttpURLConnection

class FileDownloader() {

    private val mOkHttpClient = OkHttpClient.Builder()
        .build()

    fun download(url: String, file: File) = flow {
        val request = Request.Builder().url(url)
            .build()
        val response = mOkHttpClient.newCall(request).execute()
        val body = response.body()
        val responseCode = response.code()
        if (responseCode >= HttpURLConnection.HTTP_OK &&
            responseCode < HttpURLConnection.HTTP_MULT_CHOICE &&
            body != null
        ) {
            val length = body.contentLength()
            if (length <= 0) {
                emit(Result.Error)
                return@flow
            }
            val byteStream = body.byteStream()

            file.outputStream().use { fileOut ->
                var bytesCopied = 0L
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE * 5)
                var bytes = byteStream.read(buffer, 0, DEFAULT_BUFFER_SIZE * 5)
                while (bytes >= 0 && currentCoroutineContext().isActive) {
                    fileOut.write(buffer, 0, bytes)
                    bytesCopied += bytes
                    bytes = byteStream.read(buffer)
                    emit(Result.Progress(progress = bytesCopied, total = length))
                }
            }
        } else {
            emit(Result.Error)
            return@flow
        }

        emit(Result.Success)
    }

}

interface Result {
    object Success : Result
    class Progress(val progress: Long, val total: Long) : Result
    object Error : Result
}