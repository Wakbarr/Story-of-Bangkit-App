package com.example.storyapp__subs1.ui.story.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.storyapp__subs1.R
import com.example.storyapp__subs1.data.repository.Result
import com.example.storyapp__subs1.data.respons.AddStoryRespons
import com.example.storyapp__subs1.databinding.ActivityAddStoryBinding
import com.example.storyapp__subs1.ui.core.MainActivity
import com.example.storyapp__subs1.utils.getImageUri
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel by viewModels<AddStoryViewModel> { ViewModelFactory(this) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Request for permissions
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            val message = if (isGranted) "Permission request granted" else "Permission request denied"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    private var token = ""
    private var lat = 0.0
    private var lon = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize binding
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable location and permissions
        enableMyLocation()

        // Restore description if any
        savedInstanceState?.getString("desc")?.let {
            binding.edAddDescription.setText(it)
        }

        // Request camera permission if not granted
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        // Initial progress bar visibility
        binding.progressBar.visibility = View.GONE

        // Setup image picker actions
        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonCamera.setOnClickListener { startCamera() }

        // Observe session to get token
        viewModel.getSession().observe(this) { user ->
            token = "Bearer ${user.token}"
        }

        // Handle "Add Story" button click
        binding.buttonAdd.setOnClickListener {
            val description = binding.edAddDescription.text.toString()

            when {
                description.isEmpty() -> showToast("Please Add Image Description")
                viewModel.currentImageUri == null -> showToast("Please Add Your Image")
                binding.switchEnableLocation.isChecked -> uploadStoryWithLocation(description)
                else -> uploadStoryWithoutLocation(description)
            }
        }

        // Apply padding for edge-to-edge design
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        setupLocationClient()

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                lat = it.latitude
                lon = it.longitude
            }
        }
    }

    private fun setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun uploadStoryWithLocation(description: String) {
        viewModel.getUploadStory(description, this, token, viewModel.currentImageUri!!, lat, lon).observe(this) { result ->
            handleResult(result)
        }
    }

    private fun uploadStoryWithoutLocation(description: String) {
        viewModel.getUploadStoryWithoutLoc(description, this, token, viewModel.currentImageUri!!).observe(this) { result ->
            handleResult(result)
        }
    }

    private fun handleResult(result: Result<AddStoryRespons>) {
        when (result) {
            is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
            is Result.Success -> {
                binding.progressBar.visibility = View.GONE
                showToast(result.data.message)
                finish()
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                showToast(result.error)
            }
        }
    }
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let {
            viewModel.currentImageUri = uri
            showImage()
        }
    }

    private fun startCamera() {
        viewModel.currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(viewModel.currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            viewModel.currentImageUri = null
            Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImage() {
        viewModel.currentImageUri?.let { uri ->
            binding.imageViewAddStory.setImageURI(uri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
