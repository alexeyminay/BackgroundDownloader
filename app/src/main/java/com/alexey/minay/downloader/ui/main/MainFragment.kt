package com.alexey.minay.downloader.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.alexey.minay.DownloaderResearch.R
import com.alexey.minay.downloader.worker.DownloadWorker
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val mAdapter by lazy { ContentAdapter { url, fileName -> download(url, fileName) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.list).adapter = mAdapter
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.onEach { mAdapter.submitList(it) }
                .launchIn(this)
        }
    }

    private fun download(url: String, fileName: String) {
        val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .addTag("download")
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setInputData(
                workDataOf(
                    DownloadWorker.KEY_INPUT_URL to url,
                    DownloadWorker.KEY_OUTPUT_FILE_NAME to fileName,
                    DownloadWorker.KEY_CONTENT_ID to url,
                    DownloadWorker.KEY_INPUT_TITLE to fileName
                )
            )
            .build()

        WorkManager.getInstance(requireContext().applicationContext)
            .enqueueUniqueWork(url, ExistingWorkPolicy.KEEP, downloadWorkRequest)

        //work watcher
        WorkManager.getInstance(requireContext().applicationContext)
            .getWorkInfoByIdLiveData(downloadWorkRequest.id)
            .observe(viewLifecycleOwner) { workInfo ->
                Log.d("DownloadWorker", "WorkInfo $workInfo")
                if (workInfo != null) {
                    val progress = workInfo.progress.getInt(DownloadWorker.KEY_PROGRESS, 0)
                    viewModel.updateProgress(url, progress)
                }
            }

    }

    companion object {
        fun newInstance() = MainFragment()
    }

}
