package com.example.storyapp__subs1.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.storyapp__subs1.R
import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.repository.Result
import com.example.storyapp__subs1.databinding.FragmentLoginActivityBinding
import com.example.storyapp__subs1.ui.auth.register.RegisterAcitivity
import com.example.storyapp__subs1.ui.core.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory(this)
    }
    private lateinit var binding: FragmentLoginActivityBinding
    private var email = ""
    private var password = ""

    companion object {
        private const val TAG = "LoginActivity"
        private const val MIN_PASSWORD_LENGTH = 8
        private const val ANIMATION_DURATION = 700L
        private const val FLOAT_ANIMATION_DURATION = 3000L
        private const val DELAY_BEFORE_MAIN = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = FragmentLoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restoreSavedInstanceState(savedInstanceState)
        setupViews()
        setupWindowInsets()
        playAnimation()
    }

    private fun restoreSavedInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.let { bundle ->
            bundle.getString("email")?.let { savedEmail ->
                binding.edLoginEmail.setText(savedEmail)
            }
            bundle.getString("password")?.let { savedPassword ->
                binding.edLoginPassword.setText(savedPassword)
            }
        }
    }

    private fun setupViews() {
        binding.progressBarLogin.visibility = View.GONE

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        binding.buttonGoRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun handleLogin() {
        email = binding.edLoginEmail.text.toString()
        password = binding.edLoginPassword.text.toString()

        when {
            email.isEmpty() || password.isEmpty() -> {
                showToast("All fields must be filled")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Invalid Email Format")
            }
            password.length < MIN_PASSWORD_LENGTH -> {
                showToast("Password less than 8 characters")
            }
            else -> {
                performLogin()
            }
        }
    }

    private fun performLogin() {
        viewModel.loginRequest(email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d(TAG, "Loading stories...")
                    binding.progressBarLogin.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBarLogin.visibility = View.GONE
                    val loginData = result.data
                    saveSession(loginData.name, loginData.userId, loginData.token)
                }
                is Result.Error -> {
                    binding.progressBarLogin.visibility = View.GONE
                    Log.e(TAG, "Error: ${result.error}")
                    showToast(result.error)
                }
            }
        }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterAcitivity::class.java))
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email", binding.edLoginEmail.text.toString())
        outState.putString("password", binding.edLoginPassword.text.toString())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveSession(name: String, userId: String, token: String) {
        lifecycleScope.launch {
            viewModel.saveSession(
                Model(
                    name = name,
                    userId = userId,
                    email = email,
                    token = token,
                    isLogin = true
                )
            )

            delay(DELAY_BEFORE_MAIN)
            withContext(Dispatchers.Main) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun playAnimation() {
        // Floating animation
        ObjectAnimator.ofFloat(binding.imageViewLogin, View.TRANSLATION_Y, -50f, 50f).apply {
            duration = FLOAT_ANIMATION_DURATION
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }

        // Fade in animations
        val animations = listOf(
            binding.textViewTitleLogin,
            binding.textInputEmail,
            binding.textInputPassword,
            binding.btnLogin,
            binding.textViewOrRegister,
            binding.buttonGoRegister
        ).map { view ->
            ObjectAnimator.ofFloat(view, View.ALPHA, 1f).apply {
                duration = ANIMATION_DURATION
            }
        }

        AnimatorSet().apply {
            playSequentially(*animations.toTypedArray())
            start()
        }
    }
}