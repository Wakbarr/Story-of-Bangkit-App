package com.example.storyapp__subs1.ui.story.detail

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.storyapp__subs1.R
import com.example.storyapp__subs1.databinding.ActivityStoryDetailBinding
import com.example.storyapp__subs1.ui.story.StoryModel

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    companion object {
        const val STORY_KEY = "story_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(STORY_KEY, StoryModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(STORY_KEY)
        }

        storyData?.let {
            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.storyPhoto)

            binding.textViewName.text = it.name
            binding.textViewDesc.text = it.desc
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
