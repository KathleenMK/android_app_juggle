package org.wit.juggle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.juggle.databinding.CardEventBinding
import org.wit.juggle.models.EventModel

interface EventClickListener {
    fun onEventClick(event: EventModel)
}

class EventAdapter(private var events: ArrayList<EventModel>,
                   private val listener: EventClickListener)
    : RecyclerView.Adapter<EventAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardEventBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val event = events[holder.adapterPosition]
        holder.bind(event,listener)
    }

    override fun getItemCount(): Int = events.size

    inner class MainHolder(val binding : CardEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventModel, listener: EventClickListener) {
            binding.event = event
            binding.root.setOnClickListener { listener.onEventClick(event) }
            binding.executePendingBindings()
        }
    }


}