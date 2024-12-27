package com.example.storyapp__subs1.ui.core

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp__subs1.R
import com.example.storyapp__subs1.databinding.ActivityMainBinding
import com.example.storyapp__subs1.ui.auth.login.LoginActivity
import com.example.storyapp__subs1.ui.maps.MapsActivity
import com.example.storyapp__subs1.ui.story.StoryAdapter
import com.example.storyapp__subs1.ui.story.add.AddStoryActivity
import com.example.storyapp__subs1.ui.story.add.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            if (permissions[REQUIRED_PERMISSION_ACCESS_FINE_LOCATION] == true) {
                Toast.makeText(
                    this,
                    "Access fine location permission granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "Access fine location denied", Toast.LENGTH_SHORT)
                    .show()
            }

            if (permissions[REQUIRED_PERMISSION_ACCESS_COARSE_LOCATION] == true) {
                Toast.makeText(
                    this,
                    "Access coarse location permission granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Access coarse location denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private lateinit var binding: ActivityMainBinding

    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(
                arrayOf(
                    REQUIRED_PERMISSION_ACCESS_FINE_LOCATION,
                    REQUIRED_PERMISSION_ACCESS_COARSE_LOCATION
                )
            )
        }

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
                getData(token)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun allPermissionsGranted(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION_ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocationPermission = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION_ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fineLocationPermission && coarseLocationPermission
    }

    private fun getData(token: String) {
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        val adapter = StoryAdapter()
        binding.rvStory.adapter =  adapter.withLoadStateFooter(
            footer = LoadStateAdapt {
                adapter.retry()
            }
        )
        viewModel.getPagingStories(token).observe(this, {
            adapter.submitData(lifecycle, it)
        })

        adapter.addLoadStateListener { loadState ->

            binding.progressBarMain.visibility = if (loadState.source.refresh is LoadState.Loading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Error) {
                val errorMessage = (loadState.refresh as LoadState.Error).error.localizedMessage
                showToast(errorMessage ?: "Gagal memuat cerita")
                binding.textViewNoInternet.text = "Failed Load Stories, No Connection"
                binding.textViewNoInternet.visibility = View.VISIBLE
            }
        }
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
            R.id.action_map-> {
                intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val REQUIRED_PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val REQUIRED_PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }
}