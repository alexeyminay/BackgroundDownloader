package com.alexey.minay.downloader.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {

    val state = MutableStateFlow(
        listOf(
            Content(
                progress = 0,
                title = "first",
                url = "https://firebasestorage.googleapis.com/v0/b/downloader-9f4f3.appspot.com/o/video%20(video-converter.com)%20(1).mp4?alt=media&token=08a8fadc-65b8-4755-acbf-7bbcd35df1f7"
            ),
            Content(
                progress = 0,
                title = "second",
                url = "https://firebasestorage.googleapis.com/v0/b/downloader-9f4f3.appspot.com/o/video%20(video-converter.com).mpg?alt=media&token=bc893562-6e49-441a-846e-9ef3b5ed57f9"
            )
        )
    )

    fun updateProgress(url: String, progress: Int) {
        state.value = state.value.map {
            if (it.url == url) {
                it.copy(progress = progress)
            } else {
                it
            }
        }
    }

}