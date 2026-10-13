package com.example.a8161359assignment2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.a8161359assignment2.databinding.ActivityMainBinding
import com.example.a8161359assignment2.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.content.Intent
import com.example.a8161359assignment2.ui.dashboard.DashboardActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            val keyboard = insets.getInsets(
                WindowInsetsCompat.Type.ime()
            )

            view.setPadding(
                bars.left,
                bars.top,
                bars.right,
                maxOf(bars.bottom, keyboard.bottom)
            )

            insets
        }

        binding.loginButton.setOnClickListener {
            viewModel.login(
                username = binding.studentIdInput.text?.toString().orEmpty(),
                password = binding.passwordInput.text?.toString().orEmpty()
            )
        }

        observeLoginState()
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.loadingIndicator.isVisible = state.isLoading

                    binding.loginButton.isEnabled = !state.isLoading
                    binding.studentIdInput.isEnabled = !state.isLoading
                    binding.passwordInput.isEnabled = !state.isLoading

                    binding.errorText.text = state.errorMessage
                    binding.errorText.isVisible =
                        state.errorMessage != null

                    // The temporary success message is no longer needed.
                    binding.successText.isVisible = false

                    val keypass = state.keypass

                    if (!keypass.isNullOrBlank()) {
                        val dashboardIntent = Intent(
                            this@MainActivity,
                            DashboardActivity::class.java
                        ).apply {
                            putExtra(
                                DashboardActivity.EXTRA_KEYPASS,
                                keypass
                            )
                        }

                        startActivity(dashboardIntent)
                        viewModel.onNavigationHandled()

                        // Clear the password before returning to Login later.
                        binding.passwordInput.text?.clear()
                    }
                }
            }
        }
    }
}