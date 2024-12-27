package com.example.storyapp__subs1.ui.auth.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp__subs1.R
import com.example.storyapp__subs1.databinding.FragmentRegisterAcitivityBinding
import com.example.storyapp__subs1.ui.auth.login.LoginActivity

class RegisterAcitivity : AppCompatActivity() {
    private lateinit var binding: FragmentRegisterAcitivityBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = FragmentRegisterAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        if (savedInstanceState != null) {
            val savedEmail = savedInstanceState.getString("email")
            val savedPassword = savedInstanceState.getString("password")
            val savedName = savedInstanceState.getString("name")

            if (savedEmail != null) {
                binding.edRegisterName.setText(savedEmail)
            }

            if (savedPassword != null) {
                binding.edRegisterPassword.setText(savedPassword)
            }

            if (savedName != null) {
                binding.edRegisterPassword.setText(savedName)
            }
        }

        playAnimation()

        binding.progressBarRegister.visibility = View.GONE


        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (binding.edRegisterName.text.toString().isEmpty() ||
                binding.edRegisterEmail.text.toString().isEmpty() ||
                binding.edRegisterPassword.text.toString().isEmpty()
            ) {
                showToast("All field must be filled")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Invalid Email Format")
            } else if (binding.edRegisterPassword.text.toString().length < 8) {
                showToast("Password less than 8 Char")
            } else {
                viewModel.registerRequest(name, email, password)
            }



            viewModel.isSuccess.observe(this) { isSuccess ->

                if (isSuccess == true) {
                    val intent = Intent(this@RegisterAcitivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }

            }
        }

        viewModel.isLoading.observe(this) { loadingState ->
            if (loadingState == true) {
                binding.progressBarRegister.visibility = View.VISIBLE
            } else {
                binding.progressBarRegister.visibility = View.GONE
            }
        }

        viewModel.message.observe(this) { message ->
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email", binding.edRegisterEmail.text.toString())
        outState.putString("password", binding.edRegisterPassword.text.toString())
        outState.putString("name", binding.edRegisterName.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedEmail = savedInstanceState.getString("email")
        val savedPassword = savedInstanceState.getString("password")
        val savedName = savedInstanceState.getString("name")

        if (savedEmail != null) {
            binding.edRegisterEmail.setText(savedEmail)
        }

        if (savedPassword != null) {
            binding.edRegisterPassword.setText(savedPassword)
        }

        if (savedName != null) {
            binding.edRegisterPassword.setText(savedName)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.imageViewRegister, View.TRANSLATION_Y, -50f, 50f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val nameText = ObjectAnimator.ofFloat(binding.textInputLayoutRegisterName, View.ALPHA, 1f)
            .setDuration(700)

        val titleText =
            ObjectAnimator.ofFloat(binding.textViewTitleRegister, View.ALPHA, 1f).setDuration(700)
        val emailEditText =
            ObjectAnimator.ofFloat(binding.textInputEmailRegister, View.ALPHA, 1f).setDuration(700)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.textInputPasswordRegister, View.ALPHA, 1f)
                .setDuration(700)

        val buttonLogin =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(700)


        val buttonGoRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(700)


        AnimatorSet().apply {
            playSequentially(
                titleText,
                nameText,
                emailEditText,
                passwordEditText,
                buttonLogin,
                buttonGoRegister
            )
            start()
        }
    }
}