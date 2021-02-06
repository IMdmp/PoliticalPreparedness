package com.example.android.politicalpreparedness.features.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ItemRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.features.representative.model.Representative
import timber.log.Timber

class RepresentativeListAdapter(private val representativeListener: RepresentativeListener)
    : ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, representativeListener)
    }
}

class RepresentativeDiffCallback() : DiffUtil.ItemCallback<Representative>() {
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem.official.name.equals(newItem.official.name)
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }

}

class RepresentativeViewHolder(val binding: ItemRepresentativeBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): RepresentativeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRepresentativeBinding.inflate(layoutInflater, parent, false)
            return RepresentativeViewHolder(binding)
        }
    }

    fun bind(item: Representative, representativeListener: RepresentativeListener) {
        Timber.d("binding.")
        binding.representative = item

        val channels = item.official.channels
        val urls = item.official.urls
        channels?.let {  channel ->
            showSocialLinks(channel)

            binding.facebookIcon.setOnClickListener {
                val fbUrl = getFacebookUrl(channel)
                fbUrl?.let { fb ->
                    representativeListener.openUrl(fb)
                }
            }

            binding.twitterIcon.setOnClickListener {
                val twitterUrl = getTwitterUrl(channel)
                twitterUrl?.let { twitter ->
                    representativeListener.openUrl(twitter)
                }
            }
        }
        urls?.let { urlList ->
            showWWWLinks(urlList)
            binding.wwwIcon.setOnClickListener {
                representativeListener.openUrl(urlList.first())
            }
        }
        binding.executePendingBindings()
    }

    private fun showSocialLinks(channels: List<Channel>) {
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) {
            enableLink(binding.facebookIcon, facebookUrl)
        }

        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) {
            enableLink(binding.twitterIcon, twitterUrl)
        }
    }

    private fun showWWWLinks(urls: List<String>) {
        enableLink(binding.wwwIcon, urls.first())
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
                .map { channel -> "https://www.facebook.com/${channel.id}" }
                .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
                .map { channel -> "https://www.twitter.com/${channel.id}" }
                .firstOrNull()
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }

}

interface RepresentativeListener {
    fun openUrl(url: String)
}