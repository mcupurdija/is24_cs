package com.mcupurdija.is24_cs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mcupurdija.is24_cs.databinding.ItemRepoBinding
import com.mcupurdija.is24_cs.networking.schema.RepoSchema

class RepoListAdapter : RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {

    inner class RepoViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<RepoSchema>() {
        override fun areItemsTheSame(oldItem: RepoSchema, newItem: RepoSchema): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RepoSchema, newItem: RepoSchema): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((RepoSchema) -> Unit)? = null

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = differ.currentList[position]
        holder.itemView.apply {

            holder.binding.title.text = String.format("%s: %s", "Name", repo?.name.orEmpty())
            holder.binding.language.text =
                String.format("%s: %s", "Language", repo?.language.orEmpty())
            holder.binding.numberOfWatchers.text =
                String.format("%s: %s", "Number of watchers", repo?.watchers?.toString().orEmpty())

            setOnClickListener {
                onItemClickListener?.let { it(repo) }
            }
        }
    }

    fun setOnItemClickListener(listener: (RepoSchema) -> Unit) {
        onItemClickListener = listener
    }
}