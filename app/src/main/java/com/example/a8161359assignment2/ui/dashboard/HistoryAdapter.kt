package com.example.a8161359assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a8161359assignment2.R
import com.example.a8161359assignment2.data.model.HistoryEntity
import com.example.a8161359assignment2.databinding.ItemHistoryEventBinding

class HistoryAdapter(
    private val onEventClick: (HistoryEntity) -> Unit
) : ListAdapter<HistoryEntity, HistoryAdapter.EventViewHolder>(
    EventDiffCallback
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val binding = ItemHistoryEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return EventViewHolder(binding, onEventClick)
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class EventViewHolder(
        private val binding: ItemHistoryEventBinding,
        private val onEventClick: (HistoryEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: HistoryEntity) {
            val context = binding.root.context

            binding.eventNameText.text = entity.event

            binding.eventYearsText.text =
                if (entity.startYear == entity.endYear) {
                    context.getString(
                        R.string.event_single_year,
                        entity.startYear
                    )
                } else {
                    context.getString(
                        R.string.event_year_range,
                        entity.startYear,
                        entity.endYear
                    )
                }

            binding.eventLocationText.text = context.getString(
                R.string.event_location,
                entity.location
            )

            binding.eventKeyFigureText.text = context.getString(
                R.string.event_key_figure,
                entity.keyFigure
            )

            binding.root.setOnClickListener {
                onEventClick(entity)
            }
        }
    }

    private companion object EventDiffCallback :
        DiffUtil.ItemCallback<HistoryEntity>() {

        override fun areItemsTheSame(
            oldItem: HistoryEntity,
            newItem: HistoryEntity
        ): Boolean {
            return oldItem.event == newItem.event &&
                    oldItem.startYear == newItem.startYear &&
                    oldItem.location == newItem.location
        }

        override fun areContentsTheSame(
            oldItem: HistoryEntity,
            newItem: HistoryEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}