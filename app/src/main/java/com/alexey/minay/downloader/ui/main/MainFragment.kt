package com.alexey.minay.downloader.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.alexey.minay.DownloaderResearch.R
import com.alexey.minay.downloader.worker.DownloadWorker

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val mAdapter by lazy { ContentAdapter { download(it) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.list).adapter = mAdapter
        mAdapter.submitList(
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
    }

    private fun download(url: String) {
        val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .addTag("download")
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(workDataOf(
                DownloadWorker.KEY_INPUT_URL to url
            ))
            .build()
        WorkManager.getInstance(requireContext().applicationContext).enqueue(downloadWorkRequest)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}
