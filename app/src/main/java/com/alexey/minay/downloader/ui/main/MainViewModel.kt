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
            ),
            Content(
                progress = 0,
                title = "third",
                url = "https://drive.google.com/u/2/uc?id=1Ai7YDHFypxAMsEy08E_4egXMTvaLqLZ1&export=download"
            ),
            Content(
                progress = 0,
                title = "forth",
                url = "https://aflet.ispringlearn.ru/proxy/learn-cnode-0/content/200123-BEjaq-6tWuf-z3f63/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdSI6ImlzbHJ1LTIwMDEyMyIsInV1IjoiNDMzMGM0MTItNjZjNi0xMWViLWI3NTctMGUzM2E1ZmQ0NDBlIiwiY2siOiIyMDAxMjMtQkVqYXEtNnRXdWYtejNmNjMiLCJjaSI6IjI4ZTc0MTQ0LTYyMmUtMTFlYy1hMzU5LTU2ZjU3Y2ViMWM2NSIsImFtIjoyLCJldCI6MTY2NzY3ODQ0Mn0.d9Q613mxwL-b7K9xsNEtTBje4-GFpLGfAfGNQsD2elc/video.mp4"
            ),
            Content(
                progress = 0,
                title = "fifth",
                url = "https://aflet.ispringlearn.ru/proxy/learn-cnode-0/content/200123-pqCDQ-fP7GN-Kk3jj/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdSI6ImlzbHJ1LTIwMDEyMyIsInV1IjoiNDMzMGM0MTItNjZjNi0xMWViLWI3NTctMGUzM2E1ZmQ0NDBlIiwiY2siOiIyMDAxMjMtcHFDRFEtZlA3R04tS2szamoiLCJjaSI6IjI4ZDlmYmY2LTYyMmUtMTFlYy1iYzRlLTU2ZjU3Y2ViMWM2NSIsImFtIjoyLCJldCI6MTY2NzY3ODU5M30.ilOKyIbB26_gbOKogHkpsm19D9I7VPXEUAnH6Q75qEM/video.mp4"
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