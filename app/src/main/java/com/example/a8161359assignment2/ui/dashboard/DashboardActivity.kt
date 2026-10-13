package com.example.a8161359assignment2.ui.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a8161359assignment2.R
import com.example.a8161359assignment2.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.a8161359assignment2.ui.details.DetailsActivity

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private val viewModel: DashboardViewModel by viewModels()

    private val historyAdapter = HistoryAdapter { entity ->
        startActivity(
            DetailsActivity.createIntent(
                context = this,
                entity = entity
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                bars.left,
                bars.top,
                bars.right,
                bars.bottom
            )

            insets
        }

        val keypass = intent.getStringExtra(EXTRA_KEYPASS)

        if (keypass.isNullOrBlank()) {
            Toast.makeText(
                this,
                R.string.dashboard_missing_keypass,
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        binding.eventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = historyAdapter
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadDashboard(
                keypass = keypass,
                forceRefresh = true
            )
        }

        observeDashboardState()
        viewModel.loadDashboard(keypass)
    }

    private fun observeDashboardState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.loadingIndicator.isVisible =
                        state.isLoading

                    binding.errorText.text = state.errorMessage
                    binding.errorText.isVisible =
                        state.errorMessage != null

                    binding.retryButton.isVisible =
                        state.errorMessage != null

                    val showContent =
                        !state.isLoading && state.errorMessage == null

                    binding.eventCountText.isVisible = showContent
                    binding.eventCountText.text = getString(
                        R.string.dashboard_count,
                        state.entityTotal
                    )

                    binding.emptyText.isVisible =
                        showContent && state.entities.isEmpty()

                    binding.eventsRecyclerView.isVisible =
                        showContent && state.entities.isNotEmpty()

                    historyAdapter.submitList(state.entities)
                }
            }
        }
    }

    companion object {
        const val EXTRA_KEYPASS =
            "com.example.a8161359assignment2.extra.KEYPASS"
    }
}