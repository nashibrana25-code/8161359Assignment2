package com.example.a8161359assignment2.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a8161359assignment2.R
import com.example.a8161359assignment2.data.model.HistoryEntity
import com.example.a8161359assignment2.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailsBinding.inflate(layoutInflater)
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

        val entity = readEntity()

        if (entity == null) {
            Toast.makeText(
                this,
                R.string.details_missing_event,
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        displayEntity(entity)

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun readEntity(): HistoryEntity? {
        val event = intent.getStringExtra(EXTRA_EVENT)
            ?: return null

        val location = intent.getStringExtra(EXTRA_LOCATION)
            ?: return null

        val keyFigure = intent.getStringExtra(EXTRA_KEY_FIGURE)
            ?: return null

        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
            ?: return null

        if (
            !intent.hasExtra(EXTRA_START_YEAR) ||
            !intent.hasExtra(EXTRA_END_YEAR)
        ) {
            return null
        }

        return HistoryEntity(
            event = event,
            startYear = intent.getIntExtra(EXTRA_START_YEAR, 0),
            endYear = intent.getIntExtra(EXTRA_END_YEAR, 0),
            location = location,
            keyFigure = keyFigure,
            description = description
        )
    }

    private fun displayEntity(entity: HistoryEntity) {
        binding.eventNameText.text = entity.event

        binding.startYearText.text = getString(
            R.string.details_start_year,
            entity.startYear
        )

        binding.endYearText.text = getString(
            R.string.details_end_year,
            entity.endYear
        )

        binding.locationText.text = getString(
            R.string.event_location,
            entity.location
        )

        binding.keyFigureText.text = getString(
            R.string.event_key_figure,
            entity.keyFigure
        )

        binding.descriptionText.text = entity.description
    }

    companion object {
        private const val EXTRA_EVENT = "details.event"
        private const val EXTRA_START_YEAR = "details.startYear"
        private const val EXTRA_END_YEAR = "details.endYear"
        private const val EXTRA_LOCATION = "details.location"
        private const val EXTRA_KEY_FIGURE = "details.keyFigure"
        private const val EXTRA_DESCRIPTION = "details.description"

        fun createIntent(
            context: Context,
            entity: HistoryEntity
        ): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_EVENT, entity.event)
                putExtra(EXTRA_START_YEAR, entity.startYear)
                putExtra(EXTRA_END_YEAR, entity.endYear)
                putExtra(EXTRA_LOCATION, entity.location)
                putExtra(EXTRA_KEY_FIGURE, entity.keyFigure)
                putExtra(EXTRA_DESCRIPTION, entity.description)
            }
        }
    }
}