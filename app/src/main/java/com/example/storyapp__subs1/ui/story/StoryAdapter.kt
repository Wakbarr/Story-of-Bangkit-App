package com.example.storyapp__subs1.ui.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp__subs1.R
import com.example.storyapp__subs1.data.respons.ListStoryItem


class StoryAdapter (private val listStories: List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgStory: ImageView = itemView.findViewById(R.id.imageview_story)
        val tvUsername: TextView = itemView.findViewById(R.id.textView_username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.story_items, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int  = listStories.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = listStories[position]

        holder.tvUsername.text = story.name

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .into(holder.imgStory)

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listStories[holder.adapterPosition]) }
    }
}
