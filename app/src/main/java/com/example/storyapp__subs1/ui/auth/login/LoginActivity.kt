package com.example.storyapp__subs1.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.storyapp__subs1.ui.core.MainActivity
import com.example.storyapp__subs1.R
import com.example.storyapp__subs1.data.preferensi.Model
import com.example.storyapp__subs1.data.repository.UserViewModelFactory
import com.example.storyapp__subs1.databinding.FragmentLoginActivityBinding
import com.example.storyapp__subs1.ui.auth.register.RegisterAcitivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        UserViewModelFactory.getInstance(this)
    }
    private lateinit var binding: FragmentLoginActivityBinding
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = FragmentLoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState != null) {
            val savedEmail = savedInstanceState.getString("email")
            val savedPassword = savedInstanceState.getString("password")

            if (savedEmail != null) {
                binding.edLoginEmail.setText(savedEmail)
            }

            if (savedPassword != null) {
                binding.edLoginPassword.setText(savedPassword)
            }
        }

        playAnimation()


        binding.progressBarLogin.visibility = View.GONE

        binding.btnLogin.setOnClickListener {
            email = binding.edLoginEmail.text.toString()
            password = binding.edLoginPassword.text.toString()

            if (binding.edLoginEmail.text.toString().isEmpty() ||
                binding.edLoginPassword.text.toString().isEmpty()
            ) {
                showToast("All field must be filled")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Invalid Email Format")
            } else if (binding.edLoginPassword.text.toString().length < 8) {
                showToast("Password less than 8 Char")
            } else {
                viewModel.loginRequest(email, password)
            }

            viewModel.loginValue.observe(this) { loginResponse ->
                lifecycleScope.launch {
                    viewModel.saveSession(
                        Model(
                            name = loginResponse.name,
                            userId = loginResponse.userId,
                            email = email,
                            token = loginResponse.token,
                            isLogin = true
                        )
                    )

                    delay(1000)
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }

        }

        viewModel.message.observe(this) { message ->
            message?.let {
                showToast(it)
            }

        }

        viewModel.isLoading.observe(this) { loadingState ->
            if (loadingState == true) {
                binding.progressBarLogin.visibility = View.VISIBLE
            } else {
                binding.progressBarLogin.visibility = View.GONE
            }
        }

        binding.buttonGoRegister.setOnClickListener {
            intent = Intent(this, RegisterAcitivity::class.java)
            startActivity(intent)
        }






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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedEmail = savedInstanceState.getString("email")
        val savedPassword = savedInstanceState.getString("password")

        if (savedEmail != null) {
            binding.edLoginEmail.setText(savedEmail)
        }

        if (savedPassword != null) {
            binding.edLoginPassword.setText(savedPassword)
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.imageViewLogin, View.TRANSLATION_Y, -50f, 50f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleText =
            ObjectAnimator.ofFloat(binding.textViewTitleLogin, View.ALPHA, 1f).setDuration(700)
        val emailEditText =
            ObjectAnimator.ofFloat(binding.textInputEmail, View.ALPHA, 1f).setDuration(700)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.textInputPassword, View.ALPHA, 1f).setDuration(700)

        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(700)

        val textOr =
            ObjectAnimator.ofFloat(binding.textViewOrRegister, View.ALPHA, 1f).setDuration(700)

        val buttonGoRegister =
            ObjectAnimator.ofFloat(binding.buttonGoRegister, View.ALPHA, 1f).setDuration(700)


        AnimatorSet().apply {
            playSequentially(
                titleText,
                emailEditText,
                passwordEditText,
                buttonLogin,
                textOr,
                buttonGoRegister
            )
            start()
        }
    }
}