package com.example.android.politicalpreparedness.features.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) : ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val electionItem = getItem(position) as Election
        holder.bind(electionItem,clickListener)
    }


    class ElectionViewHolder(private val binding: ItemElectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(electionItem: Election, clickListener: ElectionListener) {
            binding.tvElectionDate.text = electionItem.electionDay.toString()
            binding.tvElectionTitle.text = electionItem.name
            binding.root.setOnClickListener {
                clickListener.onElectionSelected(electionItem)
            }
        }

        companion object{
            fun from(parent:ViewGroup): ElectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemElectionBinding.inflate(layoutInflater,parent,false)
                return ElectionViewHolder(binding)
            }
        }
    }
}

interface ElectionListener {
    fun onElectionSelected(election:Election)
}

class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }


}