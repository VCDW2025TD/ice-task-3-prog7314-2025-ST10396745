// ui/feed/SimpleMemeAdapter.kt
package com.example.memestreamproto.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.memestreamproto.R
import com.example.memestreamproto.data.local.MemeEntity

class SimpleMemeAdapter :
    ListAdapter<MemeEntity, SimpleMemeAdapter.VH>(Diff) {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val url: TextView = itemView.findViewById(R.id.tvUrl)
        val tags: TextView = itemView.findViewById(R.id.tvTags)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meme, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val meme = getItem(position)
        holder.title.text = meme.title
        holder.url.text = meme.imageUrl
        holder.tags.text = meme.tags.joinToString(", ")
    }

    object Diff : DiffUtil.ItemCallback<MemeEntity>() {
        override fun areItemsTheSame(oldItem: MemeEntity, newItem: MemeEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MemeEntity, newItem: MemeEntity) = oldItem == newItem
    }
}
