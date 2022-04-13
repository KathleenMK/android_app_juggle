package org.wit.juggle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.juggle.databinding.CardRelatedeventBinding
import org.wit.juggle.models.RelatedEventModel
import org.wit.juggle.ui.eventview.EventViewFragment

interface RelatedEventClickListener {
    fun onEventClick(relatedEvent: RelatedEventModel)
}

class RelatedEventAdapter(private var relatedEvents: ArrayList<RelatedEventModel>,
                          private val listener: RelatedEventClickListener
)
    : RecyclerView.Adapter<RelatedEventAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardRelatedeventBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val relatedEvent = relatedEvents[holder.adapterPosition]
        holder.bind(relatedEvent,listener)
    }

    override fun getItemCount(): Int = relatedEvents.size

    inner class MainHolder(val binding : CardRelatedeventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(relatedEvent: RelatedEventModel, listener: RelatedEventClickListener) {
            binding.relatedEvent = relatedEvent
            binding.root.setOnClickListener { listener.onEventClick(relatedEvent) }
            binding.executePendingBindings()
        }
    }


}