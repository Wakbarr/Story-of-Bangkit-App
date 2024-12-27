package com.example.storyapp__subs1.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.example.storyapp__subs1.R
import com.example.storyapp__subs1.databinding.FragmentMapAcitivityBinding
import com.example.storyapp__subs1.ui.auth.login.LoginActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// Add the sealed class if it's not in a separate file
sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapAcitivityBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory(this)
    }

    private var token = ""
    private var desc = ""
    private var imgUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentMapAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.placeInfoCard.visibility = View.GONE

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        try {
            enableMyLocation()
        } catch (e: Exception) {
            showToast("Failed to initialize map.")
            Log.e(TAG, "Error initializing map: ", e)
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnMarkerClickListener { marker ->
            val location = LatLng(
                marker.position.latitude,
                marker.position.longitude
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 7f))

            binding.placeInfoCard.visibility = View.VISIBLE
            marker.snippet?.let {
                val infoParts = it.split("|")

                imgUrl = infoParts.find { part -> part.contains("ImageUrl:") }
                    ?.substringAfter("ImageUrl:")?.trim() ?: ""

                desc = infoParts.find { part -> part.contains("Desc:") }
                    ?.substringAfter("Desc:")?.trim() ?: "No Desc"
            }

            Glide.with(this)
                .load(imgUrl)
                .into(binding.imageView)

            binding.tvName.text = marker.title
            binding.tvDesc.text = desc

            true
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                token = "Bearer ${user.token}"
                viewModel.getStories(token).observe(this) { result ->
                    when (result) {
                        is com.example.storyapp__subs1.data.repository.Result.Loading -> {
                            Log.d("MapsActivity", "Loading stories...")
                        }
                        is com.example.storyapp__subs1.data.repository.Result.Success -> {
                            val stories = result.data
                            Log.d("MapsActivity", "Fetched ${stories.size} stories")
                            for (story in stories) {
                                Log.d("MapsActivity", "Story: ${story.name}")
                                val location = LatLng(
                                    story.lat.toString().toDouble(),
                                    story.lon.toString().toDouble()
                                )
                                mMap.addMarker(
                                    MarkerOptions().position(location)
                                        .title(story.name)
                                        .snippet("ImageUrl: ${story.photoUrl}" + " | Desc: ${story.description}")
                                        .icon(
                                            vectorToBitmap(
                                                R.drawable.story_24,
                                                Color.parseColor("#ffffff")
                                            )
                                        )
                                )
                            }
                        }
                        is com.example.storyapp__subs1.data.repository.Result.Error -> {
                            Log.e("MapsActivity", "Error: ${result.error}")
                        }
                    }
                }
            }
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
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
        setupLocationClient()

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userLocation = LatLng(it.latitude, it.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 5f))
                mMap.addMarker(
                    MarkerOptions().position(userLocation)
                        .title("Your Location")
                )
            } ?: run {
                showToast("Failed to get current location.")
            }
        }.addOnFailureListener {
            showToast("Failed to get current location.")
            Log.e(TAG, "Error getting location: ", it)
        }
    }

    private fun setupLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "MapsActivity"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}