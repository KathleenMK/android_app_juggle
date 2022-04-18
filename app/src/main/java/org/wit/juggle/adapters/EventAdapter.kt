package org.wit.juggle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.juggle.databinding.CardEventBinding
import org.wit.juggle.models.EventModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

            var now = LocalDateTime.now()
            var tomorrow = LocalDateTime.now().plusDays(1)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedNow = now.format(formatter)
            val formattedTomorrow = tomorrow.format(formatter)
            var eventDate = event.start.dateTime.split("T")[0]
            if (eventDate == formattedNow)
            {
                eventDate = "Today"
            }
            else if(eventDate == formattedTomorrow)
            {
                eventDate = "Tomorrow"
            }

            //LocalDateTime.now().plusHours(1)


            binding.eventStart.setText(eventDate
                    +" @ "
            + event.start.dateTime.split("T")[1].split(":")[0]+":"+event.start.dateTime.split("T")[1].split(":")[1])
            binding.root.setOnClickListener { listener.onEventClick(event) }
            binding.executePendingBindings()
        }
    }


}