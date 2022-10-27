package com.alexey.minay.downloader.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.alexey.minay.DownloaderResearch.databinding.ItemContentBinding

class ContentAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<Content, ContentViewHolder>(ContentDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContentViewHolder(ItemContentBinding.inflate(inflater, parent, false), onClick)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

data class ContentViewHolder(
    private val binding: ItemContentBinding,
    private val onClick: (String) -> Unit
) : ViewHolder(binding.root) {

    fun bind(content: Content) {
        binding.text.text = content.title
        onClick(content.url)
    }

}

data class Content(
    val url: String,
    val progress: Int,
    val title: String
)

object ContentDiffUtilCallback : DiffUtil.ItemCallback<Content>() {
    override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem == newItem
    }

}