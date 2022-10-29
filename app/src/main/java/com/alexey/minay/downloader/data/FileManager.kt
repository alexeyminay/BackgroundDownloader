package com.alexey.minay.downloader.data

import android.content.Context
import android.os.Build
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService

class FileManager(
    private val applicationContext: Context
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkCanAllocatedBytes(): Long {
        val storageManager = applicationContext.getSystemService<StorageManager>() ?: throw RuntimeException()
        val appSpecificInternalDirUuid = storageManager.getUuidForPath(applicationContext.filesDir)
        return storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
    }


}