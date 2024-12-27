package com.example.storyapp__subs1.ui.story

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp__subs1.data.respons.ListStoryItem
import com.example.storyapp__subs1.databinding.StoryItemsBinding
import com.example.storyapp__subs1.ui.story.detail.DetailStoryAcitivity

class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: StoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ListStoryItem) {
            binding.textViewUsername.text = data.name

            Glide.with(binding.root.context)
                .load(data.photoUrl)
                .into(binding.imageviewStory)

            binding.root.setOnClickListener {
                val intent = Intent(it.context, DetailStoryAcitivity::class.java)
                val storyData = StoryModel(
                    data.photoUrl,
                    data.name,
                    data.description
                )

                intent.putExtra(DetailStoryAcitivity.STORY_KEY, storyData)
                it.context.startActivity(intent)
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
