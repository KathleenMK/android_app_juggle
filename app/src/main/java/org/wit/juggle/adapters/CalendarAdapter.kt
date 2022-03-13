package org.wit.juggle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.juggle.databinding.CardCalendarBinding
import org.wit.juggle.models.CalendarModel

interface CalendarClickListener {
    fun onCalendarClick(calendar: CalendarModel)
}

class CalendarAdapter constructor(private var calendars: ArrayList<CalendarModel>,
                                  private val listener: CalendarClickListener)
    : RecyclerView.Adapter<CalendarAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCalendarBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val calendar = calendars[holder.adapterPosition]
        holder.bind(calendar,listener)
    }

    override fun getItemCount(): Int = calendars.size

    inner class MainHolder(val binding : CardCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(calendar: CalendarModel, listener: CalendarClickListener) {
            binding.calendar = calendar
            binding.root.setOnClickListener { listener.onCalendarClick(calendar) }
            binding.executePendingBindings()
        }
    }
}