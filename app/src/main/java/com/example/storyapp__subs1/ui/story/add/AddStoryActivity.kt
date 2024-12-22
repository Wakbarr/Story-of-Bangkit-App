package com.example.storyapp__subs1.ui.story.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storyapp__subs1.R
import com.example.storyapp__subs1.data.repository.UserViewModelFactory
import com.example.storyapp__subs1.databinding.ActivityAddStoryBinding
import com.example.storyapp__subs1.ui.core.MainActivity
import com.example.storyapp__subs1.utils.getImageUri

class AddStoryActivity : AppCompatActivity() {

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private lateinit var binding: ActivityAddStoryBinding
    var token = ""

    private val viewModel by viewModels<AddStoryViewModel> {
        UserViewModelFactory.getInstance(this)
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.currentImageUri?.let {
            showImage()
        }


        if (savedInstanceState != null) {
            val savedDesc = savedInstanceState.getString("desc")

            if (savedDesc != null) {
                binding.edAddDescription.setText(savedDesc)
            }
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }


        binding.progressBar.visibility = View.GONE

        binding.buttonGallery.setOnClickListener {
            startGallery()
        }

        binding.buttonCamera.setOnClickListener {
            startCamera()
        }

        viewModel.getSession().observe(this) { user ->
            token = "Bearer ${user.token}"
        }

        binding.buttonAdd.setOnClickListener {
            var description = binding.edAddDescription.text.toString()

            if (description.isEmpty()) {
                showToast("Please Add Image Description")
            } else if (viewModel.currentImageUri == null) {
                showToast("Please Add Your Image")

            } else if (description.isEmpty() && viewModel.currentImageUri == null) {
                showToast("Please Add Your Image & Description")
            } else if (!description.isEmpty() && viewModel.currentImageUri != null) {
                viewModel.uploadImage(description, this, token)
            }

        }

        viewModel.message.observe(this) { message ->
            message?.let {
                showToast(it)
            }
        }

        viewModel.isSuccess.observe(this) { it ->

            if (it == true) {
                intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

            }

        }

        viewModel.isLoading.observe(this) { loadingState ->
            if (loadingState == true) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("desc", binding.edAddDescription.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedDesc = savedInstanceState.getString("desc")

        if (savedDesc != null) {
            binding.edAddDescription.setText(savedDesc)
        }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            showImage()

        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private fun startCamera() {
        viewModel.currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(viewModel.currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            // User canceled or image capture failed
            viewModel.currentImageUri = null
            Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImage() {
        viewModel.currentImageUri?.let { uri ->
            Log.d("Image URI", "showImage: $uri")
            binding.imageViewAddStory.setImageURI(uri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}