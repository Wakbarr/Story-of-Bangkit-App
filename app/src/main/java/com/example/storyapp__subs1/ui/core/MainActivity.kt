package com.example.storyapp__subs1.ui.core

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp__subs1.MapsActivity
import com.example.storyapp__subs1.R
import com.example.storyapp__subs1.data.repository.UserViewModelFactory
import com.example.storyapp__subs1.data.respons.ListStoryItem
import com.example.storyapp__subs1.databinding.ActivityMainBinding
import com.example.storyapp__subs1.ui.auth.login.LoginActivity
import com.example.storyapp__subs1.ui.story.StoryAdapter
import com.example.storyapp__subs1.ui.story.StoryModel
import com.example.storyapp__subs1.ui.story.add.AddStoryActivity
import com.example.storyapp__subs1.ui.story.detail.DetailStoryActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        UserViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding

    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewNoInternet.visibility = View.GONE

        binding.progressBarMain.visibility = View.GONE

        binding.addStory.setOnClickListener {
            intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                token = "Bearer ${user.token}"
                viewModel.getStories(token)
            }
        }

        viewModel.stories.observe(this) { listStory ->
            setStoriesData(listStory)
        }


        viewModel.isLoading.observe(this) { loadingState ->
            if (loadingState == true) {
                binding.progressBarMain.visibility = View.VISIBLE
            } else {
                binding.progressBarMain.visibility = View.GONE
            }
        }

        viewModel.message.observe(this) { message ->
            if (message == "Error: No Internet") {
                binding.textViewNoInternet.visibility = View.VISIBLE
            }
            message?.let {
                showToast(it)

            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setStoriesData(listEvents: List<ListStoryItem>) {
        val list = listEvents

        binding.rvStory.layoutManager = LinearLayoutManager(this)
        val StoriesAdapter = StoryAdapter(list)
        binding.rvStory.adapter = StoriesAdapter

        StoriesAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {

                val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)

                val storyData = StoryModel(
                    data.photoUrl,
                    data.name,
                    data.description
                )

                intent.putExtra(DetailStoryActivity.STORY_KEY, storyData)
                startActivity(intent)

            }
        })


    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_option -> {
                viewModel.logout()
            }
            R.id.map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}